package com.example.backend.spring.controller;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.example.backend.dataProcessing.Predicting;
import com.example.backend.dataProcessing.Training;
import com.example.backend.spring.entity.Device;
import com.example.backend.spring.entity.Entry;
import com.example.backend.spring.entity.User;
import com.example.backend.spring.implement.ImplementUserDetailsService;
import com.example.backend.spring.repository.DeviceRepository;
import com.example.backend.spring.repository.EntryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.datavec.api.formats.input.impl.CSVInputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping()
public class MainController {

    @Autowired
    private ImplementUserDetailsService userDetailsService;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    /**
     * Return index (dashboard) view to frontend
     * Accessible only after being authenticated
     */
    @GetMapping("/")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        return modelAndView;
    }

    /**
     * Return the user of current session
     * Return empty if no session exists
     */
    @GetMapping(value = "/getUser", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userDetailsService.getUser(authentication.getName());

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(user);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }

    }

    /**
     * Save the dataset to database based on user uploaded CSV file
     * Return empty if no file is uploaded
     *
     * @param id   the user id of the file owner
     * @param file uploaded CSV file
     */
    @PostMapping("/uploadDataset")
    public String uploadDataset(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {

        if (!file.isEmpty()) {

            try {

                CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                String[] row;

                // Read the file line-by-line into String array
                while ((row = csvReader.readNext()) != null) {

                    // Convert array to arraylist
                    ArrayList<String> rowArray = new ArrayList<>(Arrays.asList(row));
                    rowArray.removeAll(Collections.singleton(null));
                    rowArray.removeAll(Collections.singleton(""));

                    // Get user of current session
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    User user = userDetailsService.getUser(authentication.getName());

                    // Save device
                    Device device = new Device(rowArray.get(0), user);
                    if (deviceRepository.findByUuid(rowArray.get(0)) == null) {
                        deviceRepository.save(device);
                    } else {
                        device = deviceRepository.findByUuid(rowArray.get(0));
                    }

                    // Save entry
                    Entry entry = new Entry(rowArray, device);
                    entryRepository.save(entry);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return "";
    }

    /**
     * Return user requested filtered entry(s) from database
     * Return empty if no requested entry(s) is found
     *
     * @param body request body from frontend
     *             count: no. of requested entry(s)
     *             period: range of timestamp
     *             temperature: range of temperature
     */
    @PostMapping("/getData")
    public String getData(@RequestBody String body) {

        JsonObject deserializedBody = JsonParser.parseString(body).getAsJsonObject();

        int count = deserializedBody.get("filter").getAsJsonObject().get("count").getAsInt();

        JsonArray period = deserializedBody.get("filter").getAsJsonObject().get("period").getAsJsonArray();
        long start = period.get(0).getAsLong();
        long end = period.get(1).getAsLong();

        JsonArray temperature = deserializedBody.get("filter").getAsJsonObject().get("temperature").getAsJsonArray();
        float min = temperature.get(0).getAsFloat();
        float max = temperature.get(1).getAsFloat();

        List<Entry> entries = entryRepository.findEntries(count, start, end, min, max);

        System.out.println(entries);

//        Map<Long, List<Entry>> hashMap = Optional.ofNullable(entries)
//                .orElseGet(ArrayList::new)
//                .stream()
//                .collect(Collectors
//                        .groupingBy(Entry::getDeviceId));

        if (!entries.isEmpty()) {
            return new Gson().toJson(entries);
        } else {
            return "[]";
        }

    }

    /**
     * Run the training process
     * based on the dataset obtained from database
     *
     * @param body request body from frontend
     *             id: user id of current user
     */
    @PostMapping("/train")
    public String train(@RequestBody String body) {

        JsonObject deserializedBody = JsonParser.parseString(body).getAsJsonObject();
        String id = deserializedBody.get("id").getAsString();

        JsonObject hyperParameters = deserializedBody.get("hyperparameters").getAsJsonObject();

        int windowSize = hyperParameters.get("window").getAsInt();

        List<Entry> entries = entryRepository.findAllSortedByTimestamp();

        try {

            List<List<Float>> features = new ArrayList<>();
            List<List<Float>> labels = new ArrayList<>();

            for (int i = windowSize; i < entries.size(); i++) {

                List<Float> feature = new ArrayList<>();
                List<Float> label = new ArrayList<>();

                for (int j = windowSize; j > 0; j--) {
                    feature.add(entries.get(i - j).getTemperature());
                }

                label.add(entries.get(i).getTemperature());

                features.add(feature);
                labels.add(label);

            }

            Training training = new Training(id, features, labels, hyperParameters);
            return training.trainingProcess();

        } catch (IOException | InterruptedException exception) {
            System.out.println(exception);
        }

        return "";
    }

    @PostMapping("/predict")
    public String predict(@RequestBody String body){
        JsonObject deserializedBody = JsonParser.parseString(body).getAsJsonObject();
        String id = deserializedBody.get("id").getAsString();

        List<Entry> entries = entryRepository.findAllSortedByTimestamp();

        try {

            List<List<Float>> features = new ArrayList<>();

            for (int i = 5; i < entries.size(); i++) {

                List<Float> feature = new ArrayList<>();

                for (int j = 5; j > 0; j--) {
                    feature.add(entries.get(i - j).getTemperature());
                }
                features.add(feature);
            }

            Predicting predicting = new Predicting(id, features);
            List<Double> predictions = predicting.predictions();

            return new Gson().toJson(predictions);

        } catch (IOException | InterruptedException exception) {
            System.out.println(exception);
        }

        return "";
    }

}

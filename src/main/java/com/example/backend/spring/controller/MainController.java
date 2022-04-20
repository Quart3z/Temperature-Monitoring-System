package com.example.backend.spring.controller;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import com.example.backend.spring.entity.Device;
import com.example.backend.spring.entity.Entry;
import com.example.backend.spring.entity.User;
import com.example.backend.spring.implement.ImplementUserDetailsService;
import com.example.backend.spring.repository.DeviceRepository;
import com.example.backend.spring.repository.EntryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.datavec.api.formats.input.impl.CSVInputFormat;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collector;
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

    @GetMapping("/")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        return modelAndView;
    }

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

    @PostMapping("/uploadDataset")
    public String uploadDataset(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {

        if (!file.isEmpty()) {

            try {

                CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                String[] row;

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
                    }else{
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

    @PostMapping("/getData")
    public String getData(@RequestBody String body) {

        JsonObject deserializedBody = JsonParser.parseString(body).getAsJsonObject();

        System.out.println(deserializedBody);

        int count = deserializedBody.get("filter").getAsJsonObject().get("count").getAsInt();
        Collection<Entry> entries = entryRepository.findEntries(count);

        Map<Long, List<Entry>> hashMap = Optional.ofNullable(entries)
                .orElseGet(ArrayList::new)
                .stream()
                .collect(Collectors
                        .groupingBy(Entry::getDeviceId));
        
        if (!entries.isEmpty()) {
            return new Gson().toJson(hashMap);
        } else {
            return "[]";
        }

    }


}

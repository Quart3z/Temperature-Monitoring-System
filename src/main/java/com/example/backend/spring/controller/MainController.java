package com.example.backend.spring.controller;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import com.example.backend.spring.entity.Entry;
import com.example.backend.spring.entity.User;
import com.example.backend.spring.implement.ImplementUserDetailsService;
import com.example.backend.spring.repository.EntryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.datavec.api.formats.input.impl.CSVInputFormat;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RestController
@RequestMapping()
public class MainController {

    @Autowired
    private ImplementUserDetailsService userDetailsService;

    @Autowired
    private EntryRepository entryRepository;

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

    @PostMapping("/getDatasets")
    public List<String> getDatasets(@RequestBody String body) throws JSONException {

        JSONObject json = new JSONObject(body);
        int id = json.getInt("id");

        List<String> availableDatasets = new ArrayList<>();
        File userFolder = new File("src/main/resources/users/" + id + "");

        if (!userFolder.exists()) {
            userFolder.mkdir();
        }

        for (File entry : Objects.requireNonNull(userFolder.listFiles())) {
            availableDatasets.add(entry.getName());
        }

        return availableDatasets;

    }

    @PostMapping("/getDataset")
    public String getDataset(@RequestBody String body) throws JSONException {

        JSONObject json = new JSONObject(body);
        String id = json.getString("id");
        String filename = json.getString("filename");

        File selectedFile = new File("src/main/resources/users/" + id + "/" + filename);
        return "{}";
    }

    @PostMapping("/uploadDataset")
    public String uploadDataset(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {

        if (!file.isEmpty()) {

            try {

                CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                String[] row;

                while ((row = csvReader.readNext()) != null) {

                    ArrayList<String> rowArray = new ArrayList<>(Arrays.asList(row));

                    for(int i = 0; i < rowArray.size(); i++){

                        if(rowArray.get(i).isEmpty()){
                            rowArray.remove(i);
                        }

                    }

                    System.out.println(rowArray);

//                    Entry entry = new Entry(rowArray);
//                    entryRepository.save(entry);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return "";
    }

}

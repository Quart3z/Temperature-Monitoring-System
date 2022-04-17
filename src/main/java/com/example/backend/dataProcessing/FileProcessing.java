package com.example.backend.dataProcessing;

import au.com.bytecode.opencsv.CSVReader;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.nd4j.common.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileProcessing {


    public static void main(String[] args) throws IOException {

        try {
            FileReader filereader = new FileReader("src/main/resources/users/31/temperature.csv");

            // create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.example.backend.dataProcessing;

import com.example.backend.spring.entity.Entry;
import org.datavec.api.records.Record;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.collection.CollectionRecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.records.reader.impl.jackson.FieldSelection;
import org.datavec.api.records.reader.impl.jackson.JacksonRecordReader;
import org.datavec.api.split.CollectionInputSplit;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.api.split.ListStringSplit;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.shade.jackson.core.JsonFactory;
import org.nd4j.shade.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Training {

    /**
     * ====================
     * Attributes & columns
     * ====================
     */
    private static final int seed = 100;
    private static final double learningRate = 0.001;
    private static int epoch;

    private int userId;

    /**
     * =================
     * Class constructor
     * =================
     */
    public Training(int userId) {
        this.userId = userId;
    }

    /**
     * =======================
     * Accessors and mutators
     * =======================
     */

    /**
     * =======================
     * Methods
     * =======================
     */
    public static RecordReader datasetReading(int userId) throws IOException, InterruptedException {

        File file = new ClassPathResource("src/main/resources/users/" + userId + "/dataset.csv").getFile();
        FileSplit fileSplit = new FileSplit(file);
        RecordReader reader = new CSVRecordReader(0, ",");
        reader.initialize(fileSplit);

        return reader;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

    }

    public void dataPreprocessing() {
    }

    public void networkConfig() {

        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .activation(Activation.RELU)
                .updater(new Nesterovs(learningRate, Nesterovs.DEFAULT_NESTEROV_MOMENTUM))
                .weightInit(WeightInit.XAVIER)
                .list()
//                .layer()
//                .layer()
//                .layer()
                .build();

    }

    public void trainingProcess() throws IOException, InterruptedException {

    }

}

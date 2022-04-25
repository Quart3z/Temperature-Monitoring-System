package com.example.backend.dataProcessing;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.collection.CollectionRecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.evaluation.regression.RegressionEvaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.ViewIterator;

import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

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
    private static final int batchSize = 3;
    private static final int seed = 100;
    private static final double learningRate = 0.1;
    private static final int epoch = 10;

    private final String userId;
    private NormalizerMinMaxScaler normalizer;

    /**
     * =================
     * Class constructor
     * =================
     */
    public Training(String userId) {
        this.userId = userId;
    }

    /**
     * =======================
     * Methods
     * =======================
     */
    public DataSetIterator dataReading() throws IOException, InterruptedException {

        // 1.1 Read from exported CSV file
        File file = new ClassPathResource("users/" + userId + "/dataset.csv").getFile();
        FileSplit fileSplit = new FileSplit(file);
        RecordReader reader = new CSVRecordReader();
        reader.initialize(fileSplit);

        List<List<Writable>> list = new ArrayList<>();

        while (reader.hasNext()) {
            list.add(reader.next());
        }

        CollectionRecordReader collectionRR = new CollectionRecordReader(list);

        return new RecordReaderDataSetIterator(collectionRR, list.size(), 0, 0, true);
    }

    public DataSetIterator[] dataProcessing(DataSet dataSet) {

        // 2.1 Data splitting
        dataSet.shuffle(seed);
        SplitTestAndTrain testAndTrain = dataSet.splitTestAndTrain(0.8);
        DataSet[] splitDataset = {testAndTrain.getTrain(), testAndTrain.getTest()};

        // 2.2 Data normalization
        normalizer = new NormalizerMinMaxScaler();
        normalizer.fitLabel(true);
        normalizer.fit(splitDataset[0]);
        normalizer.transform(splitDataset[0]);
        normalizer.transform(splitDataset[1]);

        return new DataSetIterator[]{new ViewIterator(splitDataset[0], batchSize), new ViewIterator(splitDataset[0], batchSize)};

    }

    public MultiLayerNetwork networkConfig() {

        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .activation(Activation.RELU)
                .updater(new Nesterovs(learningRate, 0.9))
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(3)
                        .nOut(4)
                        .build())
                .layer(new OutputLayer.Builder()
                        .nIn(4)
                        .nOut(1)
                        .lossFunction(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .build())
                .build();

        return new MultiLayerNetwork(config);

    }

    public void trainingProcess() throws IOException, InterruptedException {

        // 1. Obtain the dataset from the generated CSV file
        DataSetIterator preIterator = dataReading();

        // 2. Preprocessing
        DataSetIterator[] dataSetIterators = dataProcessing(preIterator.next());

        // 2. Configuration of the network
        MultiLayerNetwork model = networkConfig();
        model.init();
        model.setListeners(new ScoreIterationListener(1));

        // 3. Training
        model.fit(dataSetIterators[0], epoch);

        // 4. Evaluation
        RegressionEvaluation evalTrain = model.evaluateRegression(dataSetIterators[0]);
        System.out.println(evalTrain.stats());

        RegressionEvaluation evalTest = model.evaluateRegression(dataSetIterators[1]);
        System.out.println(evalTest.stats());

        final INDArray input = Nd4j.create(new double[]{11, 305, 57534156}, 1, 3);
//        final INDArray input = Nd4j.create(new double[]{11, 305, 35874122}, 1, 3);
        normalizer.transform(input);
        INDArray out = model.output(input, false);
        System.out.println(out.getDouble(0)/2);

    }

}

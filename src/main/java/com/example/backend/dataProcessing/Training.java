package com.example.backend.dataProcessing;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.regression.RegressionEvaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.ViewIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * ==============================
 * LSTM for temperature prediction
 * ==============================
 * <p>
 * The modal took the previous reading from the database
 * and predicts the future temperature
 */
public class Training {

    /**
     * =================
     * Hyper parameters
     * =================
     */
    private static final int seed = 100;
    private static final double learningRate = 0.01;
    private static final int epoch = 800;

    /**
     * ===============
     * Class attributes
     * ===============
     */
    private final String userId;
    private final List<Float[]> features;
    private final List<Float[]> labels;

    /**
     * =================
     * Class constructor
     * =================
     */
    public Training(String userId, List<Float[]> features, List<Float[]> labels) {
        this.userId = userId;
        this.features = features;
        this.labels = labels;
    }

    /**
     * Read and convert dataset from
     * database into training and label sets
     */
    public DataSet dataReading() throws IOException, InterruptedException {

        INDArray featuresArray = Nd4j.zeros(features.size(), 3, 1);
        INDArray labelsArray = Nd4j.zeros(labels.size(), 1);

        for (int i = 0; i < features.size(); i++) {
            featuresArray.putScalar(new int[]{i, 0, 0}, features.get(i)[0]);
            featuresArray.putScalar(new int[]{i, 1, 0}, features.get(i)[1]);
            featuresArray.putScalar(new int[]{i, 2, 0}, features.get(i)[2]);

            labelsArray.putScalar(new int[]{i, 0}, labels.get(i)[0]);
        }

        return new DataSet(featuresArray, labelsArray);

    }

    /**
     * Configuration of the LSTM neural network
     * Hyper parameters can be manually set at Hyper Parameters section
     * <p>
     * Structure of neural net:
     * LSTMLayer - nOutput: 100
     * DenseLayer - nOutput: 32
     * OutputLayer - nOutput: 1
     */
    public MultiLayerNetwork networkConfig() {

        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .updater(new Adam(learningRate))
                .list()
                .layer(new LSTM.Builder()
                        .nIn(3)
                        .nOut(100)
                        .build())
                .layer(new DenseLayer.Builder()
                        .nIn(100)
                        .nOut(32)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder()
                        .nIn(32)
                        .nOut(1)
                        .activation(Activation.IDENTITY)
                        .lossFunction(LossFunctions.LossFunction.MSE)
                        .build())
                .build();

        return new MultiLayerNetwork(config);

    }

    /**
     * Where the training process takes place.
     * After being trained, the modal is saved to the user's folder accordingly
     */
    public String trainingProcess() throws IOException, InterruptedException {

        // 1. Obtain the dataset from database
        DataSet dataSet = dataReading();

        // 2. Configuration of the network
        MultiLayerNetwork modal = networkConfig();
        modal.init();
        modal.setListeners(new ScoreIterationListener(5));

        // 3. Training
        for (int i = 0; i < epoch; i++) {
            modal.fit(dataSet);
        }

        // 4. Save modal
        modal.save(new File("src/main/resources/users/" + userId + "/modal.bin"));

        DataSetIterator iterator = new ViewIterator(dataSet, 1);

        RegressionEvaluation evaluation = modal.evaluateRegression(iterator);

        return evaluation.stats();

    }

}

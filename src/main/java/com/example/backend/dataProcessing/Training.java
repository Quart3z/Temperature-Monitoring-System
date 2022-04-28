package com.example.backend.dataProcessing;

import com.google.gson.JsonObject;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.regression.RegressionEvaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
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
     * ===============
     * Class attributes
     * ===============
     */
    private final String userId;
    private final List<List<Float>> features;
    private final List<List<Float>> labels;

    private final int seed;
    private final double learningRate;
    private final int nEpoch;

    /**
     * =================
     * Class constructor
     * =================
     */
    public Training(String userId, List<List<Float>> features, List<List<Float>> labels, JsonObject hyperparameters) {
        this.userId = userId;
        this.features = features;
        this.labels = labels;

        this.seed = hyperparameters.get("seed").getAsInt();
        this.learningRate = hyperparameters.get("learningRate").getAsDouble();
        this.nEpoch = hyperparameters.get("nEpoch").getAsInt();
    }

    public static void main(String[] args) {
        final INDArray input = Nd4j.create(new double[]{28.66, 26.78, 27.36}, 1, 3, 1); // 26.57
        System.out.println(input);
    }

    /**
     * Read and convert dataset from
     * database into training and label sets
     */
    public DataSet dataReading() throws IOException, InterruptedException {

        INDArray featuresArray = Nd4j.zeros(features.size(), 3, 1);
        INDArray labelsArray = Nd4j.zeros(labels.size(), 1);

        for (int i = 0; i < features.size(); i++) {
            featuresArray.putScalar(new int[]{i, 0, 0}, features.get(i).get(0));
            featuresArray.putScalar(new int[]{i, 1, 0}, features.get(i).get(1));
            featuresArray.putScalar(new int[]{i, 2, 0}, features.get(i).get(2));

            labelsArray.putScalar(new int[]{i, 0}, labels.get(i).get(0));
        }

        return new DataSet(featuresArray, labelsArray);

    }

    /**
     * Configuration of the LSTM neural network
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

        SplitTestAndTrain splitTestAndTrain = dataSet.splitTestAndTrain(0.8);
        DataSet trainSet = splitTestAndTrain.getTrain();
        DataSet testSet = splitTestAndTrain.getTest();

        // 2. Configuration of the network
        MultiLayerNetwork model = networkConfig();
        model.init();
        model.setListeners(new ScoreIterationListener(5));

        // 3. Training
        for (int i = 0; i < nEpoch; i++) {
            model.fit(trainSet);
        }

        // 4. Save model
        model.save(new File("src/main/resources/users/" + userId + "/model.bin"));

        DataSetIterator trainIterator = new ViewIterator(trainSet, 1);
        DataSetIterator testIterator = new ViewIterator(testSet, 1);

        RegressionEvaluation trainEval = model.evaluateRegression(trainIterator);
        RegressionEvaluation testEval = model.evaluateRegression(testIterator);

        System.out.println(trainEval.stats());
        System.out.println(testEval.stats());

//     final INDArray input = Nd4j.create(new double[]{28.66, 26.78, 27.36}, 1, 3, 1); // 26.57
        final INDArray input = Nd4j.create(new double[]{26.71, 27.87, 28.28}, 1, 3, 1); // 28.46
        INDArray result = model.output(input);
        System.out.println(result.getDouble(0));

        return testEval.stats();

    }

}

package com.example.backend.dataProcessing;

import com.google.gson.JsonObject;
import org.deeplearning4j.earlystopping.EarlyStoppingConfiguration;
import org.deeplearning4j.earlystopping.EarlyStoppingResult;
import org.deeplearning4j.earlystopping.saver.LocalFileModelSaver;
import org.deeplearning4j.earlystopping.scorecalc.DataSetLossCalculator;
import org.deeplearning4j.earlystopping.termination.MaxEpochsTerminationCondition;
import org.deeplearning4j.earlystopping.termination.MaxTimeIterationTerminationCondition;
import org.deeplearning4j.earlystopping.trainer.EarlyStoppingTrainer;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.regression.RegressionEvaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.ViewIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private final int windowSize;
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
        this.windowSize = hyperparameters.get("window").getAsInt();
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
    public DataSet dataSetPreparation() throws IOException, InterruptedException {

        INDArray featuresArray = Nd4j.zeros(features.size(), windowSize, 1);
        INDArray labelsArray = Nd4j.zeros(labels.size(), 1);

        for (int i = 0; i < features.size(); i++) {

            for (int j = 0; j < windowSize; j++) {
                featuresArray.putScalar(new int[]{i, j, 0}, features.get(i).get(j));
            }

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
    public EarlyStoppingTrainer trainer(DataSetIterator iterator) {

        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .updater(new Adam(learningRate))
                .weightInit(WeightInit.XAVIER)
                .activation(Activation.LEAKYRELU)
                .list()
                .layer(new LSTM.Builder()
                        .nIn(windowSize)
                        .nOut(windowSize * 3)
                        .build())
                .layer(new DenseLayer.Builder()
                        .nIn(windowSize * 3)
                        .nOut(windowSize * 2)
                        .build())
                .layer(new DenseLayer.Builder()
                        .nIn(windowSize * 2)
                        .nOut(windowSize * 2)
                        .build())
                .layer(new OutputLayer.Builder()
                        .nIn(windowSize * 2)
                        .nOut(1)
                        .activation(Activation.IDENTITY)
                        .lossFunction(LossFunctions.LossFunction.MEAN_ABSOLUTE_ERROR)
                        .build())
                .build();

        EarlyStoppingConfiguration<MultiLayerNetwork> esConfig = new EarlyStoppingConfiguration.Builder<MultiLayerNetwork> ()
                .epochTerminationConditions(new MaxEpochsTerminationCondition(nEpoch))
                .iterationTerminationConditions(new MaxTimeIterationTerminationCondition(20, TimeUnit.MINUTES))
                .scoreCalculator(new DataSetLossCalculator(iterator, true))
                .evaluateEveryNEpochs(1)
                .modelSaver(new LocalFileModelSaver("src/main/resources/users/" + userId + "/"))
                .build();

        return new EarlyStoppingTrainer(esConfig, config, iterator);

    }

    /**
     * Where the training process takes place.
     * After being trained, the modal is saved to the user's folder accordingly
     */
    public String trainingProcess() throws IOException, InterruptedException {

        // 1. Obtain the dataset from database
        DataSet dataSet = dataSetPreparation();

//        System.out.println(dataSet);

        SplitTestAndTrain splitTestAndTrain = dataSet.splitTestAndTrain(0.8);
        DataSet trainSet = splitTestAndTrain.getTrain();
        DataSet testSet = splitTestAndTrain.getTest();

        // 2. Configuration of the network
        DataSetIterator trainIterator = new ViewIterator(trainSet, 10);

        EarlyStoppingTrainer trainer = trainer(trainIterator);
        EarlyStoppingResult<MultiLayerNetwork> result = trainer.fit();

        //Print out the results:
        System.out.println("Termination reason: " + result.getTerminationReason());
        System.out.println("Termination details: " + result.getTerminationDetails());
        System.out.println("Total epochs: " + result.getTotalEpochs());
        System.out.println("Best epoch number: " + result.getBestModelEpoch());
        System.out.println("Score at best epoch: " + result.getBestModelScore());

        //Get the best model:
        MultiLayerNetwork model = result.getBestModel();
        model.init();
        model.setListeners(new ScoreIterationListener(5));

        final INDArray input = Nd4j.create(new double[]{26.6, 26.69, 26.53, 26.64, 26.39}, 1, 5, 1); // 25.83

        final INDArray prediction = model.output(input);
        System.out.println(prediction.getDouble(0));
//        // 3. Training
//        for (int i = 0; i < nEpoch; i++) {
//            model.fit(trainSet);
//            INDArray result = model.output(input, true);
//            System.out.println(result.getDouble(0));
//        }

        // 4. Save model
//        model.save(new File("src/main/resources/users/" + userId + "/model.bin"));


        DataSetIterator testIterator = new ViewIterator(testSet, 10);

        RegressionEvaluation trainEval = model.evaluateRegression(trainIterator);
        RegressionEvaluation testEval = model.evaluateRegression(testIterator);

        System.out.println(trainEval.stats());
        System.out.println(testEval.stats());

        return testEval.stats();
    }

}

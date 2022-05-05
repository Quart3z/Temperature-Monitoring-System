package com.example.backend.dataProcessing;

import org.deeplearning4j.nn.api.NeuralNetwork;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Predicting {

    /**
     * ===============
     * Class attributes
     * ===============
     */
    private final String userId;
    private final List<List<Float>> features;

    public Predicting(String userId, List<List<Float>> features) {
        this.userId = userId;
        this.features = features;
    }

    public List<INDArray> featureSetLoading() throws IOException, InterruptedException {

        List<INDArray> featureList = new ArrayList<>();

        for (int i = 0; i < features.size(); i++) {

            INDArray temps = Nd4j.zeros(1, 5, 1);

            for (int j = 0; j < 5; j++) {
                temps.putScalar(new int[]{i, j, 0}, features.get(i).get(j));

            }

            featureList.add(temps);

        }

        return featureList;

    }

    public List<Double> predictions() throws IOException, InterruptedException {
        List<INDArray> sets = featureSetLoading();

        MultiLayerNetwork model = MultiLayerNetwork.load(new File("src/main/resources/users/" + userId + "/bestModel.bin"), true);

        List<Double> results = new ArrayList<>();

        for(INDArray set : sets){
            INDArray result = model.output(set);

            results.add(result.getDouble(0));

        }

        return results;

    }

}

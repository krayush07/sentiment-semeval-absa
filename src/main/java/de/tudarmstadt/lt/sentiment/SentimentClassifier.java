package de.tudarmstadt.lt.sentiment;

import de.bwaldvogel.liblinear.*;
import org.apache.commons.cli.BasicParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


class GenerateTrainFeature {
    static List<LinkedHashMap<Integer, Double>> listOfMaps = new ArrayList<LinkedHashMap<Integer, Double>>();

    GenerateTrainFeature() {
        for (int i = 0; i < 20; i++) {
            listOfMaps.add(i, new LinkedHashMap<Integer, Double>());
        }
    }

    public void setHashMap(int start, List<LinkedHashMap<Integer, Double>> hMap) {
        //System.out.println(hMap.size() + " ** " + hMap.get(0).size() + " ## "+start);
        System.out.println("start: " + start + ": ");

        for (int i = 0; i < hMap.size(); i++) {
            //System.out.println();
            for (Map.Entry<Integer, Double> entry : hMap.get(i).entrySet()) {
                //System.out.print(entry.getKey() + ": " + entry.getValue() + ";   ");
                listOfMaps.get(i).put(start + entry.getKey(), entry.getValue());
                // do stuff
            }
        }
    }

    public List<LinkedHashMap<Integer, Double>> getList() {
        //System.out.println(trainingFeature.size());
        return listOfMaps;
    }
}

class GenerateTestFeature {
    static List<LinkedHashMap<Integer, Double>> listOfMaps = new ArrayList<LinkedHashMap<Integer, Double>>();

    GenerateTestFeature() {
        for (int i = 0; i < 3; i++) {
            listOfMaps.add(i, new LinkedHashMap<Integer, Double>());
        }
    }

    public void setHashMap(int start, List<LinkedHashMap<Integer, Double>> hMap) {
        //System.out.println(hMap.size() + " ** " + hMap.get(0).size() + " ## "+start);
        System.out.println("start: " + start + ": ");

        for (int i = 0; i < hMap.size(); i++) {
            //System.out.println();
            for (Map.Entry<Integer, Double> entry : hMap.get(i).entrySet()) {
                //System.out.print(entry.getKey() + ": " + entry.getValue() + ";   ");
                listOfMaps.get(i).put(start + entry.getKey(), entry.getValue());
                // do stuff
            }
        }
    }

    public List<LinkedHashMap<Integer, Double>> getList() {
        //System.out.println(trainingFeature.size());
        return listOfMaps;
    }
}

public class SentimentClassifier {
    //static int start=0;
    public static void main(String[] args) throws IOException {

        //TRAINING SET
        GenerateTrainFeature trainingObject = new GenerateTrainFeature();
        List<LinkedHashMap<Integer, Double>> trainingFeature = new ArrayList<LinkedHashMap<Integer, Double>>();


        //TESTING SET
        GenerateTestFeature testObject = new GenerateTestFeature();
        List<LinkedHashMap<Integer, Double>> testFeature = new ArrayList<LinkedHashMap<Integer, Double>>();



        //POS FEATURE
        int start = 0;
        //POS obj1 = new POS();
        //trainingObject.setHashMap(start, obj1.getTrainingList(), obj1.getFeatureCount());
        //trainingFeature = trainingObject.getList();
        //System.out.println(trainingFeature.get(10).size());


        //NRC HASHTAG FEATURE
        //start += obj1.getFeatureCount();
        NRCHashtag obj2 = new NRCHashtag();
        trainingObject.setHashMap(start, obj2.getTrainingList());
        trainingFeature = trainingObject.getList();

        testObject.setHashMap(start, obj2.getTestList());
        testFeature = testObject.getList();


        /*System.out.println("*************************************");
        for(int i=0; i<trainingFeature.size(); i++)    //Print the feature values
        {
            //System.out.println(trainingFeature.get(i).size());
            System.out.println(trainingFeature.get(i));
        }*/


        //N GRAM FEATURE
        start += obj2.getFeatureCount();
        Ngram obj3 = new Ngram();
        trainingObject.setHashMap(start, obj3.getTrainingList());
        trainingFeature = trainingObject.getList();

        testObject.setHashMap(start, obj3.getTestList());
        testFeature = testObject.getList();
        //System.out.println(trainingFeature.get(10).size());


        //CATEGORY FEATURE
        /*start = trainingFeature.get(0).size();
        Category obj4 = new Category();
        trainingObject.setHashMap(start, obj4.getTrainingList());
        trainingFeature = trainingObject.getList();
        System.out.println(trainingFeature.get(10).size());*/


        System.out.println("TRAINING *************************************");
        for (int i = 0; i < trainingFeature.size(); i++)    //Print the feature values
        {
            //System.out.println(trainingFeature.get(i).size());
            System.out.println(trainingFeature.get(i));
        }

        System.out.println("TEST *************************************");
        for (int i = 0; i < testFeature.size(); i++)    //Print the feature values
        {
            //System.out.println(trainingFeature.get(i).size());
            System.out.println(testFeature.get(i));
        }


        System.out.println("Hello sentiment!");

        // Create features

        // X = HashMap<List<(int, double)>> ...

        Problem problem = new Problem();

        // Save X to problem
        double a[] = new double[obj2.getTrainingList().size()];
        File file = new File("/Users/biem/sentiment/dataset/trainingLabels.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String read = null;
        int count = 0;
        while ((read = reader.readLine()) != null) {
            //System.out.println(read);
            a[count++] = Double.parseDouble(read.toString());
        }

        //Feature[][] f = new Feature[][]{ {}, {}, {}, {}, {}, {} };

        trainingFeature = trainingObject.getList();
        Feature[][] trainFeatureVector = new Feature[trainingFeature.size()][start + obj3.getFeatureCount()];

        System.out.println(trainingFeature.size());
        System.out.println(start + obj3.getFeatureCount());

        for (int i = 0; i < trainingFeature.size(); i++) {

            System.out.println(trainingFeature.get(i));
            for (int j = 0; j < start + obj3.getFeatureCount(); j++) {
                //System.out.print(trainingFeature.get(i).get(j + 1)+" ");
                if (trainingFeature.get(i).containsValue(j + 1)) {
                    trainFeatureVector[i][j] = new FeatureNode(j + 1, trainingFeature.get(i).get(j + 1));
                } else {
                    trainFeatureVector[i][j] = new FeatureNode(j + 1, 0.0);
                }
            }
            //System.out.println();
        }

        problem.l = trainingFeature.size(); // number of training examples
        problem.n = start + obj3.getFeatureCount(); // number of features
        problem.x = trainFeatureVector; // feature nodes
        problem.y = a; // target values ----

        /**
         This is similar to original C implementation:
         struct problem
         {
         int l, n;
         int *y;
         struct feature_node **x;
         double bias;
         };
         * */

        BasicParser bp = new BasicParser();

        SolverType solver = SolverType.L2R_LR_DUAL; // -s 7
        double C = 1.0;    // cost of constraints violation
        double eps = 0.001; // stopping criteria

        Parameter parameter = new Parameter(solver, C, eps);
        Model model = Linear.train(problem, parameter);
        File modelFile = new File("model");
        model.save(modelFile);

        // load model or use it directly
        model = Model.load(modelFile);

        Feature[] instance = {new FeatureNode(1, 4), new FeatureNode(2, 2)};

        /*for(int i=0; i<trainingFeature.get(23).size(); i++)
        {
            instance[i] = new FeatureNode(i+1, trainingFeature.get(23).get(i+1));
        }*/

        double prediction = Linear.predict(model, instance);

        System.out.println(prediction);

    }
}
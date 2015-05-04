package satb.behavior;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.trees.J48;
import weka.core.FastVector;
import weka.core.Instances;

public class WekaTest {
    public static BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;
        
        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }
        
        return inputReader;
    }
    
    public static Evaluation simpleClassify(Classifier model, Instances trainingSet, Instances testingSet) throws Exception {
        Evaluation validation = new Evaluation(trainingSet);
        
        model.buildClassifier(trainingSet);
        validation.evaluateModel(model, testingSet);
        
        return validation;
    }
    
    public static double calculateAccuracy(FastVector predictions) {
        double correct = 0;
        
        for (int i = 0; i < predictions.size(); i++) {
            NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
            if (np.predicted() == np.actual()) {
                correct++;
            }
        }
        
        return 100 * correct / predictions.size();
    }
    
    public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
        Instances[][] split = new Instances[2][numberOfFolds];
        
        for (int i = 0; i < numberOfFolds; i++) {
            split[0][i] = data.trainCV(numberOfFolds, i);
            split[1][i] = data.testCV(numberOfFolds, i);
        }
        
        return split;
    }
 
    /**
     * Para versão antiga do Weka
     * @param file
     * @throws Exception 
     */
    public void go(String file) throws Exception {
        // I've commented the code as best I can, at the moment.
        // Comments are denoted by "//" at the beginning of the line.
        
        BufferedReader datafile = readDataFile(file);
        
        Instances data = new Instances(datafile);
        data.setClassIndex(data.numAttributes() - 1);
        
        // Choose a type of validation split
        Instances[][] split = crossValidationSplit(data, 10);
        
        // Separate split into training and testing arrays
        Instances[] trainingSplits = split[0];
        Instances[] testingSplits  = split[1];
        
        // Choose a set of classifiers
        Classifier[] models = {     new weka.classifiers.meta.END(),
                                    new weka.classifiers.functions.SMO(),
                                    new weka.classifiers.meta.ClassificationViaRegression(),
                                    new weka.classifiers.trees.RandomForest(),
                                    new J48(),
                                    };
        
        // Run for each classifier model
        for(int j = 0; j < models.length; j++) {

            // Collect every group of predictions for current model in a FastVector
            FastVector predictions = new FastVector();
            
            Double correct = 0.0;
            // For each training-testing split pair, train and test the classifier
            for(int i = 0; i < trainingSplits.length; i++) {
                Evaluation validation = simpleClassify(models[j], trainingSplits[i], testingSplits[i]);
                predictions.appendElements(validation.predictions());
                
                correct += validation.correct();
                
                //System.out.println(models[j].getClass().getSimpleName() + ": " + String.format("%.2f%%", validation.correct()) + "\n=====================");

                // Uncomment to see the summary for each training-testing pair.
                //System.out.println(models[j].toString());
            }
            
            // Calculate overall accuracy of current classifier on all splits
            //double accuracy = calculateAccuracy(predictions);
            
            // Print current classifier's name and accuracy in a complicated, but nice-looking way.
            //System.out.println(models[j].getClass().getSimpleName() + ": " + String.format("%.2f%%", accuracy) + "\n=====================");
            System.out.println(models[j].getClass().getSimpleName() + ": instances "+correct+"  "+ String.format("%.2f%%", 100 * correct / predictions.size()) + "\n=====================");
            
        }
        
    }
    
    
    public Double go2(String file) throws Exception {
        // I've commented the code as best I can, at the moment.
        // Comments are denoted by "//" at the beginning of the line.
        
        BufferedReader datafile = readDataFile(file);
        
        Instances data = new Instances(datafile);
        data.setClassIndex(data.numAttributes() - 1);
        
        
        // Choose a set of classifiers
        /*Classifier[] models = {     new weka.classifiers.meta.END(),
                                    new weka.classifiers.functions.SMO(),
                                    new weka.classifiers.meta.ClassificationViaRegression(),
                                    new weka.classifiers.trees.RandomForest(),
                                    new J48(),
                                    };*/
        
        Classifier[] models = { new weka.classifiers.meta.END() };
        
        Double maxCorrect = 0.0;
        // Run for each classifier model
        
        Long tempo = System.currentTimeMillis();
        for(int j = 0; j < models.length; j++) {
            Evaluation eval = new Evaluation(data);
            Random rand = new Random(1);  // using seed = 1
            int folds = 10;
            eval.crossValidateModel(models[j], data, folds, rand);
            
            if(eval.correct() > maxCorrect) {                
                maxCorrect = 100 * eval.correct() / ( eval.correct()+eval.incorrect() );
            }
            System.out.println("--------------------"+models[j].getClass().getSimpleName()+"---------------------------------");
            System.out.println(eval.toSummaryString());
            System.out.println(eval.toClassDetailsString());
            System.out.println(eval.toMatrixString());
            
            System.out.println("\nTempo de Execução (segundos) = "+((System.currentTimeMillis()-tempo)/1000.0) );
            
        }
        System.out.println("=======================================================================================");
        
        return maxCorrect;
    }
}
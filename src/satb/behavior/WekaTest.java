package satb.behavior;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;

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
        Classifier[] models = {     //new weka.classifiers.meta.END(),
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
    
    
    /*public Double go2(String file) throws Exception {
        // I've commented the code as best I can, at the moment.
        // Comments are denoted by "//" at the beginning of the line.
        
        BufferedReader datafile = readDataFile(file);
        
        Instances data = new Instances(datafile);
        data.setClassIndex(data.numAttributes() - 1);
        
        
        // Choose a set of classifiers
        Classifier[] models = {     new weka.classifiers.meta.END(),
                                    new weka.classifiers.functions.SMO(),
                                    new weka.classifiers.meta.ClassificationViaRegression(),
                                    new weka.classifiers.trees.RandomForest(),
                                    new J48(),
                                    };
        
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
    }*/
    
    public Double go2(Instances data, String message) throws Exception {
        
        //data.setClassIndex(data.numAttributes() - 1);
        
        Instances dataFilter;
        
        Resample filter = new Resample();
        filter.setBiasToUniformClass(1.0);
        filter.setInputFormat(data);
        filter.setSampleSizePercent(100);
        filter.setNoReplacement(false);
        data = Filter.useFilter(data, filter);
        
                
        /*AttributeSelection as = new AttributeSelection();        
        GreedyStepwise asSearch = new GreedyStepwise();
        asSearch.setOptions(new String[]{"-C", "-B", "-R"});
        as.setSearch(asSearch);        
        CfsSubsetEval asEval = new CfsSubsetEval();
        asEval.setOptions(new String[]{"-M", "-L"});
        as.setEvaluator(asEval);
        as.SelectAttributes(data);
        dataFilter = as.reduceDimensionality(data);*/
        
        // Choose a set of classifiers
        /*Classifier[] models = {     new weka.classifiers.meta.END(), new weka.classifiers.functions.SMO(), new weka.classifiers.meta.ClassificationViaRegression(),
                                    new weka.classifiers.trees.RandomForest(), new J48(), new weka.classifiers.functions.MultilayerPerceptron() new weka.classifiers.lazy.KStar()                                                                        
                                    };*/                
        //Classifier classifier = AbstractClassifier.forName("weka.classifiers.trees.RandomForest", weka.core.Utils.splitOptions("-P 100 -I 300 -num-slots 3 -K 0 -M 1.0 -V 0.001 -S 1 -batch-size 300"));
        //classifier.setOptions(weka.core.Utils.splitOptions("-P 100 -I 300 -num-slots 3 -K 0 -M 1.0 -V 0.001 -S 1 -batch-size 300"));
        
        RandomForest classifier = new RandomForest();
        classifier.setBatchSize("400");
        classifier.setNumIterations(400);                
        classifier.setNumExecutionSlots(3);
                        
        Classifier[] models = { classifier };
        
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
            String printMessage = "--------------------"+models[j].getClass().getSimpleName()+"---------------------------------"+"\n"+
                    message +"\n"+
                    eval.toSummaryString()+"\n"+
                    eval.toClassDetailsString()+"\n"+
                    eval.toMatrixString()+"\n"+
                    "\nTempo de Execução (segundos) = "+((System.currentTimeMillis()-tempo)/1000.0)+"\n"+
                    "---------------------------------------------------------------------------------------------";                    
            printMessage(printMessage);
            
            /*printMessage = "\n ================ Reduce ============= \n";
            Evaluation eval2 = new Evaluation(dataFilter);            
            eval2.crossValidateModel(models[j], dataFilter, folds, rand);
            
            printMessage += "--------------------"+models[j].getClass().getSimpleName()+"---------------------------------"+"\n"+
                    message +"\n"+
                    eval2.toSummaryString()+"\n"+
                    eval2.toClassDetailsString()+"\n"+
                    eval2.toMatrixString()+"\n"+
                    "\nTempo de Execução (segundos) = "+((System.currentTimeMillis()-tempo)/1000.0)+"\n"+
                    "---------------------------------------------------------------------------------------------";                    
            printMessage(printMessage);*/
            
        }
        
        return maxCorrect;
    }
    
    public Evaluation go3(Instances data, String message, Instances dataPredict) throws Exception {
        
        Resample filter = new Resample();
        filter.setBiasToUniformClass(1.0);
        filter.setInputFormat(data);
        filter.setSampleSizePercent(100);
        filter.setNoReplacement(false);
        data = Filter.useFilter(data, filter);     
                
        /*AttributeSelection as = new AttributeSelection();        
        GreedyStepwise asSearch = new GreedyStepwise();
        asSearch.setOptions(new String[]{"-C", "-B", "-R"});
        as.setSearch(asSearch);        
        CfsSubsetEval asEval = new CfsSubsetEval();
        asEval.setOptions(new String[]{"-M", "-L"});
        as.setEvaluator(asEval);
        as.SelectAttributes(data);
        data = as.reduceDimensionality(data);*/
        
        RandomForest classifier = new RandomForest();
        classifier.setBatchSize("400");
        classifier.setNumIterations(400);                
        classifier.setNumExecutionSlots(3);
        
        Long tempo = System.currentTimeMillis();
        
        /*Evaluation eval = new Evaluation(data);
        Random rand = new Random(1);  // using seed = 1
        eval.crossValidateModel(classifier, data, 10, rand);
        
        String printMessage = "--------------------"+classifier.getClass().getSimpleName()+"---------------------------------"+"\n"+
            message +"\n"+
            eval.toSummaryString()+"\n"+
            eval.toClassDetailsString()+"\n"+
            eval.toMatrixString()+"\n"+
            "\nTempo de Execução (segundos) = "+((System.currentTimeMillis()-tempo)/1000.0)+"\n"+
            "---------------------------------------------------------------------------------------------";                    
        printMessage(printMessage);
        
       classifier = new RandomForest();
        classifier.setBatchSize("400");
        classifier.setNumIterations(400);                
        classifier.setNumExecutionSlots(3);*/
        
        InputMappedClassifier scheme = new InputMappedClassifier();
        scheme.setOptions(Utils.splitOptions("-I -trim -W weka.classifiers.trees.RandomForest -- -P 100 -I 400 -num-slots 3 -K 0 -M 1.0 -V 0.001 -S 1 -batch-size 400"));
        
        scheme.buildClassifier(data);
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(scheme, dataPredict);
        String printMessage = "--------------------"+scheme.getClass().getSimpleName()+"---------------------------------"+"\n"+
            message +"\n"+
            eval.toSummaryString()+"\n"+
            eval.toClassDetailsString()+"\n"+
            eval.toMatrixString()+"\n"+
            "\nTempo de Execução (segundos) = "+((System.currentTimeMillis()-tempo)/1000.0)+"\n"+
            "---------------------------------------------------------------------------------------------";                    
        printMessage(printMessage);
        
        ArrayList<Prediction> lpr = eval.predictions();        
        Double index;
        int i=0;
        
        Enumeration<Attribute> x = dataPredict.enumerateAttributes();        
        while (x.hasMoreElements()) {
            System.out.print(x.nextElement().name() +",");            
        }
        System.out.println("classe,predito");
        
        for(Prediction p : lpr) {
            
            System.out.print(dataPredict.get(i).toString() );            
            //index = p.actual();
            //System.out.print( "," + dataPredict.classAttribute().value( index.intValue() ) );
            index = p.predicted();
            System.out.println("," + dataPredict.classAttribute().value( index.intValue() ) );            
            
            i++;
        }
        
        
            
            
        /* for(int i=0; i<dataPredict.size(); i++) {
            
            // valor observado
            Double r = dataPredict.get(i).value(dataPredict.classAttribute());
            printMessage = dataPredict.classAttribute().value(r.intValue());
            if(printMessage.equals("NaoObservado"))
                continue;
            System.out.print(printMessage);
            
            // predito
            r = eval.evaluateModelOnce(classifier, dataPredict.get(i));
            printMessage = ";"+ dataPredict.classAttribute().value(r.intValue());            
            System.out.println(printMessage);
        }
        */

        return eval;
    }
    
    private synchronized void printMessage (String message) {
        System.out.println( message );
    }
}
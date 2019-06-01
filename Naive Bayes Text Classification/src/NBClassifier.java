/**
 * Course Number 612
 * Lab 04 Text Classification using a Naïve Bayes (Multinomial) Classifier
 * Sachin Mohan Sujir
 */

import java.util.*;

public class NBClassifier {
    HashMap<Integer, String> trainingDocs;
    int[] trainingLabels;
    int[] classCounts;                                         //number of docs per class
    String[] classStrings;                                     //concatenated string for a given class
    int[] classTokenCounts;                                    //total number of tokens per class
    HashMap<String, Double>[] condProb;
    HashSet<String> vocabulary;                               //entire vocabuary
    int numClasses = 2;
    Parser docParser;

    /**
     * Build a Naive Bayes classifier using a training document set
     * @param trainDataFolder
     * @param testDataFolder
     */
    
    //Task 1
    public NBClassifier(String trainDataFolder,String testDataFolder){
        preprocess(trainDataFolder,testDataFolder);
        trainingDocs = docParser.myDocsContentMap;
        trainingLabels = docParser.trainLabels;
        classCounts = new int[numClasses];
        classStrings = new String[numClasses];
        classTokenCounts = new int[numClasses];
        condProb = new HashMap[numClasses];
        vocabulary = new HashSet<String>();

        for (int i = 0; i < numClasses; i++) {
            classStrings[i] = "";
            condProb[i] = new HashMap<String, Double>();
        }
        for (int i = 0; i < trainingLabels.length; i++) {
            classCounts[trainingLabels[i]]++;
            classStrings[trainingLabels[i]] += (trainingDocs.get(i) + " ");
        }
        for (int i = 0; i < numClasses; i++) {
            String[] tokens = classStrings[i].split("\\p{Punct}|\\s");
            //String[] tokens = doc.split("[\" ()_,?:;%&-]+");
            classTokenCounts[i] = tokens.length;
            																		//collecting the counts
            for (String token : tokens) {
                vocabulary.add(token);
                if (condProb[i].containsKey(token)) {
                    double count = condProb[i].get(token);
                    condProb[i].put(token, count + 1);
                } else
                    condProb[i].put(token, 1.0);
            }

        }
        																		//computing the class conditional probability
        for (int i = 0; i < numClasses; i++) {
            Iterator<Map.Entry<String, Double>> iterator = condProb[i].entrySet().iterator();
            int vSize = vocabulary.size();
            while (iterator.hasNext()) {
                Map.Entry<String, Double> entry = iterator.next();
                String token = entry.getKey();
                Double count = entry.getValue();
                count = (count + 1) / (classTokenCounts[i] + vSize);
                condProb[i].put(token, count);
            }
        }
    }
    /**
     * Pre process both train and test data for future processing
     * @param trainDataFolder
     * @param testDataFolder
     */
    // Task 1
    public void preprocess(String trainDataFolder,String testDataFolder)
    {
        docParser =  new Parser(trainDataFolder, testDataFolder);
    }

    /**
     * Classifies String Docs
     * @param doc
     * returns label for which the class belongs to
     */
    // Task 2
    public int classify(String doc) {
        int label = 0;
        int vSize = vocabulary.size();
        double[] score = new double[numClasses];
        int i=0;
        while(i<numClasses){
        	
            score[i] = Math.log(classCounts[i] * 1.0 / trainingDocs.size());
            i++;
        } 
        String[] tokens = doc.split("\\p{Punct}|\\s");
        int j=0;
        while(j<numClasses) {
        
            for (String token : tokens) {
                if (!condProb[j].containsKey(token))
                	score[j] += Math.log(1.0 / (classTokenCounts[j] + vSize));
                   
                else
                	 score[j] += Math.log(condProb[j].get(token));
            }
            j++;
        }
        double maxScore = score[0];
        int k=0;
        while(k<score.length) {
        
            if (maxScore< score[k])
                label = k;
            k++;
        }

        return label;
    }
    /**
     * Classifies test docs
     * @param testDocs
     * @param trainingLabels
     */
    
    // Task 3
    public void classifyAll(HashMap<Integer, String> testDocs, int[] trainingLabels) {
        int tp = 0;
        int tn = 0;
        int fp = 0;
        int fn = 0;
        int correctlyClassified = 0;
        float precision;
        float recall;
        float fmeasure;
        float accuracy;
        float accuracy1;

        for (Map.Entry<Integer, String> testDoc : testDocs.entrySet()) {
            int result = classify(testDoc.getValue());
            if (trainingLabels[testDoc.getKey()] == 1 && result == trainingLabels[testDoc.getKey()] ) { //pos
                tp++;
            } else if (trainingLabels[testDoc.getKey()] == 0 && result == trainingLabels[testDoc.getKey()] ) {  // neg
                tn++;
            } else if (trainingLabels[testDoc.getKey()] == 0 && result != trainingLabels[testDoc.getKey()] ) {	// had to be neg but predicted pos
                fn++;
            } else if (trainingLabels[testDoc.getKey()] == 1 && result != trainingLabels[testDoc.getKey()] ) {	// had to be pos but predicted neg
                fp++;
            }
        }
        correctlyClassified=tp+tn;
        precision = (float) tp / (float) (tp + fp);
        recall = (float) tp / (float) (tp + fn);
        fmeasure = 2 * ((precision * recall) / (precision + recall));
        accuracy = (float) (tp + tn) / (float) (tp + tn + fp + fn);
        accuracy1=accuracy*100;


        System.out.println("Correctly classified " +correctlyClassified+" of "+testDocs.size());
        System.out.println("Accuracy is : " + accuracy+" "+accuracy1+"%");
        System.out.println("");
        System.out.println("Precision is : " + precision);
        System.out.println("Recall is : " + recall);
        System.out.println("Fmeasure is : " + fmeasure);

    }

   


    public static void main(String[] args) {
        NBClassifier nb = new NBClassifier("./data/train", "./data/test");
        
        nb.classifyAll(nb.docParser.myDocsTestContentMap, nb.docParser.trainLabels);

     }
}
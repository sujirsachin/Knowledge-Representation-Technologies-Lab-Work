/**
 * Course Number 612
 * Lab 04 Text Classification using a Naïve Bayes (Multinomial) Classifier
 * Sachin Mohan Sujir
 */
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;



public class Parser {
    public HashMap<Integer, String> myDocsContentMap;
    public HashMap<Integer, String> myDocsTestContentMap;
    public static HashMap<Integer, String> myDocsFileNameReferenceMap;
    public int[] trainLabels;

    public Parser(String trainPath,String testPath) {


        /**
         * Create the list of documents after removing all the stopwords.
         */
        parseDocuments(trainPath,testPath);
    }


    /**
     *
     * @param trainFolderPath
     * @param testFolderPath
     */
    private void parseDocuments(String trainFolderPath ,String testFolderPath ) {
        File trainPos = new File(trainFolderPath+"/pos");
        File trainNeg = new File(trainFolderPath+"/neg");
        File testPos = new File(testFolderPath+"/pos");
        File testNeg = new File(testFolderPath+"/neg");
        /**
         * Using two maps as the listFiles() does not return a sorted file list hence storing references to each file.
         * and their respective positions
         */
        myDocsContentMap = new HashMap<Integer, String>();
        myDocsFileNameReferenceMap = new HashMap<Integer, String>();
        myDocsTestContentMap = new HashMap<Integer, String>();
        int index = 0;
        trainLabels = new int[2000];
        try {
            for (File file : trainPos.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
                    String content = new String(encoded, Constants.ENCODING);
                    myDocsContentMap.put(index, content.toLowerCase()); //case Folding
                    myDocsFileNameReferenceMap.put(index, file.getName());
                    trainLabels[index] = 1;
                    index++;
                }
            }
            for (File file : trainNeg.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
                    String content = new String(encoded, Constants.ENCODING);
                    myDocsContentMap.put(index, content.toLowerCase()); //case Folding
                    myDocsFileNameReferenceMap.put(index, file.getName());
                    trainLabels[index] = 0;
                    index++;
                }
            }
            for (File file : testPos.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
                    String content = new String(encoded, Constants.ENCODING);
                    myDocsTestContentMap.put(index, content.toLowerCase()); //case Folding
                    myDocsFileNameReferenceMap.put(index, file.getName());
                    trainLabels[index] = 1;
                    index++;
                }
            }
            for (File file : testNeg.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
                    String content = new String(encoded, Constants.ENCODING);
                    myDocsTestContentMap.put(index, content.toLowerCase()); //case Folding
                    myDocsFileNameReferenceMap.put(index, file.getName());
                    trainLabels[index] = 0;
                    index++;
                }
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

}



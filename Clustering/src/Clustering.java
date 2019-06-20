/**
 * Course Number 612
 * Lab 04 Document Clustering
 * Sachin Mohan Sujir
 */


import java.util.*;

public class Clustering {
    // Declare attributes

    ArrayList<String[]> tokenizedDocs;
    HashMap<Integer, double[]> vectorSpace;
    ArrayList<String> termList;
    ArrayList<ArrayList<Doc>> docLists;
    int numberOfClusters;


    /**
     * Constructor for attribute initialization
     * @param numC number of clusters
     */
    public Clustering(int numC) {
        numberOfClusters = numC;
    }

    /**
     * Load the documents to build the vector representations
     * @param docs
     */
    public void preprocess(String[] docs){
        tokenizedDocs = new ArrayList<String[]>();
        termList = new ArrayList<String>();
        docLists = new ArrayList<ArrayList<Doc>>();
        ArrayList<Doc> docList;

        for (int i = 0; i < docs.length; i++) {
            String[] tokens = docs[i].split("\\p{Punct}|\\s");
            tokenizedDocs.add(i, tokens);

            for (String token : tokens) {
                if (!termList.contains(token)) {
                    termList.add(token);					//Building doclists
                    docList = new ArrayList<Doc>();
                    Doc doc = new Doc(i, 1);
                    docList.add(doc);
                    docLists.add(docList);
                } else {
                    int index = termList.indexOf(token);
                    docList = docLists.get(index);
                    boolean match = false;
                    for (Doc d : docList) {
                        if (d.id == i) {
                            d.weights++;
                            match = true;
                            break;
                        }
                    }
                    if (!match) {
                        Doc d = new Doc(i, 1);
                        docList.add(d);
                    }
                }
            }
        }
        vectorSpace = new HashMap<Integer, double[]>();
        double[] weights;
        int i=0;
      
        while(i<docLists.size()) {
         
            docList = docLists.get(i);
            int j=0;
            while(j< docList.size())
            {
            	
            
            
                Doc d = docList.get(j);			//building vector space
                if (vectorSpace.containsKey(d.id)) {
                	weights = vectorSpace.get(d.id);
                    weights[i] = d.weights;
                    vectorSpace.put(d.id, weights);
                } else {
                	weights = new double[termList.size()];
                    weights[i] = d.weights;
                    vectorSpace.put(d.id, weights);
                    
                }
                j++;
            }
            i++;
        }
    }


    /**
     * Cluster the documents using k-means
     */
    public void cluster(){
            double[][] centroids = new double[numberOfClusters][];
            centroids[0] = vectorSpace.get(8);
            centroids[1] = vectorSpace.get(0);
            HashMap<Integer, double[]>[] clusters = new HashMap[numberOfClusters];
           
            double[][] old = null;
            while (!Arrays.deepEquals(old, centroids)) {
               
                // Store for convergence convergence test.
                old = centroids;
                clusters = getClusters(centroids);
                centroids = getCentroids(clusters);
            }
            print(clusters);
    }

    /**
     * Prints the clusters
     * @param clusters
     */
    public void print(HashMap<Integer, double[]>[] clusters) {
        
        String clusterString;
        int i=0;
        while(i<clusters.length)
        {
        
            clusterString = "Cluster:  " +i+ "\n";
            HashMap<Integer, double[]> cluster = clusters[i];
            for (Integer id : cluster.keySet()) {
                clusterString += id + " ";
            }
            System.out.println(clusterString);
            i++;
        }
    }



    /**
     * @param clusters
     * @return centroids
     */
    public double[][] getCentroids(HashMap<Integer, double[]>[] clusters) {
        double[][] centroids = new double[numberOfClusters][];
        int i=0;
        while(i<clusters.length)
        {
        
            HashMap<Integer, double[]> cluster = clusters[i];
            double[] mean = new double[termList.size()];
            for (Integer id : cluster.keySet()) {
                double[] currDocVector = cluster.get(id);
                int x=0;
                while(x<currDocVector.length)
                {
               
                    mean[x] += currDocVector[x];
                    x++;
                }
                int y=0;
                while(y<mean.length)
                {
                
                    mean[y] = mean[y] / cluster.size();
                    y++;
                }
            }
            centroids[i] = mean;
            i++;
        }

        return centroids;
    }
    /**
     * @param centroids
     * @return clusters
     */
    public HashMap<Integer, double[]>[] getClusters(double[][] centroids) {
        HashMap<Integer, double[]>[] clusters = new HashMap[numberOfClusters];
        int i=0;
        while(i<numberOfClusters)
        {
        	
        
        
            clusters[i] = new HashMap<Integer, double[]>();
            i++;
        }
        int j=0;
        while(j<vectorSpace.size())
        {
        	
        
      
            double[] currDocVector = vectorSpace.get(j);
            int currDocId = j;
            double[] scores = new double[numberOfClusters];
            int k=0;
            while(k<numberOfClusters)
            {
            
                scores[k] = cosineSimilarity(centroids[k], currDocVector);
                k++;
            }
            int clusterId = 0;
            double max = scores[clusterId];
            int n=0;
            while(n<scores.length)
            {
           
                if (scores[n] > max) {
                    max = scores[n];
                    clusterId = n;
                }
                n++;
            }
            
            clusters[clusterId].put(currDocId, currDocVector);
            j++;
        }
        
        return clusters;
    }
    /**
     * Calculate cosine similarity between two vectors
     * @param vector1
     * @param vector2
     * @return cosine similarity
     */
    private double cosineSimilarity(double[] vector1, double[] vector2) {
        double dotProduct = 0.0,magnitude1 = 0.0,magnitude2 = 0.0;
        double cosineSimilarity = 0.0;
        int i=0;
        while(i<vector1.length)
        {
        
            dotProduct += vector1[i] * vector2[i];
            magnitude1 += Math.pow(vector1[i], 2);
            magnitude2 += Math.pow(vector2[i], 2);
            i++;
        }
        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);
        if (magnitude1 != 0.0 | magnitude2 != 0.0) {
            cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
        }
        return cosineSimilarity;
    }
    /**
     * Main entry point of program.
     * @param args
     */
    public static void main(String[] args){
    	String[] docs = {"hot chocolate cocoa beans",
				 "cocoa ghana africa",
				 "beans harvest ghana",
				 "cocoa butter",
				 "butter truffles",
				 "sweet chocolate can",
				 "brazil sweet sugar can",
				 "suger can brazil",
				 "sweet cake icing",
				 "cake black forest"
				};

        Clustering c = new Clustering(2);
       c.preprocess(docs);
      
        c.cluster();
		
		
    }
}
/**
*
* Document class for the vector representation of a document
*/
class Doc{
   int id;
   double weights;

   public Doc(int id, double tw) {
       this.id = id;
       this.weights = tw;
   }

   public String toString() {
       String str = id + ": " + weights;
       return str;
   }
}

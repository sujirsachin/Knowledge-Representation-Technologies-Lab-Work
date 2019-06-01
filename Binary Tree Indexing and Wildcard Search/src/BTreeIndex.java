/**
 * Course Number 612
 * Lab 03 Binary Tree Indexing and Wildcard Search
 * Sachin Mohan Sujir
 */

import java.util.*;
import java.util.stream.Stream;

public class BTreeIndex {
    String[] myDocs;
    BinaryTree termList;
    BTNode root;
    ArrayList<ArrayList<Integer>> docLists;

    /**
     * Construct binary search tree to store the term dictionary
     *
     * @param docs List of input strings
     */
    public BTreeIndex(String[] docs) {
        myDocs = docs;
        ArrayList<Integer> docList = new ArrayList<Integer>();
        ArrayList<String> terms = new ArrayList<String>();
                docLists = new ArrayList<ArrayList<Integer>>();
               
        termList = new BinaryTree();								
        for (int i = 0; i < myDocs.length; i++) {
            String[] words = myDocs[i].split(" ");				//splitting into words
            for (String word : words) {
                if (!terms.contains(word)) {//a new term
                    terms.add(word);
                }
            }
        }

        Collections.sort(terms);								// sorting the terms of docs
        int start = 0;
        int end = terms.size() - 1;
        int mid = (start + end) / 2;
        BTNode r = new BTNode(terms.get(mid), docList);			// center element becomes the root node
        root = r;

        for (int i = 0; i < myDocs.length; i++) {
            String[] tokens = myDocs[i].split(" ");
            for (String token : tokens) {
                if (termList.search(r, token) == null) {		//a new term root
                    docList = new ArrayList<Integer>();
                    docList.add(new Integer(i));
                    docLists.add(docList);
                    termList.add(root, new BTNode(token, docList));		//adding term to the tree
                } else {
                	
                														//an existing term
                    BTNode indexNode = termList.search(r, token);
                    docList = indexNode.docLists;
                    if (!docList.contains(new Integer(i))) {
                        docList.add(new Integer(i));
                    }
                    indexNode.docLists = docList;
                }
            }

        }

    }

    /**
     * Single keyword search
     *
     * @param query the query string
     * @return doclists that contain the term
     */
    public ArrayList<Integer> search(String query) {
        BTNode node = termList.search(root, query);
        if (node == null)									// No term in tree
            return null;
        return node.docLists;
    }

    /**
     * conjunctive query search
     *
     * @param query the set of query terms
     * @return doclists that contain all the query terms
     */
    public ArrayList<Integer> search(String[] query) {
        ArrayList<Integer> result = search(query[0]);
        
        for(int termId=1;termId<query.length;termId++)
        {
        
            ArrayList<Integer> result1 = search(query[termId]);
            if (result != null) 
            	if(result1 != null) {
                result = merge(result, result1);
            }
            
        }
        return result;
    }


    /**
     * @param wildcard the wildcard query, e.g., class, classification can be found
     * @return a list of ids of documents that contain terms matching the wild card
     */
    public ArrayList<Integer> wildCardSearch(String wildcard) {
        ArrayList<BTNode> searchList = termList.wildCardSearch(root, wildcard);		// searching particular nodes based on wildcard
        ArrayList<Integer> docIds = new ArrayList<>();
        searchList.stream().forEach(btNode -> {
            docIds.addAll(btNode.docLists);											// adding docids of terms based on wildcard
        });
        return docIds;
    }


    private ArrayList<Integer> merge(ArrayList<Integer> l1, ArrayList<Integer> l2) {
        ArrayList<Integer> mergedList = new ArrayList<Integer>();
        int id1 = 0, id2 = 0;
        while (id1 < l1.size() && id2 < l2.size()) {
            if (l1.get(id1).intValue() == l2.get(id2).intValue()) {
                mergedList.add(l1.get(id1));
                id1++;
                id2++;
            } else if (l1.get(id1) < l2.get(id2))
                id1++;
            else
                id2++;
        }
        return mergedList;
    }


    /**
     * Test cases
     */
    public static void main(String[] args) {
        String[] docs = {"text warehousing over big data",
                "dimensional data warehouse over big data",
                "nlp before text mining",
                "nlp before text classification"
        };

        BTreeIndex bTree = new BTreeIndex(docs);

        /**
         *   For one term
         */
        System.out.println("\nTestCase 1 :Single Keyword :  text");
        ArrayList<Integer> result = bTree.search("text");
        if (!result.isEmpty()) {
            for (Integer i : result) {
                System.out.println("\nFound in : " + i);
            }
        } else
            System.out.println("No match!");


        /**
         *   For conjunctive terms
         */
        String[] query2 = {"before", "text"};
        System.out.println("\nTestCase 2 :  query terms for AND : before and text");
        ArrayList<Integer> result1 = bTree.search(query2);
        if (result1 != null && !result1.isEmpty()) {
            for (Integer i : result1) {
                System.out.println("\nFound in : " + i);
            }
        } else
            System.out.println("No match!");


        /**
         * For Wild card Query
         */

        String query3 = "wa";
        System.out.println("\nTestCase 3 : wildcard Query "+query3);
        ArrayList<Integer> result3 = bTree.wildCardSearch(query3);
        if (result3 != null && !result3.isEmpty()) {
            for (Integer i : result3) {
                System.out.println("\nFound in : " + i);
            }
        } else
            System.out.println("No match!");
        String query4 = "class";
        System.out.println("\nTestCase 4 : wildcard Query "+query4);
        ArrayList<Integer> result4 = bTree.wildCardSearch(query4);
        if (result4 != null && !result4.isEmpty()) {
            for (Integer i : result4) {
                System.out.println("\nFound in : " + i);
            }
        } else
            System.out.println("No match!");
    }
}
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
/*
 * Sachin Mohan Sujir
 * Inverted Index and Query Processing
 * Lab 01
 * Course Number 612
 */
    public class InvertedIndexLab {              
   //attributes
    	private static String fileName1;
   	 private static String fileName2;
 	private static String fileName3;
 	private static String fileName4;
 	private static String fileName5;
 	
   private String[] myDocs;    
   
   public static ArrayList<String> termList;    //dictionary
   public static ArrayList<ArrayList<Integer>> docLists;      
   public ArrayList<Integer> docList;                
   
   /**
    * Construct a InvertedIndex
    * @param docs List of input strings
    *
    */
     
   public InvertedIndexLab(String[] docs) {            
	     myDocs = docs;
	     termList = new ArrayList<String>();
	              
	     docLists = new ArrayList<ArrayList<Integer>>();
	     docList = new ArrayList<Integer>();
	     
	     for(int i=0;i<myDocs.length;i++) {
	      String[] words = myDocs[i].split(" ");
	      for(String word:words) {
	         if(!termList.contains(word)) {
	            termList.add(word);
	            
	            docList = new ArrayList<Integer>();
	            docList.add(new Integer(i+1));
	         
	            docLists.add(docList);
	          }
	         else {
	            int index =termList.indexOf(word);
	          
	            docList = docLists.get(index);
	          
	            if(!docList.contains(new Integer(i+1))) {
	               docList.add(new Integer(i+1));
	               docLists.set(index, docList);
	            }
	         }
	      }
	     }
	   }
   /**
    * @param key
    */

   public static int searchStopWord(String key) throws IOException {
	   List<String> list = Files.readAllLines(Paths.get("stopwords.txt"), StandardCharsets.UTF_8);
	   String[] stopWords = list.toArray(new String[list.size()]); 

      int lo=0;
      int hi = stopWords.length-1;
      
      while(lo<=hi) {
         int mid = lo +(hi-lo)/2;
         int result = key.compareTo(stopWords[mid]);
         if(result<0) hi = mid-1;
         else if(result > 0) lo = mid+1;
         else return mid;
      }
      return -1;
   }
   /**
    * @param String Query
    * @return stemmed and parsed version of query
    */

   public static String parseB(String Query) throws IOException {
	      String[] tokens = null;
	      ArrayList<String> pureTokens = new ArrayList<String>();
	      ArrayList<String> stemms = new ArrayList<String>();
	      String res = null;
	      
	      	String words = Query.toLowerCase();
	      
	      tokens = words.split("[\" '.,?!:;$%+()\\-\\*]+");
	      
	      //remove stop words
	      for(String token:tokens) {
	         if(searchStopWord(token) == -1) {
	            pureTokens.add(token);
	         }
	      }
	      
	      //stemming
	      Stemmer st = new Stemmer();
	      for(String token:pureTokens) {
	         st.add(token.toCharArray(), token.length());
	         st.stem();
	         stemms.add(st.toString());
	         res = String.join("", stemms);
	         st = new Stemmer();
	      }
	      return res;
	   }
   
   /**
    * @param String array Query
    * @return stemmed and parsed version of query
    */

   public static String parseB(String[] Words) throws IOException {
	      String[] tokens = null;
	      ArrayList<String> pureTokens = new ArrayList<String>();
	      ArrayList<String> stemms = new ArrayList<String>();
	      String res = null;
	      String str = String.join(",", Words);
	      
	      
	      tokens = str.split("[\" '.,?!:;$%+()\\-\\*]+");
	      
	      //remove stop words
	      for(String token:tokens) {
	         if(searchStopWord(token) == -1) {
	            pureTokens.add(token);
	         }
	      }
	      
	      //stemming
	      Stemmer st = new Stemmer();
	      for(String token:pureTokens) {
	         st.add(token.toCharArray(), token.length());
	         st.stem();
	         stemms.add(st.toString());
	         res = String.join(" ", stemms);
	         st = new Stemmer();
	      }
	      return res;
	   } 
   
   /**
    * @param ArrayList of Integers
    * @return print resultant document name
    */

   // Method to process result
   public static void Result(ArrayList<Integer> result) {
	   for(Integer i:result) {
            if(i.intValue()==1) {
            	System.out.println("\n"+fileName1);
            }
            if(i.intValue()==4) {
            	System.out.println("\n"+fileName4);
            }
            if(i.intValue()==3) {
            	System.out.println("\n"+fileName3);
            }
            if(i.intValue()==2) {
            	System.out.println("\n"+fileName2);
         
            }
            if(i.intValue()==5) {
            	System.out.println("\n"+fileName5);
            }
         }
         
   }
   /**
    * @param String Single word Query
    * @return ArrayList of Integers of merged docs
    */

   //Search a word from termlist
   public static ArrayList<Integer> search(String query) {
   
     int index = termList.indexOf(query);
     if(index >= 0) {
         return docLists.get(index);
     }
     else return null;
   }
   /**
    * @param String Multiple word Query
    * @return ArrayList of Integers of merged docs
    */
   //Method to Process OR Query
   
   public static ArrayList<Integer> processOr(String Query) throws IOException
   {
	   	   
	   String []words=Query.split(" ");
	   
	      String term1=parseB(words[0]);
		   String term2=parseB(words[1]);
	   
	   String []terms= {term1,term2};
	   
	   
	   ArrayList<Integer> terms1 = search(terms[0]);
       ArrayList<Integer> terms2 = search(terms[1]);
       ArrayList<Integer> merge=MergeOr(terms1,terms2);
       return merge;
   }
   
   /**
    * @param1 Array List of Integer of term1
    * @param2 Array List of Integer of term2
    * @return ArrayList of Integers of merged docs
    */
 //Method to Merge OR Query
   public static ArrayList<Integer> MergeOr(ArrayList<Integer>terms1,ArrayList<Integer>terms2 ) {
	  int i=0; int j=0;
	  ArrayList<Integer> merged=new ArrayList<Integer>();
	  while(i<terms1.size()&&j<terms2.size())
	  {
		  if(terms1.get(i).intValue()>terms2.get(j).intValue()) {
			  merged.add(terms2.get(j));
			  j++;
			  
		  }
		  if(terms1.get(i).intValue()<terms2.get(j).intValue())
		  {
			  merged.add(terms1.get(i));
			  i++;
			  
		  }
		  if(terms1.get(i).intValue()==terms2.get(j).intValue()) {
			  merged.add(terms1.get(i));
			  
			  i++;
			  j++;
			  
		  }
			  
	  }
	  
          while (i < terms1.size()) {
              merged.add(terms1.get(i));
              i++;
        	  }
          
      
      
          while (j <terms2.size()) {
        	  
              merged.add(terms2.get(j));
              j++;
        	  }
          
      return merged;

	   
   }
   
   /**
    * @param String Multiple word Query
    * @return ArrayList of Integers of merged docs
    */
 //Method to Process AND Query
   public static ArrayList<Integer> processAnd(String Query) throws IOException
   {
	   	   
	   String []words=Query.split(" ");
	   
	      String term1=parseB(words[0]);
		   String term2=parseB(words[1]);
	   
	   String []terms= {term1,term2};
	   
	   
	   ArrayList<Integer> terms1 = search(terms[0]);
       ArrayList<Integer> terms2 = search(terms[1]);
       ArrayList<Integer> merge=MergeAnd(terms1,terms2);
       return merge;
   }
   
   /**
    * @param1 Array List of Integer of term1
    * @param2 Array List of Integer of term2
    * @return ArrayList of Integers of merged docs
    */
 //Method to Merge AND Query
   public static ArrayList<Integer> MergeAnd(ArrayList<Integer>terms1,ArrayList<Integer>terms2 ) {
	  int i=0; int j=0;
	  ArrayList<Integer> merged=new ArrayList<Integer>();
	  while(i<terms1.size()&&j<terms2.size())
	  {
		  if(terms1.get(i).intValue()>terms2.get(j).intValue()) {
			  j++;
			  
		  }
		  if(terms1.get(i).intValue()<terms2.get(j).intValue())
		  {
			  i++;
			  
		  }
		  if(terms1.get(i).intValue()==terms2.get(j).intValue()) {
			  merged.add(terms1.get(i));
			  
			  i++;
			  j++;
			  
		  }
			  
	  }
	return merged;
	   }
   /**
    * Creating string representation of  Inverted index
    */

   public String toString() {
      String outputString = new String();
      for(int i=0;i<termList.size();i++) {
         outputString += String.format("%-15s", termList.get(i));
         //int[] docList = docLists.get(i);
         ArrayList<Integer> docList = docLists.get(i);
         //for(int j=0;j<docList.length;j++) {
         for(int j=0;j<docList.size();j++) {
            //outputString += docList[j] + "\t";
            outputString += docList.get(j) + "\t";
         }
         outputString += "\n";
      }
      return outputString;
   }

   /**
    * @param String Multiple word Query
    * @return ArrayList of Integers of merged docs
    */
 //Method to Process MultiAND Query
   public static ArrayList<Integer> processMultiAnd(String Query) throws IOException
   {
	  
	   String []words=Query.split(" ");
	   
	   String term1=parseB(words[0]);
	   String term2=parseB(words[1]);
	   String term3=parseB(words[2]);
	 ;
   
	  String[] terms0= {term1,term2,term3};
	 
	   int size1;
	   int size2;
	   int size3;
	   
	   ArrayList<Integer> terms1 = search(terms0[0]);
    ArrayList<Integer> terms2 = search(terms0[1]);
    ArrayList<Integer> terms3 = search(terms0[2]);
    
    size1=terms1.size();
    size2=terms2.size();
    size3=terms3.size();
    
    ArrayList<Integer> merge= new ArrayList<Integer>();
    if(Math.min(size1,size2)==size1 &(Math.min(size1,size3)==size1))
    {
    	
    		if(Math.min(size2,size3)==size2)
    		{
    			ArrayList<Integer> merge1=MergeAnd(terms1,terms2);
    		    merge=MergeAnd(merge1,terms3);
    		  
    		}
    		else if(Math.min(size2,size3)==size3)
    		{
    			ArrayList<Integer> merge1=MergeAnd(terms1,terms3);
    		    merge=MergeAnd(merge1,terms2);
    		    
    		
    	}
    }
    if(Math.min(size2,size1)==size2)
    {
    	if(Math.min(size2,size3)==size2)
    	{
    		if(Math.min(size1,size3)==size1)
    		{
    			ArrayList<Integer> merge1=MergeAnd(terms2,terms1);
    		    merge=MergeAnd(merge1,terms3);
    		
    		}
    		 if (Math.min(size1,size3)==size3)
    		{
    			ArrayList<Integer> merge1=MergeAnd(terms2,terms3);
    		    merge=MergeAnd(merge1,terms1);
    		    
    		}
    	}
    }
    if(Math.min(size3,size1)==size3)
    {
    	if(Math.min(size2,size3)==size3)
    	{
    		if(Math.min(size1,size2)==size1)
    		{
    			ArrayList<Integer> merge1=MergeAnd(terms3,terms1);
    		    merge=MergeAnd(merge1,terms2);
    		    System.out.println("here");
    		}
    		else if(Math.min(size1,size2)==size2)
    		{
    			ArrayList<Integer> merge1=MergeAnd(terms3,terms2);
    		    merge=MergeAnd(merge1,terms1);
    		    
    		    
    		}
    	}
    }
    if(size1==size3)
    		{
    	if(size1==size2)
    	{
    		ArrayList<Integer> merge1=MergeAnd(terms3,terms2);
		    merge=MergeAnd(merge1,terms1);
		    
    	}
    		}
      return merge; 
   }
   
   
       public static void main(String[] args) throws IOException {

     	Scanner scanner1 = new Scanner( new File("/home/sujirsachin/eclipse-workspace/Lab01/Lab1_Data/1.txt") );
     	
     	   String doc1 = scanner1.useDelimiter("\\A").next();
     	   
     	   Scanner scanner2 = new Scanner( new File("/home/sujirsachin/eclipse-workspace/Lab01/Lab1_Data/2.txt") );
     	   
     	   String doc2 = scanner2.useDelimiter("\\A").next();
     	   
     	   Scanner scanner3 = new Scanner( new File("/home/sujirsachin/eclipse-workspace/Lab01/Lab1_Data/3.txt") );
     	   String doc3 = scanner3.useDelimiter("\\A").next();
     	   Scanner scanner4 = new Scanner( new File("/home/sujirsachin/eclipse-workspace/Lab01/Lab1_Data/4.txt") );
     	   String doc4 = scanner4.useDelimiter("\\A").next();
     	   Scanner scanner5 = new Scanner( new File("/home/sujirsachin/eclipse-workspace/Lab01/Lab1_Data/5.txt") );
     	   String doc5 = scanner5.useDelimiter("\\A").next();
       ParserB p = new ParserB();
        
         
         //Stemming
         Stemmer st = new Stemmer();

         
            File file1= new File("1.txt");
            File file2= new File("2.txt");
            File file3= new File("3.txt");
            File file4= new File("4.txt");
            File file5= new File("5.txt");
            fileName1=file1.getName().toString();
            fileName2=file2.getName().toString();
            fileName3=file3.getName().toString();
            fileName4=file4.getName().toString();
            fileName5=file5.getName().toString();
            ArrayList<String> stemmed1 = p.parseB(file1);
            ArrayList<String> stemmed2 = p.parseB(file2);
            ArrayList<String> stemmed3 = p.parseB(file3);
            ArrayList<String> stemmed4 = p.parseB(file4);
            ArrayList<String> stemmed5 = p.parseB(file5);
            String File1 = String.join(" ", stemmed1);
            String File2 = String.join(" ", stemmed2);
            String File3 = String.join(" ", stemmed3);
            String File4 = String.join(" ", stemmed4);
            String File5 = String.join(" ", stemmed5);

         
         String[] docs = new String[]{File1,File2,File3,File4,File5};
         
         InvertedIndexLab im = new InvertedIndexLab(docs);     //LBE02

         System.out.println(im);

      
            
       	 // Single key word search
            	  String Query1="Learning";
            	  String Query2="Entertaining";
            System.out.println("Single Query Test Case");
            System.out.println("Query: " + Query1);
            System.out.println("Query: " + Query2);
            String parse1=parseB(Query1).toString();
            String parse2=parseB(Query2).toString();
            ArrayList<Integer> result1 = im.search(parse1);
            ArrayList<Integer> result2 = im.search(parse2);
            System.out.println("Test Case for Single Key word");
            System.out.println("The Documents Containing the word: " +Query1+" are: ");
            Result(result1);
            System.out.println("The Documents Containing the word: " +Query2+" are: ");
            Result(result2);
            
            
            // Two Keyword Search with AND
            String Query3="Product Year";
            String Query4="William Time";
            
            ArrayList<Integer> result3 =  processAnd(Query3);
            ArrayList<Integer> result4 =  processAnd(Query4);
       	
       	System.out.println("Test Case for AND Query");
            System.out.println("The Documents Containing the words(AND): " +Query3+" are: ");
            Result(result3);
            System.out.println("The Documents Containing the words(AND): " +Query4+" are: ");
            Result(result4);
            
            // Two keyword search with OR
            String Query5="Robot Act";
            String Query6="fact target";
            ArrayList<Integer> result5 =  processOr(Query5);
       	 System.out.println("Test Case for Or Query");
            System.out.println("The Documents Containing the words(OR): " +Query5+" are: ");
            Result(result5);
            
            ArrayList<Integer> result6 =  processOr(Query6);
            System.out.println("The Documents Containing the words(OR): " +Query6+" are: ");
            Result(result6);
            
            //Multiple Keyword Search
           String Query7="man save cast";
            String Query8="late television 1960";
           ArrayList<Integer> result7 =  processMultiAnd(Query7);
            ArrayList<Integer> result8 =  processMultiAnd(Query8);
            System.out.println("Test Case for Multi keyword word AND");
          System.out.println("The Documents Containing the words(MultiAnd): " +Query7+" are: ");
         Result(result7);
            System.out.println("The Documents Containing the words(MultiAnd): " +Query8+" are: ");
            Result(result8);
            
         } 
    		
    }

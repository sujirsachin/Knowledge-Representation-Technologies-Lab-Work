import java.util.*;
/**
 * Course Number 612
 * Lab 02 Positional Index
 * Sachin Mohan Sujir
 */
 
public class PositionalIndex {
   //attributes
   private String[] myDocs;
   private ArrayList<String> termList;
   private ArrayList<ArrayList<Doc>> docLists;
   
   //Task 1
   /**
    * Construct a positional index
    * @param docs List of input strings
    *
    */

   public PositionalIndex(String[] docs) {
      
      myDocs = docs;
      termList = new ArrayList<String>();
      
      docLists = new ArrayList<ArrayList<Doc>>();
      ArrayList<Doc> docList;
      
      for(int i=0;i< myDocs.length;i++) {
         String[] words = myDocs[i].split(" ");
         String word;
         
         for(int j=0;j<words.length;j++) {
            boolean match = false;
            word = words[j];
            if(!termList.contains(word)) {                 // New term
               termList.add(word);
               docList = new ArrayList<Doc>();
               Doc doc = new Doc(i,j);
               docList.add(doc);
               docLists.add(docList);
            }
            else {                                         // Existing terms
               int index = termList.indexOf(word);
               docList = docLists.get(index);
               
               int k=0;
               for(Doc did:docList) {
                  if(did.docId == i) {
                     did.insertPosition(j);
                     docList.set(k, did);
                     match = true;
                     break;
                  }
                  k++;                 
               }
               if(!match) {                             // adding  doc id and position if match isn't found
                  Doc doc = new Doc(i,j);
                  docList.add(doc);
               }
               
            }
         }
      }
   }
   /**
   *
   * @param query - phrase query
   * @return finalList of intersected result
   */
  
   // Task 3
   public ArrayList<Integer> processQuery(String query) throws IndexOutOfBoundsException
   {
	   String[] q=query.split(" ");                              //splitting the multiple phrase query and storing it to a string array
	   ArrayList<Integer> finalList=new ArrayList<Integer>();
	   ArrayList<Doc> merged=docLists.get(termList.indexOf(q[0])); //storing first word of the phrase for merging
	
	   for(int i=1;i<q.length;i++)
	   {
		   ArrayList<Doc> temp=docLists.get(termList.indexOf(q[i]));  // getting each word and merging with existing merge list
		   System.out.println(temp.toString());
		   merged=intersect(merged,temp);
		   
		   
		   
	   }
	  if(merged.isEmpty())                                            // if position  list wasn't matched with the query phrase
	  { System.out.println("Match not Found");
	  return null;
	  }
	  
	  else
	  
		  
		  for(int j=0;j<merged.size();j++)
		  {
			  finalList.add(merged.get(j).docId);                     // Storing the merged list into integer array( Only docID)
		                                                              // for displaying the respective document
		  }
	
	return finalList;
	  
   }
   /**
   *
   * @param w1 first postings
   * @param w2 second postings
   * @return merged result of two postings
   */
   
   // Task 2
   private ArrayList<Doc> intersect(ArrayList<Doc> w1, ArrayList<Doc> w2) {
		   
	   ArrayList<Doc> intersected= new ArrayList<Doc>();
	  int wp1=0,wp2=0;
	  int pp1=0,pp2=0;
	  int flag=0;
	  while(wp1<w1.size()&&wp2<w2.size())                        //setting a pointer to the doclist
	  {
		  if(w1.get(wp1).docId==w2.get(wp2).docId)               //checking if both words are from same documents
		  {
			  ArrayList<Integer> p1=w1.get(wp1).positionList;    //getting position list
			  ArrayList<Integer> p2=w2.get(wp2).positionList;
			  while(pp1<p1.size())
			  {
				  while(pp2<p2.size())
				  {
					  
				  
				  if(p1.get(pp1) - p2.get(pp2) == -1)          // if word2 occurs after word1
				  {
					  Doc matchfound = new Doc(w1.get(wp1).docId, p2);    //storing the respective word's docId and position in Doc Constructor
                      intersected.add(matchfound);                        // adding it to doc array
                      flag=1;
                      break;                                              // break since match was found
				  }//2nd if
				  else if(p2.get(pp2)>p1.get(pp1))
                      break;
                  pp2++;
			
				  } //3nd while
				 if(flag==1) {
					 flag=0;
					 break;
				 }
				    pp1++;
				  
			  }//2nd while
			  wp1++;
			  wp2++;
		  }// 1st if
		  else if(w1.get(wp1).docId>w2.get(wp2).docId)
		  {
			  wp2++;
		  }
		  else
			  wp1++;
	  }//first while
	   return intersected;
	   
	   }
   /**
    * Creating string representation of  positional index
    */

	   
public String toString() {
	 System.out.println(" TermList \t PositionList DocNumber:<Position>");
      String outString = new String();
      for(int i=0;i<termList.size();i++) {
    	 
         outString += String.format("Word: %-15s", termList.get(i));
         ArrayList<Doc> docList = docLists.get(i);
         
         for(int j=0;j<docList.size();j++) {
        	 String a="s";
            outString += docList.get(j) + "\t";
         }
         outString += "\n";
      }
      return outString;
   }
   
   public static void main(String[] args)throws IndexOutOfBoundsException {
      String[] docs = {"text warehousing over big data",
                       "dimensional data warehouse over big data",
                       "nlp before text mining",
                        "nlp before text classification",
                       "Text mining good for unstructured data"};
      
     /* 
      * An alternate list of Document
      */
      
    /*
     String[] docs = {"A bug in the code is worth two in the documentation.",
                       "A computers attention span is as long as its power cord",
                       "A computer scientist is someone who fixes things that aren't broken",
                       "Adding manpower to a late software project makes it later"};
      
     */
                       
      PositionalIndex pi = new PositionalIndex(docs);
      System.out.println(pi);
  //Task 4
      String query1 = "text warehousing";                               // two-phrase query
      System.out.println("Query1: "+query1+ " --Two word Query");
     ArrayList<Integer> result1 = pi.processQuery(query1);
      if(result1!=null) {
	

      for(Integer i:result1) {
    	  
         System.out.println("Resultant doc: "+(i+1)+": "+docs[i.intValue()]);
      }
}
      String query2 = "nlp before text";                                // Three-phrase query
      System.out.println("\nQuery2: "+query2+ " --Three word Query");
     ArrayList<Integer> result2 = pi.processQuery(query2);
      if(result2!=null) {
	

      for(Integer i:result2) {
    	  
         System.out.println("Resultant doc: "+(i+1)+": "+docs[i.intValue()]);
      }
}
      String query3 = "text warehousing over big";                   // Four-phrase query
      System.out.println("\nQuery3: "+query3+ " --Four word Query");
     ArrayList<Integer> result3 = pi.processQuery(query3);
      if(result3!=null) {
	

      for(Integer i:result3) {
    	  
         System.out.println("Resultant doc: "+(i+1)+": "+docs[i.intValue()]);
      }
}
      String query4 = "Text mining good for unstructured";          // Five-phrase query
      System.out.println("\nQuery4: "+query4+ " --Five word Query");
     ArrayList<Integer> result4 = pi.processQuery(query4);
      if(result4!=null) {
	

      for(Integer i:result4) {
    	  
         System.out.println("Resultant doc: "+(i+1)+": "+docs[i.intValue()]);
      }
}


   }
}
//Doc class
class Doc {
   int docId;
   ArrayList<Integer> positionList;
   
   public Doc(int did, int position) {
   //#1
      docId = did;
      positionList = new ArrayList<Integer>();
      positionList.add(new Integer(position));
   }
   
   public Doc(int docId1, ArrayList<Integer> posAL1) {
	   docId = docId1;
       positionList = posAL1;

}

public void insertPosition(int position) {
  //#2
      positionList.add(new Integer(position));
   }
   
   public String toString() {
	  
      String docIdString = docId+1 + ":<";
      for(Integer pos:positionList) {
         docIdString += pos+1 + ",";
      }
      docIdString = docIdString.substring(0,docIdString.length()-1) + ">";
      return docIdString;
   }
}
import java.util.*;
/**
 * 612 LBE01 IncidenceMatrix
 * Prof. Kang
 */
 
/*public class IncidenceMatrixA1 {
   //attributes
   private String[] myDocs;               //input docs
   private ArrayList<String> termList;    //dictionary
   private ArrayList<int[]> docLists;
   
   //Constructor
   public IncidenceMatrixA1(String[] docs) {
     myDocs = docs;
     termList = new ArrayList<String>();
     docLists = new ArrayList<int[]>();
     
     for(int i=0;i<myDocs.length;i++) {
      String[] words = myDocs[i].split(" ");
      for(String word:words) {
         if(!termList.contains(word)) {
            termList.add(word);
            int[] docList = new int[myDocs.length];
            docList[i] = 1;
            docLists.add(docList);
          }
         else {
            int index =termList.indexOf(word);
            int[] docList = docLists.remove(index);
            docList[i] = 1;
            docLists.add(index, docList);
         
         }
      }
     }
   }
   
   public ArrayList<Integer> search(String query) {
      return null;
   }
   
   public String toString() {
      String outputString = new String();
      for(int i=0;i<termList.size();i++) {
         outputString += String.format("%-15s", termList.get(i));
         int[] docList = docLists.get(i);
         for(int j=0;j<docList.length;j++) {
            outputString += docList[j] + "\t";
         }
         outputString += "\n";
      }
      return outputString;
   }
   
   public static void main(String[] args) {
      //a document collection: corpus
      String[] docs = {"text data warehousing over big data",
                       "dimensional data warehousing over big data",
                       "nlp before text mining",
                       "nlp before text classification"}; 
      IncidenceMatrixA1 im = new IncidenceMatrixA1(docs);
      System.out.println(im);            
   }
}
*/
public class IncidenceMatrixA1
{
	
	private String[] myDocs;
	private static	ArrayList<String> termList;
	private static ArrayList<int[]> docLists;
	public IncidenceMatrixA1(String[] docs)
	{
		myDocs=docs;
		termList=new ArrayList<String>();
		docLists=new ArrayList<int[]>();
		for(int i=0;i<myDocs.length;i++)
		{
			String[] words=myDocs[i].split(" ");
			for(String word:words)
			{
				if(!termList.contains(word))
				{
					termList.add(word);
					int[] docList=new int[myDocs.length];
					docList[i]=1;
					docLists.add(docList);
					
				}
				else
				{
					int index=termList.indexOf(word);
					int[] docList=docLists.remove(index);
					docList[i]=1;
					docLists.add(index,docList);
					
				}
			}
		}
		
	}
	 public static ArrayList<Integer> search(String query) {
		 ArrayList<Integer> result = new ArrayList<Integer>();
	      int index = termList.indexOf(query);
	      if(index >= 0) {
	         int[] docList = docLists.get(index);
	        
	         for(int i=0;i<docList.length;i++) {
	            if(docList[i] != 0) {
	               if(!result.contains(i))
	                  result.add(i);
	            }
	         }
	      }
	   
	      return result;
	   }
	public String toString() {
	      String outputString = new String();
	      for(int i=0;i<termList.size();i++) {
	         outputString += String.format("%-15s", termList.get(i));
	         int[] docList = docLists.get(i);
	         for(int j=0;j<docList.length;j++) {
	            outputString += docList[j] + "\t";
	         }
	         outputString += "\n";
	      }
	      return outputString;
	}
public static void main(String args[])
{
	String[] docs = {"text data warehousing over big data",
            "dimensional data warehousing over big data",
            "nlp before text mining",
            "nlp before text classification"}; 
	IncidenceMatrixA1 im=new IncidenceMatrixA1(docs);
	System.out.println(im);
	String query="text";
	ArrayList<Integer>result=search(query);
	for(Integer i:result)
	{
		System.out.println(docs[i.intValue()]);
	}
}
}




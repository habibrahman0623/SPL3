package semi_supervised;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FileReader {
	
	public List<List<String>> myList = new ArrayList<List<String>>();
    private Scanner input;
    private String filePath;
    
    public FileReader(String filePath) {
		// TODO Auto-generated constructor stub
    	this.filePath=filePath;
	}

   public void openFile()
   {
      try
      {
         input = new Scanner( new File(filePath) );
      } 
      catch ( FileNotFoundException fileNotFoundException )
      {
         System.err.println( "Error opening file." );
         System.exit( 1 );
      } 
   } 
   public List<List<String>> readRecords()
   {

      try 
      {
        
        
        	 while (input.hasNextLine()){
        		 List<String> al=new ArrayList<String>();
        		 String line = input.nextLine();
        		  
        	     String[] tokens = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
        	     for (String token : tokens) {
       		     if(token != null){
       		    	al.add(token);
       		     }
       	        }
       		     myList.add(al);
           }
         
                   
      } 
      catch ( NoSuchElementException elementException )
      {
         System.err.println( "File improperly formed." );
         input.close();
         System.exit( 1 );
      }
      catch ( IllegalStateException stateException )
      {
         System.err.println( "Error reading from file." );
         System.exit( 1 );
      }
      return myList;
   } 
   public void closeFile()
   {
      if ( input != null )
         input.close();
   } 


}

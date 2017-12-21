package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
 
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		List<String> stopWords = new ArrayList<>();
		Scanner input = new Scanner(new File("stopwords.txt"));
		while (input.hasNextLine()){
   		 
  		     stopWords.add(input.nextLine());
        }
	    List<List<String>> myReports = new ArrayList<List<String>>();
	    FileReader fileReader = new FileReader("BugReport.txt");
	    fileReader.openFile();
	    myReports = fileReader.readRecords();
		PrintWriter writer = new PrintWriter("withoutStopWord.txt", "UTF-8");
		
		for (List<String> list : myReports) {
			StopWordsElemenation stopWordsElemenation = new StopWordsElemenation(stopWords, list);
			List<String> eleminatedWords = stopWordsElemenation.eleminateStopWords();
			
			String line = "";
			for (String string : eleminatedWords) {
				line = line + string + " ";
			}
			
			writer.println(line);
			
		}
		writer.close();
		Stemmer stemmer = new Stemmer();
		stemmer.performStemming("withoutStopWord.txt", "afterStemming.txt");
		
	
	}
}

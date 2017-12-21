package test;

import java.util.ArrayList;
import java.util.List;

public class StopWordsElemenation {
	
	List<String> StopWords = new ArrayList<>();
	List<String> AllWords = new ArrayList<>();
	
	
	public StopWordsElemenation(List<String> stopWords, List<String> allWords) {
		// TODO Auto-generated constructor stub
		StopWords = stopWords;
		AllWords = allWords;
	}
	
	public List<String> eleminateStopWords(){
		
		for(int i = 0; i < StopWords.size(); i++){
			for(int j = 0; j < AllWords.size(); j++){
				if(StopWords.get(i).compareToIgnoreCase(AllWords.get(j)) == 0){
					AllWords.remove(j);
					j--;
				}
			}
		}
		
		return AllWords;
	}
	

}

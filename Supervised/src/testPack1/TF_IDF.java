package testPack1;

import java.util.List;

public class TF_IDF {
	
	/*private String word;
	private List<String> document;
	private List<List<String>> documents;*/
	
	private Double tfCalculation(String word, List<String> document) {
		
		double wordCount = 0;
		for (String wordOnDoc : document) {
			if(word.equalsIgnoreCase(wordOnDoc)){
				wordCount++;
			}
		}
		
		return (wordCount/document.size());
	}
	
	private Double idfCalculation(String word, List<List<String>> documents) {
		
		double count = 0;
		for (List<String> document : documents) {
			for (String wordOnDoc : document) {
				if(word.equalsIgnoreCase(wordOnDoc)){
					count++;
					break;
				}
			}
			
		}
		
		return Math.log((documents.size()/count));
	}
	
	public double tf_idf_calculation(String word, List<String> document,List<List<String>> documents){
		
		return tfCalculation(word, document)*idfCalculation(word, documents);
	}

}

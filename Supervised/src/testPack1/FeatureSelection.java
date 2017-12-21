package testPack1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeatureSelection {
	
	List<List<String>> bugList = new ArrayList<>();
	List<List<String>> nonBugList = new ArrayList<>();
	Set<String> uniqueWords = new HashSet<>();
	List<Node> nodeList = new ArrayList<>();
	Double thresholdForFeatureSelection;

	
	
	public FeatureSelection(List<List<String>> bugList, List<List<String>> nonBugList, Double thresholdForFeatureSelection) {
		// TODO Auto-generated constructor stub
		this.bugList = bugList;
		this.nonBugList = nonBugList;
		this.thresholdForFeatureSelection = thresholdForFeatureSelection;
		for (List<String> list : bugList) {
			for (String word : list) {
				uniqueWords.add(word);
			}
		}
		for (List<String> list : nonBugList) {
			for (String word : list) {
				uniqueWords.add(word);
			}
		}
		
	}
	
	public List<Node> featureRanking() throws FileNotFoundException{
		
		int occurrencesInClass = 0;
		int occurrencesInAllClass = 0;
		int numberOfPositiveObservation = bugList.size();
		double chi_squire = 0;
		int numberOfAllObservation = numberOfPositiveObservation + nonBugList.size();
		
		for (String word : uniqueWords) {
			occurrencesInClass = countOccurrences(word, bugList);
			occurrencesInAllClass = occurrencesInClass + countOccurrences(word, nonBugList);
			Double ANMinusPM = (double) (occurrencesInClass * numberOfAllObservation - occurrencesInAllClass * numberOfPositiveObservation);
			Double NMinusP = (double) (numberOfAllObservation - numberOfPositiveObservation);
			Double NMinusM = (double) (numberOfAllObservation - occurrencesInAllClass);
			chi_squire = (numberOfAllObservation * Math.pow(ANMinusPM, 2.0)) / (numberOfPositiveObservation * occurrencesInAllClass *NMinusP*NMinusM);
			if(chi_squire >= thresholdForFeatureSelection){
				Node node = new Node(word);
				node.set_chiSquire(chi_squire);
				nodeList.add(node);
			}
			
			
		}
		
		Collections.sort(nodeList, new MyComparator());
		PrintWriter sortedFeature = new PrintWriter(new File("sortedFeature"));
		for (Node node : nodeList) {
			String featureValuePair = node.getWord() + " " + Double.toString(node.get_chiSquire()) + "\n";
			sortedFeature.write(featureValuePair);
			sortedFeature.flush();
		}
		sortedFeature.close();
		return nodeList;
	}
	
	
	
	public int countOccurrences(String feature, List<List<String>> reports){
		int count = 0;
		for (List<String> list : reports) {
			for (String word : list) {
				if(feature.equalsIgnoreCase(word)){
					count++;
					break;
				}
			}
		}
		return count;
	}
}

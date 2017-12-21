package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class HistogramBasedClustering {
	
	private List<List<String>> clusterList;
	private List<String> reportList;
	private double threshold;
	private int indexOfBugCluster;
	
	public HistogramBasedClustering(List<String> reports, double lowerHR) throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		clusterList = new ArrayList<List<String>>();
		reportList = new ArrayList<>();
		reportList = reports;
		
		threshold = lowerHR;
		
	}
	
	public List<List<String>> makeCluster(){
		
		
		
		clusterList.add(new ArrayList<String>());
		System.out.println(reportList.size());
		clusterInitilization();
		int temp = 0;
		int numberOfSentence = reportList.size();
		for(int i = 0; i < numberOfSentence; i++){
			double histogramRatio1;
			double histogramRatio2;
			histogramRatio1 = histogramRatio(clusterList.get(0), reportList.get(i));
			histogramRatio2 = histogramRatio(clusterList.get(1), reportList.get(i));
			if(histogramRatio1 > histogramRatio2){
				clusterList.get(0).add(reportList.get(i));
				temp++;
			}
			else {
				clusterList.get(1).add(reportList.get(i));
				temp++;
			}
			/*for(int j = 0; j < clusterList.size(); j++){
				
				double oldHR = histogramRatio(clusterList.get(j));
				double newHR = histogramRatio(clusterList.get(j), reportList.get(i));
				if(newHR >= oldHR || (newHR >= threshold && (oldHR - newHR) < epsilon)){
					
					clusterList.get(j).add(reportList.get(i));
					break;
				}
				
				else {
					clusterList.add(new ArrayList<>());
					clusterList.get(clusterList.size() - 1).add(reportList.get(i));
					
				}
			}*/
		}
		
		return clusterList;
	}
	
	
	private double histogramRatio(List<String> cluster, String report){
		
		double similarity, histogramRatio;
		int similarityCount = 0;
		Cosine_Similarity cosine_Similarity = new Cosine_Similarity();
		
		for (String string : cluster) {
			similarity = cosine_Similarity.Cosine_Similarity_Score(report, string);
			if(similarity > threshold){
				similarityCount++;
			}
			
		}
		histogramRatio = similarityCount*1.0/cluster.size();
		return histogramRatio;
	}
	
	private void clusterInitilization(){
		
		Cosine_Similarity cosine_Similarity = new Cosine_Similarity();
		clusterList.get(0).add(reportList.get(0));
		String bug = reportList.remove(0);
		double similarityValue;
		for (String string : reportList) {
			similarityValue = cosine_Similarity.Cosine_Similarity_Score(bug, string);
			if(similarityValue < 0.3){
				clusterList.add(new ArrayList<String>());
				clusterList.get(1).add(string);
				reportList.remove(string);
				break;
			}
		}
		
	}
	
	
	private List<String> frequentTermCalculation(Set<String> uniqueWord, List<List<String>>reports){
		
		List<String> frequentTerms = new ArrayList<>();
		List<Node> nodeList = new ArrayList<>();
		int frequency;
		for (String string : uniqueWord) {
			
			frequency = TFValue(string, reports);
			nodeList.add(new Node(string, frequency));
		}
		Collections.sort(nodeList, new MyComparator());
		/*for(int i = 0; i < 200; i++){
			frequentTerms.add(nodeList.get(i).word);
		}*/
		return frequentTerms; 
		
	}
	
	public void clusterLabeling() throws FileNotFoundException, UnsupportedEncodingException{
		
		
		//Set<String> uniqueWord = new HashSet<>();
		List<List<String>> reports = new ArrayList<>();
		List<String> labelingWords = new ArrayList<>();
		labelingWords.add("error");
		labelingWords.add("fail");
		labelingWords.add("except");
		int tfInCluster1 = 0, tfInCluster2 = 0;
		for(int i = 0; i < 2; i++){
			int j = 0;
			reports.add(new ArrayList<>());
			for (String string : clusterList.get(i)) {
				String [] words = string.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
				for (String string2 : words) {
					if(string2.length() > 2){
						//uniqueWord.add(string2);
						reports.get(j).add(string2);
					}
				}
				j++;
				reports.add(new ArrayList<>());
			}
			if( i == 0){
				tfInCluster1 = totalTFInCluster(labelingWords, reports);
				reports.clear();
			}
			
			else {
				tfInCluster2 = totalTFInCluster(labelingWords, reports);
			}
			//frequentTermCalculation(uniqueWord, reports);
			
		}
		System.out.println(tfInCluster1 + "\t" + tfInCluster2);
		if( tfInCluster1 > tfInCluster2){
			indexOfBugCluster = 0;
			writeClusterValueOnFollowingFile(indexOfBugCluster);
		}
		else {
			indexOfBugCluster = 1;
			writeClusterValueOnFollowingFile(indexOfBugCluster);
		}
		
	}
	
	private void writeClusterValueOnFollowingFile(int indexOfBugCluster) throws FileNotFoundException, UnsupportedEncodingException{
		
		PrintWriter writer1 = new PrintWriter("bug.txt", "UTF-8");
		PrintWriter writer2 = new PrintWriter("nonBug.txt", "UTF-8");
		if( indexOfBugCluster == 1){
			
			for(int i = 0; i < clusterList.get(1).size(); i++){
				//System.out.println(i + " " + clusterList.get(0).get(i).length());
				writer1.println(clusterList.get(1).get(i));
			}
			writer1.close();
			for(int i = 0; i < clusterList.get(0).size(); i++){
				writer2.println(clusterList.get(0).get(i));
			}
			
			writer2.close();
		}
		else {
			
			for(int i = 0; i < clusterList.get(0).size(); i++){
				writer1.println(clusterList.get(0).get(i));
			}
			
			writer1.close();
			for(int i = 0; i < clusterList.get(1).size(); i++){
				//System.out.println(i + " " + clusterList.get(0).get(i).length());
				writer2.println(clusterList.get(1).get(i));
			}
			writer2.close();
		}
	}
	
	private int totalTFInCluster(List<String> words, List<List<String>> reports){
		
		int totalTF = 0;
		for (String string : words) {
			totalTF = totalTF + TFValue(string, reports);
		}
		
		return totalTF;
	}
	
	
	
	private int TFValue(String word, List<List<String>>reports){
		
		int count = 0;
		int totalWord = 0;
		for (List<String> list : reports) {
			for (String string : list) {
				if(string.toLowerCase().contains(word)){
					count++;
				}
				totalWord++;
			}
		}
		
		return count;
	}
	
	public void testResultCalculation(List<String> reportType){
		
		int reportSize = reportList.size();
		int truePositive = 0, trueNegative = 0, falsePositive = 0, falseNegative = 0;
		double accuracy, precision, recall, fMeasure;
		for(int i = 0; i < reportSize; i++){
			
			if(reportType.get(i).toLowerCase().contains("bug")){
				if(searchReportOnCluster(clusterList.get(indexOfBugCluster), reportList.get(i))){
					truePositive++;
				}
				else {
					falseNegative++;
				}
			}
			else {
				if(searchReportOnCluster(clusterList.get(1 - indexOfBugCluster), reportList.get(i))){
					trueNegative++;
				}
				else {
					falsePositive++;
				}
			}
			
			
		}
		
		accuracy = ((truePositive + trueNegative)*1.0) / reportSize;
		precision = (truePositive * 1.0) / (truePositive + falsePositive);
		recall = (truePositive * 1.0) / (truePositive + falseNegative);
		fMeasure = (2.0 * precision * recall) / (precision + recall);
		
		System.out.println("Accuracy: " + accuracy + "\n" + "Precision: " + precision + "\n" + "Recall: " + recall + "\n" + "F-Measure: " + fMeasure);
	}
	
	private boolean searchReportOnCluster(List<String> cluster, String report){
		
		boolean flag = false;
		for (String string : cluster) {
			if(string.toLowerCase().contains(report.toLowerCase())){
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
		List<String> reportList = new ArrayList<>();
		List<String> reportType = new ArrayList<>();
		Scanner input = new Scanner(new File("afterStemming.txt"));
		Scanner input2 = new Scanner(new File("reportType.txt"));
		
		
		while (input.hasNextLine()){
   		 
  		     reportList.add(input.nextLine());
        }
		input.close();
		while (input2.hasNextLine()){
	   		 
 		     reportType.add(input2.nextLine());
       }
		input2.close();
		HistogramBasedClustering histogramBasedClustering = new HistogramBasedClustering(reportList, 0.4);
		histogramBasedClustering.makeCluster();
		histogramBasedClustering.clusterLabeling();
		histogramBasedClustering.testResultCalculation(reportType);
	}	

}

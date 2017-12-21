package testPack1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Training {
	
	private List<List<String>> reportlist;
	private List<List<String>> bugReportList;
	private List<List<String>> nonBugReportList;
	private List<Node> nodeListForBug;
	private List<Node> nodeListForNonBug;
	private Set<String> uniqueWord;
	private List<Node> selectedFeature;
	
	public Training(List<List<String>> bugReportlist, List<List<String>> nonBugReportList, double featureSelectionThresold) throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		reportlist = new ArrayList<List<String>>(bugReportlist);
		this.bugReportList = new ArrayList<List<String>>();
		this.nonBugReportList = new ArrayList<List<String>>();
		this.bugReportList = bugReportlist;
		this.nonBugReportList = nonBugReportList;
		reportlist.addAll(nonBugReportList);
		nodeListForBug = new ArrayList<>();
		nodeListForNonBug = new ArrayList<>();
		uniqueWord = new HashSet<>();
		selectedFeature = new ArrayList<>();
		FeatureSelection featureSelection = new FeatureSelection(bugReportlist,nonBugReportList,featureSelectionThresold);
		selectedFeature = featureSelection.featureRanking();
				
	}
	
	public void calculateAll_tf_idf(){
		
		double tf_idf_value = 0;
		int index;
		TF_IDF tf_idf = new TF_IDF();
		for (List<String> doc : bugReportList) {
			for (String word : doc) {
				
				if(findNode(selectedFeature, word)!= -1){
					uniqueWord.add(word);
					tf_idf_value = tf_idf.tf_idf_calculation(word, doc, reportlist);
					index = findNode(nodeListForBug, word);
					if(index != -1){
							
						nodeListForBug.get(index).set_tf_idf_value(tf_idf_value);
					}
					else {
						Node node = new Node(word);
						node.set_tf_idf_value(tf_idf_value);
						nodeListForBug.add(node);
					}
				}
			}
		}
		
		for (List<String> doc : nonBugReportList) {
			for (String word : doc) {
				
				if(findNode(selectedFeature, word)!= -1){
					
					uniqueWord.add(word);
					tf_idf_value = tf_idf.tf_idf_calculation(word, doc, reportlist);
					
					index = findNode(nodeListForNonBug, word);
					if(index != -1){
							
						nodeListForNonBug.get(index).set_tf_idf_value(tf_idf_value);
					}
					else {
						Node node = new Node(word);
						node.set_tf_idf_value(tf_idf_value);
						nodeListForNonBug.add(node);
					}
				}
			}
		}
		
		System.out.println();
	}
	
	
	
	public int findNode(List<Node> nodeList, String word){
		
		int index = 0;
		boolean flag = false;
		if(nodeList.size() != 0){
			
			for (Node node : nodeList) {
				
				if(node.getWord().equalsIgnoreCase(word)){
					
					flag = true;
					break;
				}
				
				index++;
			}
		}
		
		if(flag){
			return index;
		}
		else {
			return -1;
		}
	}
	
	public void naiveBayes() throws FileNotFoundException, UnsupportedEncodingException{
		
		PrintWriter trainingFile = new PrintWriter(new File("training.txt"));
		int total_unique_words = uniqueWord.size();
		double total_tf_idf_for_bug_class = total_tf_idf_in_class(nodeListForBug);
		double total_tf_idf_for_nonBug_class = total_tf_idf_in_class(nodeListForNonBug);
		for (Node node : nodeListForBug) {
			double bugProbability = 0;
			double nonBugProbability = 0;
			bugProbability = (node.get_total_tf_idf() + 1)/(total_tf_idf_for_bug_class + total_unique_words);
			if(findNode(nodeListForNonBug, node.getWord()) == -1){
				nonBugProbability = 1/(total_tf_idf_for_nonBug_class + total_unique_words);
			}
			else {
				int index = findNode(nodeListForNonBug, node.getWord());
				nonBugProbability = (nodeListForNonBug.get(index).get_total_tf_idf() + 1)/ (total_tf_idf_for_nonBug_class + total_unique_words);
			}
			String training_value = node.getWord() + " " + Double.toString(bugProbability) + " " + Double.toString(nonBugProbability) + "\n";
			trainingFile.write(training_value);
			trainingFile.flush();
		}
		for (Node node : nodeListForNonBug) {
			double bugProbability = 0;
			double nonBugProbability = 0;
			if(findNode(nodeListForBug, node.getWord()) == -1){
				
				nonBugProbability = (node.get_total_tf_idf() + 1)/(total_tf_idf_for_nonBug_class + total_unique_words);
				bugProbability = 1/(total_tf_idf_for_bug_class + total_unique_words);
				String training_value = node.getWord() + " " + Double.toString(bugProbability) + " " + Double.toString(nonBugProbability) + "\n";
				trainingFile.write(training_value);
				trainingFile.flush();
			}
			
			
			
		}
		
	}
	
	public double total_tf_idf_in_class(List<Node> nodelist){
		
		double value = 0;
		for (Node node : nodelist) {
			value =+ node.get_total_tf_idf();
		}
		
		return value;
	}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		List<List<String>> bugList = new ArrayList<>();
		List<List<String>> nonBugList = new ArrayList<>();
		Stemmer stemmer = new Stemmer();
		stemmer.performStemming("bug.txt", "afterStemmingBugSummary.txt");
		stemmer.performStemming("nonBug.txt", "afterStemmingNonBugSummary.txt");
		FileReader fileReader1 = new FileReader("afterStemmingBugSummary.txt");
		FileReader fileReader2 = new FileReader("afterStemmingNonBugSummary.txt");
		fileReader1.openFile();
		bugList = fileReader1.readRecords();
		fileReader1.closeFile();
		fileReader2.openFile();
		nonBugList = fileReader2.readRecords();
		fileReader2.closeFile();
		
		Training training = new Training(bugList, nonBugList, 8.0);
		training.calculateAll_tf_idf();
		training.naiveBayes();
		
	}
	
}

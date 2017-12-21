package semi_supervised;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Training {
	
	private List<List<String>> reportlist;
	private List<List<String>> bugReportList;
	private List<List<String>> nonBugReportList;
	private List<Node> nodeListForBug;
	private List<Node> nodeListForNonBug;
	private Set<String> uniqueWord;
	
	
	public void updateTrainingData(List<List<String>> bugReportlist, List<List<String>> nonBugReportList) {
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
		
				
	}
	
	public void calculateAll_tf_idf(){
		
		double tf_idf_value = 0;
		int index;
		TF_IDF tf_idf = new TF_IDF();
		for (List<String> doc : bugReportList) {
			for (String word : doc) {
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
		
		for (List<String> doc : nonBugReportList) {
			for (String word : doc) {
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
	
	public void selfTraining(List<List<String>> reportlist, int chunkSize) throws FileNotFoundException{
		
		try {
			FileWriter bugFile = new FileWriter("afterStemmingBugSummary.txt", true);
			FileWriter nonBugFile = new FileWriter("afterStemmingNonBugSummary.txt", true);
			List<List<String>> test = new ArrayList<>();
			
			for(int i = 0; i < reportlist.size(); i++){
				
				test.add(reportlist.get(i));
				if(i%chunkSize == 0){
					
					Map<String, String> result = new HashMap<>();
					result = classifyTestdata(test);
					for(String key : result.keySet()){
						
						if(result.get(key).equalsIgnoreCase("bug")){
							bugFile.write(key + "\n");
							bugFile.flush();
						}
						else {
							nonBugFile.write(key + "\n");
							nonBugFile.flush();
						}
					}
					test.clear();
				}
			}
			
			bugFile.close();
			nonBugFile.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public Map<String, List<Double>> readTrainedData() throws FileNotFoundException{
		
		Scanner input = new Scanner(new File("training.txt"));
		Map<String, List<Double>> trainedData = new HashMap<>();
		
		
		while(input.hasNextLine()){
			List<Double> probability = new ArrayList<>();
			String line = input.nextLine();
			String [] data = line.split("\\s+");
			if(data.length == 3){
				
				probability.clear();
				probability.add(Double.parseDouble(data[1]));
				probability.add(Double.parseDouble(data[2]));
				trainedData.put(data[0], probability);
			}
		}
		input.close();
		
		return trainedData;
	}
	
	public Map<String, String> classifyTestdata(List<List<String>> testData) throws FileNotFoundException{
		
		
		Map<String, String> resultMap = new HashMap<>();
		Map<String, List<Double>> trainedData = new HashMap<>();
		trainedData = readTrainedData();
		for (List<String> list : testData) {
			double bugProbability = 0,nonBugProbability = 0;
			String report = "";
			for (String word : list) {
				report = report + word + " " ;
				if(trainedData.containsKey(word)){
					
					bugProbability =+ trainedData.get(word).get(0);
					nonBugProbability =+ trainedData.get(word).get(1);
				}
				
			}
			if(bugProbability > nonBugProbability){
				
				resultMap.put(report, "bug");
			}
			else {
				resultMap.put(report, "nonBug");
			}
		}
		return resultMap;
	}

	
	public void supervisedTraining() throws FileNotFoundException, UnsupportedEncodingException{
		
		List<List<String>> bugList = new ArrayList<>();
		List<List<String>> nonBugList = new ArrayList<>();
		
		FileReader bugFile = new FileReader("afterStemmingBugSummary.txt");
		FileReader nonBugFile = new FileReader("afterStemmingNonBugSummary.txt");
		bugFile.openFile();
		bugList = bugFile.readRecords();
		bugFile.closeFile();
		nonBugFile.openFile();
		nonBugList = nonBugFile.readRecords();
		nonBugFile.closeFile();
		updateTrainingData(bugList, nonBugList);
		calculateAll_tf_idf();
		naiveBayes();
	}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
		Training training = new Training();
		training.supervisedTraining();
		FileReader unsupervisedFile = new FileReader("unsupervised.txt");
		List<List<String>> reports = new ArrayList<>();
		unsupervisedFile.openFile();
		reports = unsupervisedFile.readRecords();
		unsupervisedFile.closeFile();
		training.selfTraining(reports, 200);
	}


}

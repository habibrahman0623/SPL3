package testPack1;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DTreeTraining {
	
	List<List<Double>> bagOfWords;
	List<List<String>> bugs;
	List<List<String>> nonBugs;
	List<List<String>> reports;
	List<Node> selectedFeature;
	List<List<Double>> trainingData;
	List<List<Double>> testData;
	List<Double> theta;
	TreeNode root;
	
	public DTreeTraining(List<List<String>> bugReport, List<List<String>> nonBugReport, double featureSelectionThresold) throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		bagOfWords  = new ArrayList<>();
		bugs = new ArrayList<>();
		nonBugs = new ArrayList<>();
		reports = new ArrayList<>(bugReport);
		selectedFeature = new ArrayList<>();
		root = new TreeNode();
		bugs = bugReport;
		nonBugs = nonBugReport;
		reports.addAll(nonBugReport);
		FeatureSelection featureSelection = new FeatureSelection(bugs, nonBugs, featureSelectionThresold);
		selectedFeature = featureSelection.featureRanking();
		
	}
	
	public void makeBagOfWords(){
		
		int i = 0;
		TF_IDF tf_IDF = new TF_IDF();
		for (List<String> list : reports) {
			bagOfWords.add(new ArrayList<>());
			for (Node node : selectedFeature) {
				Double tf_idf_value;
				Boolean flag = true;
				for (String word : list) {				
					
						if(word.equalsIgnoreCase(node.getWord())){
							tf_idf_value = tf_IDF.tf_idf_calculation(word, list, reports);
							bagOfWords.get(i).add(tf_idf_value);
							flag = false;
							break;
						}						
				}
				if(flag){
					bagOfWords.get(i).add(0.0);
				}
			}
			i++;
		}
	}
	
	public void prepareDataset(){
		
		trainingData = new ArrayList<>();
		testData = new ArrayList<>();
		int numberOfBug = bugs.size();
		for(int i = 0; i < bagOfWords.size(); i++){
			if(i < numberOfBug){
				bagOfWords.get(i).add(0.0);
			}
			else {
				bagOfWords.get(i).add(1.0);
			}
		}
		int temp1 = (int) (0.65 * bagOfWords.size());
		int temp2 = (int) (0.75 * bagOfWords.size());
		for(int i = 0; i < bagOfWords.size(); i++){
			if(i < temp1){
				trainingData.add(bagOfWords.get(i));
			}
			else if(i > temp2){
				trainingData.add(bagOfWords.get(i));
			}
			else {
				testData.add(bagOfWords.get(i));
			}
		}
	}
	
	public void training(){
		makeBagOfWords();
		prepareDataset();
		theta = new ArrayList<>();
		Tree decisionTree = new Tree();
		decisionTree.inputMenipulation(trainingData);
		root = decisionTree.root;
		
		
	}
	
	public void dtTesting(){
		int num = 0;
		double actualResult = 0;
		double expectedResult;
		
		int numerOfColumn = testData.get(0).size() - 2;
		int truePositive = 0, trueNegative = 0, falsePositive = 0, falseNegative = 0;
		double accuracy, precision, recall, fMeasure;
		for(int i = 0; i < testData.size(); i++){
			
			try {
				actualResult = testData.get(i).get(numerOfColumn + 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
			expectedResult = checkDTResult(testData.get(i).subList(0, numerOfColumn+1));
			if(actualResult == 0){
				if(actualResult == expectedResult){
					truePositive++;
				}
				else {
					falseNegative++;
				}
				
			}
			
			else {
				if(actualResult == expectedResult){
					trueNegative++;
				}
				else {
					falsePositive++;
				}
			}
		}
		
		accuracy = ((truePositive + trueNegative)*1.0)/testData.size();
		precision = (truePositive * 1.0) / (truePositive + falsePositive);
		recall = (truePositive * 1.0) / (truePositive + falseNegative);
		fMeasure = (2.0 * precision * recall) / (precision + recall);
		
		System.out.println("Accuracy: " + accuracy + "\n" + "Precision: " + precision + "\n" + "Recall: " + recall + "\n" + "F-Measure: " + fMeasure);

	}
	
	public double checkDTResult(List<Double> checkList){
		
		TreeNode current = new TreeNode();
		current = root;
		int indexOfClass = current.list.get(0).size() - 1;
		while(true){
			try {
				if(current.value < checkList.get(current.column)){
					current = current.rightNode;
				}
				else {
					current = current.leftNode;
				}
				if(current.value == null){
					double temp = current.list.get(0).get(indexOfClass);
					//System.out.println(current.list.get(0).get(temp));
					return temp;
				}
				
				if(current.value < checkList.get(current.column)){
					current = current.rightNode;
				}
				else {
					current = current.leftNode;
				}
				if(current.value == null){
					int temp = current.list.get(0).size() - 1;
					//System.out.println(current.list.get(0).get(temp));
					return temp;
				}
		
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(current.column);
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		List<List<String>> bugList = new ArrayList<>();
		List<List<String>> nonBugList = new ArrayList<>();
		FileReader fileReader1 = new FileReader("afterStemmingBugSummary.txt");
		FileReader fileReader2 = new FileReader("afterStemmingNonBugSummary.txt");
		fileReader1.openFile();
		bugList = fileReader1.readRecords();
		fileReader1.closeFile();
		fileReader2.openFile();
		nonBugList = fileReader2.readRecords();
		fileReader2.closeFile();
		
		DTreeTraining dTreeTraining = new DTreeTraining(bugList, nonBugList, 50.0);
		dTreeTraining.training();
		dTreeTraining.dtTesting();
		
	}


}

package testPack1;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LRTraining {
	
	List<List<Double>> bagOfWords;
	List<List<String>> bugs;
	List<List<String>> nonBugs;
	List<List<String>> reports;
	List<Node> selectedFeature;
	List<List<Double>> trainingData;
	List<List<Double>> testData;
	List<Double> theta;
	TreeNode root;
	
	public LRTraining(List<List<String>> bugReport, List<List<String>> nonBugReport, double featureSelectionThresold) throws FileNotFoundException {
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
		GradientDescent gradientDescent = new GradientDescent(trainingData);
		//System.out.println(selectedFeature.size());
		theta = new ArrayList<>();
		theta = gradientDescent.gradientDescentCalcualtion(0.5, 0.01);
		/*Tree decisionTree = new Tree();
		decisionTree.inputMenipulation(trainingData);
		root = decisionTree.root;*/
		
		
	}
	
	public void dtTesting(){
		int num = 0;
		double accuracy;
		double actualResult;
		double expectedResult;
		
		int numerOfColumn = testData.get(0).size() - 2;
		for(int i = 0; i < testData.size(); i++){
			
			actualResult = testData.get(i).get(numerOfColumn + 1);
			expectedResult = checkDTResult(testData.get(i).subList(0, numerOfColumn));
			if(actualResult == expectedResult){
				
				num++;
			}
		}
		
		accuracy = num * 1.0 / testData.size();
		System.out.println(accuracy);
	}
	
	public double checkDTResult(List<Double> checkList){
		
		TreeNode current = new TreeNode();
		current = root;
		
		while(true){
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
		}
	}
	public void lrTesting(){
		
		
		int truePositive = 0, trueNegative = 0, falsePositive = 0, falseNegative = 0;
		double accuracy, precision, recall, fMeasure;
		
		int numberOfColumn = testData.get(0).size();
		for (int i = 0; i < testData.size(); i++) {
			double acctualResult = testData.get(i).get(numberOfColumn - 1);
			double hTheta = hypothesisValue(i);
			double expectedResult;
			if(hTheta >= 0.5){
				if(acctualResult == 1){
					trueNegative++;
				}
				else {
					falsePositive++;
				}
				
			}
			
			else {
				if(acctualResult == 0){
					truePositive++;
				}
				else {
					falseNegative++;
				}
			}			
		}
		
		
		accuracy = ((truePositive + trueNegative)*1.0)/testData.size();
		precision = (truePositive * 1.0) / (truePositive + falsePositive);
		recall = (truePositive * 1.0) / (truePositive + falseNegative);
		fMeasure = (2.0 * precision * recall) / (precision + recall);
		
		System.out.println("Accuracy: " + accuracy + "\n" + "Precision: " + precision + "\n" + "Recall: " + recall + "\n" + "F-Measure: " + fMeasure);
		
	}
	private Double hypothesisValue(int row){
		Double value = theta.get(0);
		for(int i = 1; i < theta.size(); i++){
			//System.out.println(i + " " + row);
			value = value + theta.get(i)*testData.get(row).get(i-1);
		}
		double hTheta = 1/(1+Math.exp(-value));
		return hTheta;
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
		
		LRTraining lrTraining = new LRTraining(bugList, nonBugList, 25.0);
		lrTraining.training();
		//lrTraining.dtTesting();
		lrTraining.lrTesting();
		
	}

}

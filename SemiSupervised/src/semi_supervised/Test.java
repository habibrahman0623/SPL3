package semi_supervised;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Test {
	
	private Map<String, List<Double>> trainedData;
	private List<List<String>> testData;
	private List<String> result;
	
	public Test(Map<String, List<Double>> trainedata, List<List<String>> testData){
		
		this.trainedData = new HashMap<>();
		this.testData = new ArrayList<>();
		this.trainedData = trainedata;
		this.testData = testData;
		result = new ArrayList<>();
	}
	
	public void classifyTestdata(){
		
		Map<String, String> reportsMap = new HashMap<>();
		int i = 0;
		int truePositive = 0, trueNegative = 0, falsePositive = 0, falseNegative = 0;
		double accuracy, precision, recall, fMeasure;
		for (List<String> list : testData) {
			i++;
			double bugProbability = 0,nonBugProbability = 0;
			String report = "";
			for (String word : list) {
				report = report + word;
				if(trainedData.containsKey(word)){
					
					bugProbability =+ trainedData.get(word).get(0);
					nonBugProbability =+ trainedData.get(word).get(1);
				}
				
			}
			if(bugProbability > nonBugProbability){
				if(i < 300){
					truePositive++;
				}
				else {
					falseNegative++;
				}
				
			}
			else {
				if( i > 300){
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
	

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
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
		List<List<String>> testdata = new ArrayList<>();
		FileReader testFile = new FileReader("test.txt");
		testFile.openFile();
		testdata = testFile.readRecords();
		testFile.closeFile();
		Test test = new Test(trainedData, testdata);
		test.classifyTestdata();
		
			
		

	}



}

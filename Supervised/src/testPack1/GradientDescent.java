package testPack1;

import java.util.ArrayList;
import java.util.List;

public class GradientDescent {
	
	private List<List<Double>> dataset;
	public List<Double> theta;
	
	public GradientDescent(List<List<Double>> data) {
		// TODO Auto-generated constructor stub
		
		dataset = data;
		theta = new ArrayList<>();
		for(int i = 0; i<dataset.get(0).size(); i++){
			theta.add(1.0);
		}
		
	}
	
	public List<Double> gradientDescentCalcualtion(Double learningRate, Double epsilon){
		List<Double> tempTheta = new ArrayList<>();
		
		while(true){
			for(int i = 0; i < theta.size(); i++){
				Double temp;
				temp = theta.get(i) - (learningRate/dataset.size())*summationPart(i);
				tempTheta.add(temp);
			}
			if(isConverged(tempTheta, epsilon)){
				break;
			}
			theta.clear();
			for(int i = 0; i < tempTheta.size(); i++){
				theta.add(tempTheta.get(i));
			}
			tempTheta.clear();
			
		}
		return theta;
	}
	
	private Boolean isConverged(List<Double> newTheta, Double epsilon){
		Boolean flag = true;
		for(int i = 0; i < theta.size(); i++){
			Double temp = Math.abs((newTheta.get(i) - theta.get(i)));
			if( temp > epsilon){
				flag = false;
				break;
			}
		}
		
		return flag;
	}
	
	private Double summationPart(int column){
		Double summation = 0.0;
		int columnNumber = dataset.get(0).size();
		for(int m = 0; m < dataset.size(); m++){
			if(column != 0){
				summation = summation + (hypothesisValue(m) - dataset.get(m).get(columnNumber-1))*dataset.get(m).get(column-1);
			}
			
			else {
				summation = summation + (hypothesisValue(m) - dataset.get(m).get(columnNumber-1));
			}
		}
		
		return summation;
		
	}
	
	private Double hypothesisValue(int row){
		Double value = theta.get(0);
		for(int i = 1; i < theta.size(); i++){
			//System.out.println(i + " " + row);
			value = value + theta.get(i)*dataset.get(row).get(i-1);
		}
		double hTheta = 1/(1+Math.exp(-value));
		return hTheta;
	}
	
	

}

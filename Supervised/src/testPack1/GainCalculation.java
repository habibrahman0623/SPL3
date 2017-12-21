package testPack1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class GainCalculation {
	Map<Double,Integer>map = new HashMap<Double,Integer>();
	List<List<Double>> value = new ArrayList<List<Double>>();
	public GainCalculation(List<List<Double>> value) {
		// TODO Auto-generated constructor stub
		this.value = value;
	}
	public TreeNode gain(){
		Double maxGain = Double.MIN_VALUE; 
		TreeNode node = new TreeNode();
		List<Double>decisionList = new ArrayList<Double>();
		for(int i = 0; i < value.size(); i++){
			decisionList.add(value.get(i).get(value.get(i).size()-1));
		}
		Double parentEntropy = entropy(decisionList);
		if(parentEntropy == 0){
			node.column = -1;
			return node;
		}
		for(int i = 0; i < value.get(0).size()-1; i++){
			
			for(int j = 0; j < value.size(); j++){
				List<Double>left = new ArrayList<Double>();
				List<Double>right = new ArrayList<Double>();
				Double avgEntropy,gain;
				for(int k = 0; k < value.size(); k++){
					if(value.get(j).get(i) > value.get(k).get(i)){
						left.add(value.get(k).get(value.get(k).size()-1));
					}
					else {
						right.add(value.get(k).get(value.get(k).size()-1));
					}
				}
				if(left.size()!= 0 && right.size()!= 0){
					avgEntropy = ((double)left.size() / value.size()) * entropy(left) + ((double)right.size() / value.size()) * entropy(right);
					gain = parentEntropy - avgEntropy;
					if(gain > maxGain){
						maxGain = gain;
						node.column = i;
						node.value = value.get(j).get(i);
					}
				}
			}
		}
		
		return node;
	}
	public Double entropy(List<Double> decisionList) {
		Double entropy = 0.0;
		Set<Double> decisionSet = new TreeSet<>();
		for(int i = 0; i < decisionList.size(); i++){
			decisionSet.add(decisionList.get(i));
		}
		for(Double value: decisionSet){
			int numberOfADecision = 0;
			for(int j = 0;j < decisionList.size(); j++){
				if(decisionList.get(j).equals(value)){
					numberOfADecision++;
				}
			}
			Double tempValue = (numberOfADecision / (double)decisionList.size());
			entropy = entropy - (tempValue * (Math.log(tempValue) / Math.log(2.0)));
		}
		
		return entropy;
	}

}

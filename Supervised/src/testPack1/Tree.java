package testPack1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree {
	
	public List<List<Double>> inputList = new ArrayList<List<Double>>();
	public List<List<Double>> checkList = new ArrayList<List<Double>>();
	Random random = new Random();

	public TreeNode root = new TreeNode();
	public void inputMenipulation(List<List<Double>> dataSet){
		inputList = dataSet;
		root.list = inputList;
		treeConstruction(root.list, root);
		List<Double>check = new ArrayList<Double>();
		for(int i = 0; i < inputList.get(0).size(); i++){
			check.add(inputList.get(3100).get(i));
		}
		check(check);
	}
	public void treeConstruction(List<List<Double>> inputMatrix, TreeNode node){
		GainCalculation gainCalculation = new GainCalculation(inputMatrix);
		TreeNode tempnode = new TreeNode();
		tempnode = gainCalculation.gain();
		if(tempnode.column == -1){
			return;
		}
		else {
			node.column = tempnode.column;
			node.value = tempnode.value;
			node.leftNode = new TreeNode();
			node.rightNode = new TreeNode();

			//System.out.println(node.column+" "+node.value);
			for(int i = 0; i < inputMatrix.size(); i++){
				//List<Double>tempList=new ArrayList<Double>();
				if(node.value != null){
					if(inputMatrix.get(i).get(node.column) >= node.value){
						List<Double> tempList = new ArrayList<Double>();
						for(int j = 0; j < inputMatrix.get(0).size(); j++){
							tempList.add(inputMatrix.get(i).get(j));
						}
						node.rightNode.list.add(tempList);
					}
					else {
						List<Double> tempList = new ArrayList<Double>();
						for(int j = 0; j < inputMatrix.get(0).size(); j++){
							tempList.add(inputMatrix.get(i).get(j));
						}
						node.leftNode.list.add(tempList);

					}
				}
			}
			treeConstruction(node.leftNode.list, node.leftNode);
			treeConstruction(node.rightNode.list, node.rightNode);

		}
		
	}
	public void check(List<Double> checkList){
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
				System.out.println(current.list.get(0).get(temp));
				return;
			}
		}
		
	}
	



}

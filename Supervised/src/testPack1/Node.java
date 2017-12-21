package testPack1;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	private String word;
	private List<Double> tf_idfList;
	private Double chiSquire;
	
	public Node(String word) {
		// TODO Auto-generated constructor stub
		this.word = word;
		tf_idfList = new ArrayList<>();
	}
	
	public void set_tf_idf_value(Double value){
		
		tf_idfList.add(value);
	}
	
	public List<Double> get_tf_idg_list(){
		
		return tf_idfList;
	}
	
	public String getWord(){
		
		return word;
	}
	
	public double get_total_tf_idf(){
		double total_tf_idf = 0;
		for (Double value : tf_idfList) {
			total_tf_idf =+ value;
		}
		
		return total_tf_idf;
	}
	
	public void set_chiSquire(Double value){
		
		chiSquire = value;
	}
	
	public Double get_chiSquire(){
		
		return chiSquire;
	}

}

package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Clustering {
	
	List<String> reportList = new ArrayList<>();
	List<String> reportListWithoutId = new ArrayList<>();
	List<String> idList = new ArrayList<>();
	HashMap<String, Integer> clusterMap= new HashMap<>();
	int numberOfCluster = 0;
	public Clustering(List<String> list) {
		// TODO Auto-generated constructor stub
		reportList = list;
		segregateIdFromReportList();
	}
	
	public void applyClustering(){
		
		int reportSize = reportListWithoutId.size();
		Cosine_Similarity cosine_Similarity = new Cosine_Similarity();
		
		for(int i = 0; i < reportSize; i++){
			
			if(!clusterMap.containsKey(idList.get(i))){
				clusterMap.put(idList.get(i), numberOfCluster++);
			}
			for(int j = i+1; j < reportSize; j++){
				
				if(cosine_Similarity.Cosine_Similarity_Score(reportListWithoutId.get(i), reportListWithoutId.get(j)) > 0.5){
					
					
					clusterMap.put(idList.get(j), clusterMap.get(idList.get(i)));
				}
			}
			
		}
	}
	
	public void segregateIdFromReportList(){
		
		for(int i = 0; i < reportList.size(); i++){
			String [] temp = reportList.get(i).split(" ", 2);
			idList.add(temp[0]);
			reportListWithoutId.add(temp[1]);
		}
		
	}
	

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		List<String> reportList = new ArrayList<>();
		Scanner input = new Scanner(new File("afterStemming.txt"));
		while (input.hasNextLine()){
   		 
  		     reportList.add(input.nextLine());
        }
		
		Clustering clustering = new Clustering(reportList);
		clustering.applyClustering();
		System.out.println(clustering.numberOfCluster);

	}

}

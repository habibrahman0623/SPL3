package test;

import java.util.Comparator;

public class MyComparator implements Comparator<Node> {
	
	
	@Override
	public int compare(Node o1, Node o2) {
		// TODO Auto-generated method stub
		Comparable val1 = o1.frequency;
		Comparable val2 = o2.frequency;
		return val2.compareTo(val1);
	}

}

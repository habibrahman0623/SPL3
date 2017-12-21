package testPack1;

import java.util.Comparator;
import java.util.Map;

public class MyComparator implements Comparator<Node> {
	
	
	@Override
	public int compare(Node o1, Node o2) {
		// TODO Auto-generated method stub
		Comparable val1 = o1.get_chiSquire();
		Comparable val2 = o2.get_chiSquire();
		return val2.compareTo(val1);
	}

}

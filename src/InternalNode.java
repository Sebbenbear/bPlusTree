import java.util.Arrays;

public class InternalNode implements Node {

	public static final int maxNodeKeys = 3;
	public int size = 0;

	public Node [] child = new Node[maxNodeKeys+2];
	public int [] keys = new int[maxNodeKeys+1];

	public InternalNode(){ }

	@Override
	public void print(int depth) {		
		System.out.println(this);
		
		for (int j=0;j<size;j++) {
			Util.printIndent("+++", depth+1);
			
			Node n = child[j];
			
			if (n == null) { System.out.println("<null>"); continue; }
			n.print(depth + 1);
		}
	}

}

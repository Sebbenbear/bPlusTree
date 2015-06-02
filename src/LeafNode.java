import java.util.Map.Entry;
import java.util.TreeMap;


public class LeafNode implements Node {

	public int maxLeafKeys = 3;
	public TreeMap <Integer, String> keysAndValues = new TreeMap<>();
	public LeafNode nextLeaf;
	
	/**
	 * Put the first value into the leadNode
	 * @param key
	 * @param value
	 */
	public LeafNode(int key, String value, LeafNode nextLeaf){
		keysAndValues.put(key,value);
		this.nextLeaf = nextLeaf;
	}

	public LeafNode(){
		
	}
	
	public int size(){
		return keysAndValues.size();
	}

	@Override
	public void print(int depth) {		
		System.out.println(this);
		for (Entry<Integer, String> p : keysAndValues.entrySet()) {
			Util.printIndent("+++", depth);
			
			System.out.println(p.getKey() + ": " + p.getValue());
		}
	}
	
}

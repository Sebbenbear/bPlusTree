import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
  Implements a B+ tree in which the keys are integers and the
  values are Strings (with maximum length 60 characters)
 */

public class BPlusTreeIntToString60 {

	private Node root;

	private final int KEY_SIZE = 60 * 4;
	private final int BLOCK = 1024; 

	/*
	 * Constructs index from an existing file
	 */
	public BPlusTreeIntToString60(){
		//TODO
	}

	/*
	 * Constructs empty tree.
	 */
	public BPlusTreeIntToString60(String fileName, int blockSize, int keySize, int valueSize){
		//TODO
	}

	/**
	 * Returns the String associated with the given key,
	 * or null if the key is not in the B+ tree.
	 */
	/*
	 * Find(key):
	 * if root is empty 
	 * 		return null
	 * else 
	 * 		return Find(key, root)
	 */
	public String find(int key){
		//System.out.println("Finding: " + key);
		if(root == null){
			return null;
		}
		else {
			return find(key,root);
		}
	}

	/*
	 *  if node is a leaf
			for i from 0 to node.size-1
				if key = node.keys[i] 
					return node.values[ i ]
			return null 
		if node is an internal node
			for i from 1 to node.size for
				if key < node.keys[ i ] return if return Find(key, getNode(node.child[i-1]))
			return Find(key, getNode(child[node.size] ))
	 */
	public String find(int key, Node node){	
		//System.out.println("Finding...");
		if(Util.nodeIsLeaf(node)){  							//initialize when the first child is added
			LeafNode leafNode = (LeafNode) node;
		//	System.out.println("Found key: " + key + ", value: " + leafNode.keysAndValues.get(key));
			return leafNode.keysAndValues.get(key);				//Return the String value if the key exists, otherwise return null
			
		}
		else {													//Otherwise it's an internal node
		//	System.out.println("searching internal nodes...");
			InternalNode internalNode = (InternalNode) node;
			for(int i = 1; i < internalNode.size; i++){			//for i from 1 to node.size
				if(key < internalNode.keys[i]){
					return find(key, internalNode.child[i-1]);	//get the next child
					
				}
			}
			return find(key, internalNode.child[internalNode.size-1]);  
		}
	}

	/**
	 * Stores the value associated with the key in the B+ tree.
	 * If the key is already present, replaces the associated value.
	 * If the key is not present, adds the key with the associated value
	 * @param key 
	 * @param value
	 * @return whether pair was successfully added.
	 */

	/*
	 * Add(key, value):
		if root is empty
			create new leaf, add key-value,
			root <- leaf
		else
			(newKey, rightChild) <- Add(key, value, root)
			if (newKey, rightChild) != null if
				node <- create new internal node
				node.size <- 1
				node.child[0] <- root
				node.keys[1] <- newKey
				node.child[1] <- rightChild
				root <- node
	 */
	public boolean put(int key, String value){
		System.out.println("Put: (" + key + ", " + value + ")");
		
		//ADD 1 Slides
		if(root == null){
			LeafNode leafNode = new LeafNode(key,value,null);
			root = leafNode;	 									//is not attached to anything
		}
		else {
			Pair<Integer,Node> pair = add(key,value,root);
			if(pair != null){				//if it was successfully added
				InternalNode node = new InternalNode();
				node.size = 2;
				node.child[0] = root;
				node.keys[1] = pair.newKey;
				node.child[1] = pair.rightChild;
				root = node;
				
				System.out.println("Root split");
			}
		}
		expectedKeys.add(key);
		integrityTest("end of put");
		return true;
	}
	
	
	public static void move (){
		
	}
	
	/*
	 * Add(key, value, node):
		if node is a leaf if
			if node.size < maxLeafKeys if
				insert key and value into leaf in correct place
				return null return
			else
				return SplitLeaf(key, value, node) return
		if node is an internal node if
			for i from 1 to node.size for
				if key < node.keys[i] if
					(k, rc) <- Add(key, value, node.child[i-1])
					if (k, rc)=null 
						return null
					else 
						return dealWithPromote(k,rc,node)
				(k, rc) <- Add(key, value, node.child[node.size])
		if (k, rc) = null 
			return null
		else 
			return else return dealWithPromote( k, rc, node)
	 */
	//Add (2)
	public Pair<Integer, Node> add(int key, String value, Node node){
		if(node == null) {
			throw new IllegalStateException("Node is nuullllll!!!!!");
		}
		
		if(Util.nodeIsLeaf(node)){
			LeafNode leafNode = (LeafNode) node;
			if(leafNode.size() < leafNode.maxLeafKeys){
				leafNode.keysAndValues.put(key, value);
				printState("add to leaf (no split)");
				return null;		//why?
			}
			else {
				System.out.println("splitting leaf");
				return splitLeaf(key,value,leafNode);
			}
		}
		else {
			InternalNode internalNode = (InternalNode) node;
			
			if(internalNode.size == 0) {
				throw new IllegalStateException("Node is empty!!!!!");
			}
			
			for(int i = 1; i < internalNode.size; i++){
				if(key < internalNode.keys[i]){
					Pair<Integer, Node> pair = add(key, value, internalNode.child[i-1]);
					printState("added child: " + pair);
					if(pair == null){
						return null;
					}
					else {
						return dealWithPromote(pair, internalNode);
					}
				}
			}
			Pair<Integer, Node> pair = add(key, value, internalNode.child[internalNode.size-1]);
			printState("added child: " + pair);
			if(pair == null){
				return null;
			}
			else {
				return dealWithPromote(pair, internalNode);
			}
		}
	}
	
	/*
	 * SplitLeaf(key, value, node):
			insert key and value into leaf in correct place (spilling over end)
			sibling <- create new leaf
			mid	<- (node.size+1)/2
			move keys and values from mid ... size out of node into sibling.
			sibling.next <- node.next 
			node.next <- sibling
			return (sibling.keys[0] , sibling)
	 */
	
	public Pair<Integer, Node> splitLeaf(int key, String value, LeafNode node){
		node.keysAndValues.put(key, value);
		LeafNode sibling = new LeafNode(); 		//shouldn't need key and value to make leafnode
		int mid = (node.size()+1)/2;
		moveToLeaf(node, sibling, mid);
		//move keys and values from mid ... size out of node into sibling		
		sibling.nextLeaf = node.nextLeaf;
		node.nextLeaf = sibling;		
		return new Pair<Integer, Node>(sibling.keysAndValues.firstKey(), sibling);
	}
	
	/**
	 * Moves half the contents from one leafnode to another
	 * @param fromNode			The node to shift half of the contents from
	 * @param destinationNode	The node to insert the values into
	 * @param mid	The middle index of the keyset in the fromNode.
	 */
	private static void moveToLeaf(LeafNode fromNode, LeafNode destinationNode, int mid) {
		List<Integer> list = new ArrayList<>(fromNode.keysAndValues.keySet()); // Keyset is an ordered set so this is safe
		//for every value from mid to size
		List<Integer> subList = list.subList(mid, list.size()); //take out all the values from the sublist, and put them in the list.
		
		//remove the items that were in that list already
		for(Integer i : subList){
			destinationNode.keysAndValues.put(i, fromNode.keysAndValues.remove(i));		//put the values from the fromNode map into the destinationNode map
		}
	}
	
	private static void moveToInternal(InternalNode fromNode, InternalNode destinationNode, int mid){
		//move node.keys[mid+1... node.size] to sibling.node [1... node.size-mid]
		//move node.child[mid ... node.size] to sibling.child [0... node.size-mid]
		//promoteKey <- node.keys[mid]
		//remove node.keys[mid]
		
		int before = fromNode.size;
		
		destinationNode.size = fromNode.size - mid;
		fromNode.size = mid;
		
		Util.moveSecondHalf(fromNode.keys, destinationNode.keys, mid+1, 1);
		Util.moveSecondHalf(fromNode.child, destinationNode.child, mid, 0);
		
		System.out.println("Did a split, size before: " + before + " sizes after: " + fromNode.size + " " + destinationNode.size);
		
		if(fromNode.size == 1 || destinationNode.size == 1) {
			System.out.println("Mid point was "+mid);
			throw new RuntimeException("Invalid internal node size after split");
		}
	}
	
	/*
	 * DealWithPromote( newKey, rightChild, node):
		if (newKey, rightChild) = null 
			return null
		if newKey > node.keys[node.size]
			insert newKey at node.keys[node.size+1]
			insert rightChild at node.child[node.size+1]
		else 
			for i from 1 to node.size
				if newKey < node.keys[i]
					insert newKey at node.keys[i]
					insert rightChild at node.child[i]
		if size <= maxNodeKeys return if return null
		sibling <- create new node
		mid <- (size/2) +1
		move node.keys[mid+1... node.size] to sibling.node [1... node.size-mid]
		move node.child[mid ... node.size] to sibling.child [0... node.size-mid]
		promoteKey <- node.keys[mid]
		remove node.keys[mid]
		return (promoteKey, sibling)
	 */
	

	public Pair<Integer, Node> dealWithPromote(Pair <Integer, Node> pair, InternalNode internalNode){
		if(pair == null){
			System.out.println("Promote of null pair... wth?");
			return null;
		}
		
		System.out.println("Promoting: " + internalNode + ", key: " + pair.newKey + ", node: " +  pair.rightChild);
		
		if(pair.newKey > internalNode.keys[internalNode.size-1]){
			internalNode.keys[internalNode.size] = pair.newKey;
			internalNode.child[internalNode.size] = pair.rightChild;
			internalNode.size++;
		}
		else {
			for(int i = 1; i < internalNode.size; i++){
				if(pair.newKey < internalNode.keys[i]){
					Util.insert(internalNode.keys, pair.newKey, i);
					Util.insert(internalNode.child, pair.rightChild, i);
					
					internalNode.size++;
					break;
				}
			}
		}
		
		printState("promote: "+(internalNode.size <= InternalNode.maxNodeKeys));
		
		if(internalNode.size <= InternalNode.maxNodeKeys){
			return null;
		}
		InternalNode sibling = new InternalNode();
		int mid = internalNode.size/2;
		moveToInternal(internalNode, sibling, mid);
		int promoteKey = internalNode.keys[mid];	//the key to be promoted up a level
		internalNode.keys[mid] = 0;  //remove the node keys mid
		return new Pair<>(promoteKey, sibling);
	}
	private List<Integer> expectedKeys = new ArrayList<Integer>();
	private void integrityTest(String source) {
//		printState(source);
//		for (Integer k : expectedKeys) {
//			if (find(k) == null) {
//				throw new RuntimeException("Unable to find a key (" + k + ") that we placed!");
//			}
//		}
	}
	private void printState(String source) {
//		System.out.println("=== "+source+" ===");
//		System.out.println("=== STATE ===");
//		if (root == null) {
//			System.out.println("<null>");
//		} else {
//			root.print(0);
//		}
	}
}

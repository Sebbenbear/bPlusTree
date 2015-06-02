/**
  Implements a B+ tree in which the keys  are Strings (with
  maximum length 60 characters) and the values are integers 
*/

public class BPlusTreeString60toInt {
	
	private final int KEY_SIZE = 60 * 4;
	private final int BLOCK = 1024; 
	
    /**
     * Returns the integer associated with the given key,
     * or null if the key is not in the B+ tree.
     */
    public Integer find(String key){
	//YOUR CODE HERE
    	//
    	//make sure the tree takes a size in the constructor
    	//have fields nodeCapacity and leafCapacity - make a number up.
    	//or pass it into the constructor
    	//searching for value associated with a given key
    	//
    	//left = <
    	//right is >=
    	//
	return null;
    }


    /**
     * Stores the value associated with the key in the B+ tree.
     * If the key is already present, replaces the associated value.
     * If the key is not present, adds the key with the associated value
     * @param value
     * @param key 
     * @return whether pair was successfully added.
     */
    public boolean put(String value, int key){
	//YOUR CODE HERE
	return false;
    }

}

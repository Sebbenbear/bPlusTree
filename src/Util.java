public class Util {

	public static boolean nodeIsLeaf(Node node){
		if(node instanceof LeafNode){
			return true;
		}
		return false;
	}
	
	/**
	 * Move all the elements from index mid (inclusive) in from to the array to starting at index dst
	 * @param from the array to move from
	 * @param to the array to move to
	 * @param mid the point from which to start moving items from
	 * @param dst the index from which to start moving items to
	 */
	public static <T> void moveSecondHalf(T[] from, T[] to, int mid, int dst) {
		int indexInTo = dst;
		
		for(int indexInFrom = mid; indexInFrom < from.length; indexInFrom++) {
			to[indexInTo] = from[indexInFrom];
			from[indexInFrom] = null;
			
			indexInTo++;
		}
	}
	
	// TODO: Remove once b+tree is generic as well
	public static void moveSecondHalf(int[] from, int[] to, int mid, int dst) {
		int indexInTo = dst;
		
		for(int indexInFrom = mid; indexInFrom < from.length; indexInFrom++) {
			to[indexInTo] = from[indexInFrom];
			from[indexInFrom] = 0;
			
			indexInTo++;
		}
	}
	
	public static <T> void insert(T[] array, T value, int index) {
		for(int i=array.length-1; i > index; i--) {
			array[i] = array[i-1];
		}
		
		array[index] = value;
	}
	
	// TODO: Remove once b+tree is generic as well
	public static void insert(int[] array, int value, int index) {
		for(int i=array.length-1; i > index; i--) {
			array[i] = array[i-1];
		}
		
		array[index] = value;
	}

	public static void printIndent(String string, int depth) {
		for(int i=0; i<depth; i++) {
			System.out.print(string);
		}
		
		if(depth != 0) {
			System.out.print(" ");
		}
	}
}

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * BPlusTree implementation.
 * This contains the methods to insert, search and search range for the given key or key/value pair.
 */
public class BplusTree {

	public NodeStruct root;
	public static int Order=0;
	
	// Set the Order for the tree.
	BplusTree(int deg){
		this.Order=deg;
	}
	
	/**
	 * Search the values for a given key.
	 * 
	 * @param key
	 * @return List<values>
	 */
	public String search(double key) {
		// Search for the leaf node that will contain key
		LeafNodeStruct leaf = getLeafNode(root, key);
		List<String> res=new ArrayList<>();
 
		// Search for value within the leaf
		for (int i = 0; i < leaf.values.size(); i++){
			String[] val_obj=leaf.values.get(i).split("#");
			double orig_key=Double.parseDouble(val_obj[0]);
			if (orig_key == key){
				res.add(val_obj[1]); 
			}
		}
		int len=res.size();
		String out="";
		for(int i=0;i<len;i++){
			if(i==len-1) out+=res.get(i);
			else out+=res.get(i)+",";
		}
		return out.length()>0?out:"Null"; 
	}
	
	/**
	 * Search the values for a given range of keys.
	 * 
	 * @param key1
	 * @param key2
	 * @return List<values> within the range of key1 and key2
	 */
	public String search(double key1,double key2) {
		// Search the leaf node that will contain the key1 that is the minimum key from where the range starts.
		LeafNodeStruct leafNode = getLeafNode(root, key1);
		List<String> res=new ArrayList<>();
 
		// Search the values within the given key range.
		while(leafNode!=null){
			for (int i = 0; i < leafNode.values.size(); i++){
				String[] val_obj=leafNode.values.get(i).split("#");
				double orig_key=Double.parseDouble(val_obj[0]);
				if (orig_key>=key1 && orig_key<=key2){
					res.add("("+orig_key+","+val_obj[1]+")"); 
				}
			}
			leafNode=leafNode.nextLeafPtr;
		}
		
		int len=res.size();
		String out="";
		for(int i=0;i<len;i++){
			if(i==len-1) out+=res.get(i);
			else out+=res.get(i)+",";
		}
		return out.length()>0?out:"Null"; 
	}
	
	/**
	 * Insert key/value pair into the tree.
	 * Also stores the duplicate values.
	 * 
	 * @param key
	 * @param value
	 */
	public void insert(double key, String value) {
		//Check if the key is already inserted
		if(updateExisting(key,value)) return;
		
		// Initialize the tree
		if (root == null){
			root = new LeafNodeStruct(key, value);
		}
		
		//Check the overflow condition
		NodeTuple isSplitNode = insertCheck(root, key, value);
		if (isSplitNode != null){
			root = new IndexNodeStruct(isSplitNode.getKey(), root, isSplitNode.getNode());
		}
	}

	/**
	 * Check if key is already inserted and if yes append the value to its arrayList.
	 * 
	 * @param key
	 * @param value
	 */
	public boolean updateExisting(double key,String value) {
		LeafNodeStruct leaf = getLeafNode(root, key);
		if(leaf==null) return false;
		for (int i = 0; i < leaf.keys.size(); i++){
			if (leaf.keys.get(i) == key){
				leaf.values.add(key+"#"+value);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * The overflow condition approaches, split the node and insert the split node a level above than it 
	 * and keep on satisfying the overflow condition until satisfied at each above level.
	 */
	private NodeTuple insertCheck(NodeStruct node, double key, String value){
		NodeTuple isSplit = null; 
		if (node.isLeaf){
			LeafNodeStruct leaf = (LeafNodeStruct) node; 
			leaf.inorderInsert(key, value);
			if (leaf.isOverflowed()){
				NodeTuple rightSplit = leafNodeSplit(leaf);
				return rightSplit;
			}
			return null; 
		}
		else {
			IndexNodeStruct indexNode = (IndexNodeStruct) node; 
			if (key >= node.keys.get(indexNode.keys.size() - 1))
				isSplit = insertCheck(indexNode.childrenList.get(indexNode.childrenList.size() - 1), key, value); 
			else if (key < node.keys.get(0)) 
				isSplit = insertCheck(indexNode.childrenList.get(0), key, value);
			else {
				// To insert as the middle child.
				for (int i = 0; i < indexNode.childrenList.size(); i++){
					if (indexNode.keys.get(i) > key){
						isSplit = insertCheck(indexNode.childrenList.get(i), key, value);
						break;
					}
				}
			}
		}
		if (isSplit != null){
			IndexNodeStruct indexNode = (IndexNodeStruct)node;
			
			// To insert the index that has overflowed.
			double splitKey = isSplit.getKey();
			int indexParent = indexNode.keys.size();
			if (splitKey < indexNode.keys.get(0)){
				indexParent = 0; 
			} else if (splitKey > indexNode.keys.get(indexNode.keys.size() -1)){
				indexParent = indexNode.childrenList.size(); 
			} else {
				for (int i = 0; i < indexNode.keys.size(); i++){
					if (indexNode.keys.get(i) > splitKey){
						indexParent = i;
						break;
					}
				}
			}
			
			indexNode.inorderInsert(isSplit, indexParent);
			if (indexNode.isOverflowed()){
				NodeTuple rightSplit = splitIndexNode(indexNode);
				return rightSplit;
			}
			return null;
		}
		return isSplit;
	}

	/**
	 * Points the leftLeafNode to its next leaf found so far. 
	 * @param leftLeaf 
	 * @param rightLeaf
	 */
	private void siblingPtrs(LeafNodeStruct leftLeafNode, LeafNodeStruct rightLeafNode) {
		if (leftLeafNode.nextLeafPtr != null){
			rightLeafNode.nextLeafPtr = leftLeafNode.nextLeafPtr;
		}
		leftLeafNode.nextLeafPtr = rightLeafNode; 	
	}

	/**
	 * Split the leaf node and return the splitting key and the new right node found as an Entry<splitKey, rightNode>
	 * 
	 * @param leaf
	 * @return the key and right node pair as an Entry
	 */
	public NodeTuple leafNodeSplit(LeafNodeStruct leafNode) {
		 
		ArrayList<Double> keysRight = new ArrayList<Double>(); 
		ArrayList<String> valuesRight = new ArrayList<String>();
		int mid=leafNode.keys.size()/2;
		
		keysRight.addAll(leafNode.keys.subList(mid, leafNode.keys.size()));
		setRightValues(keysRight,valuesRight,leafNode.values);
		
		// To get the leftLeaf List
		leafNode.keys.subList(mid, leafNode.keys.size()).clear();
		leafNode.values.removeAll(valuesRight);
		
		LeafNodeStruct rightLeaf = new LeafNodeStruct(keysRight, valuesRight);
		
		// To point to the next sibling of the node
		siblingPtrs(leafNode, rightLeaf);

		return new NodeTuple(rightLeaf.keys.get(0), rightLeaf);

	}

	/** 
	 * Find the values that are paired with the keys in the rightChild.
	 * @param rightKeys
	 * @param rightValues
	 * @param leaf_values
	 */
	private void setRightValues(ArrayList<Double> rightKeys, ArrayList<String> rightValues, ArrayList<String> leafValues) {
		for(double key:rightKeys){
			for(String value:leafValues){
				String[] val_obj=value.split("#");
				double key_gen=Double.parseDouble(val_obj[0]);
				if(key==key_gen){
					rightValues.add(value);
				}
			}
		}
	}


	/**
	 * Split the indexNode and return the splitting key and the new right node found as an Entry<splitKey, rightNode>
	 * 
	 * @param indexNode
	 * @return new key and node pair as an Entry
	 */
	public NodeTuple splitIndexNode(IndexNodeStruct indexNode) { 
		
		ArrayList<Double> rightKeys = new ArrayList<Double>(); 
		ArrayList<NodeStruct> childrenRight = new ArrayList<NodeStruct>();
		int mid=indexNode.keys.size()/2;
		rightKeys.addAll(indexNode.keys.subList(mid+1, indexNode.keys.size()));
		childrenRight.addAll(indexNode.childrenList.subList(mid+1, indexNode.childrenList.size())); 
		
		// push up the new index
		IndexNodeStruct rightNode = new IndexNodeStruct(rightKeys, childrenRight);
		NodeTuple split = new NodeTuple(indexNode.keys.get(mid), rightNode);

		// To get the left List
		indexNode.keys.subList(mid, indexNode.keys.size()).clear();
		indexNode.childrenList.subList(mid+1, indexNode.childrenList.size()).clear();
		
		return split;
	}
	
	/**
	 * Returns the LeafNode where the given key has to be inserted to
	 * @param key
	 * 	
	 * @return the LeafNode the key is to be inserted to
	 */
	private LeafNodeStruct getLeafNode(NodeStruct nodeStruct, double key){
		// If the node is null
		if (nodeStruct == null)
			return null; 
		
		if (nodeStruct.isLeaf){
			// LeafNode is found
			return (LeafNodeStruct) nodeStruct;
		}
		else {
			// When node is the IndexNode
			IndexNodeStruct indexNode = (IndexNodeStruct) nodeStruct;
			if (key >= nodeStruct.keys.get(nodeStruct.keys.size() - 1)) {
				return getLeafNode(indexNode.childrenList.get(indexNode.childrenList.size() - 1), key);
			} else if (key < nodeStruct.keys.get(0)){
				return getLeafNode(indexNode.childrenList.get(0), key);
			} else {
				ListIterator<Double> iter = indexNode.keys.listIterator();
				while (iter.hasNext()){
					if (iter.next() > key){
						return getLeafNode(indexNode.childrenList.get(iter.previousIndex()), key); 
					}
				}
			}
		}
		return null;
	}
}

import java.util.ArrayList;
import java.util.List;

/**
 * This contains the Index Node Structure.
 */
public class IndexNodeStruct extends NodeStruct {

	protected ArrayList<NodeStruct> childrenList; 

	public IndexNodeStruct(double key, NodeStruct child1, NodeStruct child2) {
		isLeaf = false;
		keys = new ArrayList<Double>();
		keys.add(key);
		childrenList = new ArrayList<NodeStruct>();
		childrenList.add(child1);
		childrenList.add(child2);
	}

	public IndexNodeStruct(List<Double> newKeys, List<NodeStruct> newChildren) {
		isLeaf = false;

		keys = new ArrayList<Double>(newKeys);
		childrenList = new ArrayList<NodeStruct>(newChildren);

	}

	/**
	 * Insert the node at the given index in a way that the list is sorted
	 * @param nodeTuple
	 * @param idx
	 */
	public void inorderInsert(NodeTuple nodeTuple, int idx) {
		double key = nodeTuple.getKey();
		NodeStruct child = nodeTuple.getNode();
		if (idx >= keys.size()) {
			keys.add(key);
			childrenList.add(child);
		} else {
			keys.add(idx, key);
			childrenList.add(idx+1, child);
		}
	}

}

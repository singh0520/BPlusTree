import java.util.ArrayList;

/**
 * This contains the Generic Node Structure mentioning the overflow condition.
 */
public class NodeStruct {
	protected boolean isLeaf;
	protected ArrayList<Double> keys;

	public boolean isOverflowed() {
		return keys.size() > BplusTree.Order - 1;
	}
}

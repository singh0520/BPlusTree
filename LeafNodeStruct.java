import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * This contains the Leaf Node Structure.
 */
public class LeafNodeStruct extends NodeStruct {
	protected ArrayList<String> values;
	protected LeafNodeStruct nextLeafPtr;

	public LeafNodeStruct(double keyFirst, String valueFirst) {
		isLeaf = true;
		keys = new ArrayList<Double>();
		values = new ArrayList<String>();
		keys.add(keyFirst);
		values.add(keyFirst+"#"+valueFirst);

	}

	public LeafNodeStruct(List<Double> newKeys, List<String> newValues) {
		isLeaf = true;
		keys = new ArrayList<Double>(newKeys);
		values = new ArrayList<String>(newValues);

	}

	/**
	 * Insert the key and value in sorted order.
	 * 
	 * @param key
	 * @param value
	 */
	public void inorderInsert(double key, String value) {
		if (key < keys.get(0)) {
			keys.add(0, key);
			values.add(0, key+"#"+value);
		} else if (key > keys.get(keys.size() - 1)) {
			keys.add(key);
			values.add(key+"#"+value);
		} else {
			ListIterator<Double> iter = keys.listIterator();
			while (iter.hasNext()) {
				if (iter.next() > key) {
					int pos = iter.previousIndex();
					keys.add(pos, key);
					values.add(pos, key+"#"+value);
					break;
				}
			}

		}
	}

}

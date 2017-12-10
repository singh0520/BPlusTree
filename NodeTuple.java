public class NodeTuple {
	private NodeStruct node;
	private Double key;
	
	NodeTuple(Double key,NodeStruct node){
		this.node=node;
		this.key=key;
	}

	public NodeStruct getNode() {
		return node;
	}

	public void setNode(NodeStruct node) {
		this.node = node;
	}

	public Double getKey() {
		return key;
	}

	public void setKey(Double key) {
		this.key = key;
	}
}

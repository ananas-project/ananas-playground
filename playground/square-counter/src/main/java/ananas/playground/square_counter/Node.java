package ananas.playground.square_counter;

public class Node {

	private int viewX;
	private int viewY;
	private final int logicX;
	private final int logicY;
	private final NodeMap map;

	private NodeKey left;
	private NodeKey right;
	private NodeKey head;
	private NodeKey foot;

	public Node(NodeMap map, int x, int y) {
		this.map = map;
		this.logicX = x;
		this.logicY = y;
	}

	public int getViewX() {
		return viewX;
	}

	public void setViewX(int viewX) {
		this.viewX = viewX;
	}

	public int getViewY() {
		return viewY;
	}

	public void setViewY(int viewY) {
		this.viewY = viewY;
	}

	public int getLogicX() {
		return logicX;
	}

	public int getLogicY() {
		return logicY;
	}

	public NodeMap getMap() {
		return map;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(this.logicX);
		sb.append(',');
		sb.append(this.logicY);
		sb.append(')');
		return sb.toString();
	}

	public NodeKey getLeft() {
		return left;
	}

	public void setLeft(NodeKey left) {
		this.left = left;
	}

	public NodeKey getRight() {
		return right;
	}

	public void setRight(NodeKey right) {
		this.right = right;
	}

	public NodeKey getHead() {
		return head;
	}

	public void setHead(NodeKey head) {
		this.head = head;
	}

	public NodeKey getFoot() {
		return foot;
	}

	public void setFoot(NodeKey foot) {
		this.foot = foot;
	}

}

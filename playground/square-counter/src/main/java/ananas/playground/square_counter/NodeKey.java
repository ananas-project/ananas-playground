package ananas.playground.square_counter;

public class NodeKey {

	private final Node head;
	private final Node foot;
	private final Node left;
	private final Node right;

	private boolean linked;

	public NodeKey(Node h, Node r, Node f, Node l) {
		this.head = h;
		this.right = r;
		this.foot = f;
		this.left = l;
	}

	public boolean isLinked() {
		return linked;
	}

	public void setLinked(boolean linked) {
		this.linked = linked;
	}

	public Node getHead() {
		return head;
	}

	public Node getFoot() {
		return foot;
	}

	public Node getLeft() {
		return left;
	}

	public Node getRight() {
		return right;
	}

	public static void linkLeftRight(Node left, Node right) {
		if (left == null || right == null)
			return;
		NodeKey nk = new NodeKey(null, right, null, left);
		right.setLeft(nk);
		left.setRight(nk);
	}

	public static void linkHeadFoot(Node head, Node foot) {
		if (head == null || foot == null)
			return;
		NodeKey nk = new NodeKey(head, null, foot, null);
		head.setFoot(nk);
		foot.setHead(nk);
	}

}

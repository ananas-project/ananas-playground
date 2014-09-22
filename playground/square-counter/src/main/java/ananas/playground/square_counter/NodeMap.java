package ananas.playground.square_counter;

import java.util.ArrayList;
import java.util.List;

public class NodeMap {

	private final int width;
	private final int height;
	private final List<Node> nodeList;
	private final Node[] nodeArray;

	public NodeMap(int w, int h) {
		this.width = w;
		this.height = h;
		this.nodeArray = new Node[w * h];
		this.nodeList = this.__create(w, h);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private List<Node> __create(int w, int h) {

		List<Node> list = new ArrayList<Node>();
		for (int x = w - 1; x >= 0; x--) {
			for (int y = h - 1; y >= 0; y--) {
				Node node = new Node(this, x, y);
				list.add(node);
				this.__put(node);
			}
		}
		for (Node node : list) {
			this.__head(node);
			this.__right(node);
			this.__foot(node);
			this.__left(node);
		}
		return list;
	}

	private void __put(Node node) {
		int x = node.getLogicX();
		int y = node.getLogicY();
		int index = this.__index_by_xy(x, y);
		Node[] array = this.nodeArray;
		if (0 <= index && index < array.length) {
			this.nodeArray[index] = node;
		}
	}

	public Node get(int x, int y) {
		int index = this.__index_by_xy(x, y);
		Node[] array = this.nodeArray;
		if (0 <= index && index < array.length) {
			return this.nodeArray[index];
		}
		return null;
	}

	private int __index_by_xy(int x, int y) {
		if ((0 <= x && x < this.width) && (0 <= y && y < this.height)) {
			return ((this.width * y) + x);
		}
		return -1;
	}

	private void __left(Node node) {
		int x = node.getLogicX() - 1;
		int y = node.getLogicY();
		Node left = this.get(x, y);
		NodeKey.linkLeftRight(left, node);
	}

	private void __right(Node node) {
		int x = node.getLogicX() + 1;
		int y = node.getLogicY();
		Node right = this.get(x, y);
		NodeKey.linkLeftRight(node, right);
	}

	private void __head(Node node) {
		int x = node.getLogicX();
		int y = node.getLogicY() - 1;
		Node head = this.get(x, y);
		NodeKey.linkHeadFoot(head, node);
	}

	private void __foot(Node node) {
		int x = node.getLogicX();
		int y = node.getLogicY() + 1;
		Node foot = this.get(x, y);
		NodeKey.linkHeadFoot(node, foot);
	}

	public List<Node> getNodeList() {
		return nodeList;
	}

	public void setViewSize(int w, int h) {
		int wpn = w / (this.width + 2);
		int hpn = h / (this.height + 2);
		int pn = Math.min(wpn, hpn);
		for (Node node : this.nodeList) {
			node.setViewX(pn * (node.getLogicX() + 1));
			node.setViewY(pn * (node.getLogicY() + 1));
		}
	}

}

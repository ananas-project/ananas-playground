package ananas.playground.square_counter;

import java.util.List;

public class NodeMapSquareCounter {

	private int _count;

	public void count(NodeMap nm) {
		List<Node> nodes = nm.getNodeList();
		for (Node node : nodes) {
			for (int w = 1;; w++) {
				if (this.isLineRight(node, w)) {
					if (this.isSquare(node, w)) {
						this.addToResult(node, w);
					}
				} else {
					break;
				}
			}
		}
	}

	private void addToResult(Node node, int w) {
		int cnt = (this._count++);
		if (cnt == 0) {
			System.out.println();
			System.out.println("count square:");
		}
		System.out.println(cnt + ". " + node + "," + w + "x" + w);
	}

	private boolean isSquare(Node node, int w) {
		node = this.toRight(node, w);
		if (node == null)
			return false;
		node = this.toFoot(node, w);
		if (node == null)
			return false;
		node = this.toLeft(node, w);
		if (node == null)
			return false;
		node = this.toHead(node, w);
		if (node == null)
			return false;
		return true;
	}

	private boolean isLineRight(Node node, int w) {
		Node n2 = this.toRight(node, w);
		return (n2 != null);
	}

	private Node toRight(Node node, int w) {
		for (int i = 0; i < w; i++) {
			NodeKey nk = node.getRight();
			if (nk == null)
				return null;
			node = nk.getRight();
			if (!nk.isLinked())
				return null;
			if (node == null)
				return null;
		}
		return node;
	}

	private Node toLeft(Node node, int w) {
		for (int i = 0; i < w; i++) {
			NodeKey nk = node.getLeft();
			if (nk == null)
				return null;
			node = nk.getLeft();
			if (!nk.isLinked())
				return null;
			if (node == null)
				return null;
		}
		return node;
	}

	private Node toHead(Node node, int w) {
		for (int i = 0; i < w; i++) {
			NodeKey nk = node.getHead();
			if (nk == null)
				return null;
			node = nk.getHead();
			if (!nk.isLinked())
				return null;
			if (node == null)
				return null;
		}
		return node;
	}

	private Node toFoot(Node node, int w) {
		for (int i = 0; i < w; i++) {
			NodeKey nk = node.getFoot();
			if (nk == null)
				return null;
			node = nk.getFoot();
			if (!nk.isLinked())
				return null;
			if (node == null)
				return null;
		}
		return node;
	}

}

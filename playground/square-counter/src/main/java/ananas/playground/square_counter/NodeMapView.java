package ananas.playground.square_counter;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JPanel;

public class NodeMapView extends JPanel implements MouseListener,
		MouseMotionListener {

	private static final long serialVersionUID = 4343796844389632243L;

	private final NodeMap _nodeMap = new NodeMap(10, 10);
	private Node _hot_node;
	private Node _node_1;
	private Node _node_2;
	private boolean _do_linked;

	public NodeMapView() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public NodeMap getNodeMap() {
		return _nodeMap;
	}

	@Override
	public void paint(Graphics g) {

		final int w = this.getWidth();
		final int h = this.getHeight();
		g.setColor(this.getBackground());
		g.fillRect(0, 0, w, h);
		g.setColor(this.getForeground());
		Node hot = this._hot_node;

		this._nodeMap.setViewSize(w, h);
		for (Node node : this._nodeMap.getNodeList()) {

			int x = node.getViewX();
			int y = node.getViewY();

			if (node == hot) {
				g.fillRect(x - 5, y, 10, 10);
			} else {
				g.drawRect(x - 5, y, 10, 10);
			}

			this.__paint_nk(g, node.getLeft());
			this.__paint_nk(g, node.getRight());
			this.__paint_nk(g, node.getHead());
			this.__paint_nk(g, node.getFoot());

		}

		Node node1 = this._node_1;
		Node node2 = this._node_2;
		if (node1 != null && node2 != null) {
			g.drawLine(node1.getViewX(), node1.getViewY(), node2.getViewX(),
					node2.getViewY());
		}

		String op = this._do_linked ? "link" : "clear";
		int gap = 5;
		g.drawString("op:" + op, gap, h - gap);

	}

	private void __paint_nk(Graphics g, NodeKey nk) {
		if (nk == null)
			return;
		if (!nk.isLinked())
			return;
		Node n1 = nk.getLeft();
		Node n2 = nk.getRight();
		if (n1 == null) {
			n1 = nk.getHead();
			n2 = nk.getFoot();
		}
		g.drawLine(n1.getViewX(), n1.getViewY(), n2.getViewX(), n2.getViewY());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();
		this._node_1 = this.find(x, y);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();
		final Node node1 = this._node_1;
		final Node node2 = this.find(x, y);

		this.__set_node_key(node1, node2);

		this.setHotNode(null);
		this._node_1 = null;
		this._node_2 = null;

		System.out.println("link: " + node1 + "-" + node2);

	}

	private void __set_node_key(Node node1, Node node2) {

		if (node1 == null || node2 == null)
			return;

		boolean linked = this._do_linked;

		final NodeMap map = node1.getMap();
		final int x1 = node1.getLogicX();
		final int y1 = node1.getLogicY();
		final int x2 = node2.getLogicX();
		final int y2 = node2.getLogicY();
		if (x1 == x2 && y1 == y2) {
			// skip
		} else if (x1 == x2) {
			final int y0 = Math.min(y1, y2);
			final int y3 = Math.max(y1, y2);
			for (int y = y0; y < y3; y++) {
				Node node = map.get(x1, y);
				node.getFoot().setLinked(linked);
			}
		} else if (y1 == y2) {
			final int x0 = Math.min(x1, x2);
			final int x3 = Math.max(x1, x2);
			for (int x = x0; x < x3; x++) {
				Node node = map.get(x, y1);
				node.getRight().setLinked(linked);
			}
		} else {
			// skip
		}
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();
		Node node = this.find(x, y);
		this._node_2 = node;
		this.setHotNode(node);
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();
		Node node = this.find(x, y);
		this.setHotNode(node);
	}

	private void setHotNode(Node node) {
		if (node != null) {
			// System.out.println("hot:" + node);
		}
		NodeMapView.this._hot_node = node;
		NodeMapView.this.repaint();
	}

	private Node find(int x, int y) {
		return this.find(x, y, 10);
	}

	private Node find(int x, int y, int r) {
		List<Node> list = NodeMapView.this._nodeMap.getNodeList();
		for (Node node : list) {
			int x2 = node.getViewX();
			int y2 = node.getViewY();
			int xx = x - x2;
			int yy = y - y2;
			int rr = r * r;
			xx = xx * xx;
			yy = yy * yy;
			if (rr > (xx + yy)) {
				return node;
			}
		}
		return null;
	}

	public boolean isLinkMode() {
		return _do_linked;
	}

	public void setLinkMode(boolean linkMode) {
		this._do_linked = linkMode;
		this.repaint();
	}

}

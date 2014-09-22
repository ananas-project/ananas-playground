package ananas.playground.square_counter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 4343796844389632243L;

	private final NodeMapView _view_nodes;

	interface Command {
		String calculate = "calc";
		String draw = "draw";
		String clear = "clear";
	}

	public MainFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(640, 480);
		this.setTitle(this.getClass() + "");

		JMenu menu = new JMenu("menu");
		menu.add(this.newMenuItem(Command.calculate, null));
		menu.add(this.newMenuItem(Command.draw, null));
		menu.add(this.newMenuItem(Command.clear, null));
		JMenuBar mb = new JMenuBar();
		mb.add(menu);
		this.setJMenuBar(mb);

		this._view_nodes = new NodeMapView();
		this.add(this._view_nodes);

	}

	private JMenuItem newMenuItem(String cmd, String text) {
		if (cmd == null)
			cmd = "undef";
		if (text == null)
			text = cmd;
		JMenuItem it = new JMenuItem("it");
		it.addActionListener(this);
		it.setActionCommand(cmd);
		it.setText(text);
		return it;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd == null) {
		} else if (cmd.equals(Command.calculate)) {
			this.doCalculate();
		} else if (cmd.equals(Command.draw)) {
			this._view_nodes.setLinkMode(true);
		} else if (cmd.equals(Command.clear)) {
			this._view_nodes.setLinkMode(false);
		} else {
		}
	}

	private void doCalculate() {
		NodeMap nm = this._view_nodes.getNodeMap();
		NodeMapSquareCounter nmsc = new NodeMapSquareCounter();
		nmsc.count(nm);
	}

}

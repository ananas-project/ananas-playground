package ananas.app.dlm2kml.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ananas.app.dlm2kml.Main_for_Dlm2Kml;

public class DLM2KML_MainFrame extends JFrame {

	private static final long serialVersionUID = -212486718905877263L;

	interface CMD {

		String file_open = "file_open";

	}

	public static void main(String[] arg) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				UICore uiCore = new UICore();
				DLM2KML_MainFrame frame = new DLM2KML_MainFrame();
				frame.onCreate(uiCore);
				frame.setVisible(true);

			}
		});

	}

	protected void onCreate(UICore uiCore) {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(this.getClass().getName());
		this.setSize(640, 480);

		// menu
		JMenuBar mb = this.createMenuBar(new JMenuBar());
		this.setJMenuBar(mb);
	}

	private JMenuBar createMenuBar(JMenuBar mb) {
		{
			JMenu menu = new JMenu("File");
			mb.add(menu);
			this.addMenuItem(menu, CMD.file_open, null);
		}
		return mb;
	}

	private void addMenuItem(JMenu menu, String id, String text) {
		if (id == null)
			id = "(default_id)";
		if (text == null)
			text = id;
		JMenuItem item = new JMenuItem();
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String cmd = e.getActionCommand();
				onCommand(cmd);
			}
		});
		item.setActionCommand(id);
		item.setText(text);
		menu.add(item);
	}

	public void onCommand(String cmd) {
		if (cmd == null) {
			return;
		} else if (cmd.equals(CMD.file_open)) {
			this.onCommandFileOpen();
		} else {
			return;
		}
	}

	private void onCommandFileOpen() {
		JFileChooser fc = new JFileChooser();
		int rlt = fc.showOpenDialog(this);
		if (rlt == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			this.doFileOpen(file);
		}
	}

	private void doFileOpen(File file) {
		(new Main_for_Dlm2Kml()).todo(file);
	}

}

package ananas.app.rfc_tw.gui.base;

import java.awt.Component;

import javax.swing.JMenuBar;

public interface IViewController {

	JMenuBar getJMenuBar();

	Component getView();

	ICommandChainNode getCommandChainNode();

}

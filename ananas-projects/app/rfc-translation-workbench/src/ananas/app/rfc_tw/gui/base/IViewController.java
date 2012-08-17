package ananas.app.rfc_tw.gui.base;

import java.awt.Component;

import ananas.lib.blueprint.elements.awt.util.IEventChainNode;
import ananas.lib.blueprint.elements.swing.IEJMenuBar;

public interface IViewController {

	IEJMenuBar getMenuBar();

	Component getView();

	IEventChainNode getEventChainNode();

	boolean bindMenuBar(IEJMenuBar menuBar);

	boolean bindView(Component view);
}

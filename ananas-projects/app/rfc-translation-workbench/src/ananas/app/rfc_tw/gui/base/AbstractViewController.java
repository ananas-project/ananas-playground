package ananas.app.rfc_tw.gui.base;

import java.awt.Component;
import java.util.EventObject;

import ananas.lib.blueprint.elements.awt.util.DefaultEventChainNode;
import ananas.lib.blueprint.elements.awt.util.IEventChainNode;
import ananas.lib.blueprint.elements.swing.IEJMenuBar;

public class AbstractViewController implements IViewController {

	@Override
	public final IEventChainNode getEventChainNode() {
		return this.mEventChainNode;
	}

	protected EventObject onEvent(EventObject event) {
		return event;
	}

	private final IEventChainNode mEventChainNode = new DefaultEventChainNode() {

		@Override
		protected EventObject onEvent(EventObject event) {
			return AbstractViewController.this.onEvent(event);
		}

	};

	private IEJMenuBar mMenuBar;
	private Component mView;

	@Override
	public final Component getView() {
		return this.mView;
	}

	@Override
	public final boolean bindView(Component view) {
		if (this.mView == null) {
			this.mView = view;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public final IEJMenuBar getMenuBar() {
		return this.mMenuBar;
	}

	@Override
	public final boolean bindMenuBar(IEJMenuBar menuBar) {
		if (this.mMenuBar != null)
			return false;

		IEventChainNode cNode = menuBar.getEventChainNode();
		IEventChainNode pNode = this.getEventChainNode();
		cNode.setNextNode(pNode);

		this.mMenuBar = menuBar;
		return true;
	}

}

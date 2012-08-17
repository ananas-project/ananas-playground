package ananas.lib.blueprint.elements.swing;

import java.util.EventObject;

import javax.swing.JMenuBar;

import ananas.lib.blueprint.IElement;
import ananas.lib.blueprint.elements.awt.util.DefaultEventChainNode;
import ananas.lib.blueprint.elements.awt.util.IEventChainNode;

public interface IEJMenuBar extends IEJComponent {

	JMenuBar toJMenuBar();

	IEventChainNode getEventChainNode();

	public static class Wrapper extends IEJComponent.Wrapper implements
			IEJMenuBar {

		@Override
		public JMenuBar toJMenuBar() {
			return (JMenuBar) this.getTarget(true);
		}

		@Override
		public IEventChainNode getEventChainNode() {
			return this.mEventChainNode;
		}

		@Override
		public boolean appendChild(IElement child) {
			if (child instanceof IEJMenuItem) {
				IEJMenuItem ch = (IEJMenuItem) child;
				ch.getEventChainNode().setNextNode(this.getEventChainNode());
			}
			return super.appendChild(child);
		}

		private final IEventChainNode mEventChainNode = new DefaultEventChainNode() {

			@Override
			protected EventObject onEvent(EventObject event) {
				System.out.println(this + ".onEvent():" + event.getSource());
				return super.onEvent(event);
			}

		};

	}
}

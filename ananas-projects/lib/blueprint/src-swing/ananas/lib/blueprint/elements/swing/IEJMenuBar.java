package ananas.lib.blueprint.elements.swing;

import javax.swing.JMenuBar;

import ananas.lib.blueprint.IElement;

public interface IEJMenuBar extends IEJComponent {

	JMenuBar toJMenuBar();

	IEventChainNode getEventChainNode();

	public static class Wrapper extends IEJComponent.Wrapper implements
			IEJMenuBar {

		private final IEventChainNode mEventChainNode = new DefaultEventChainNode();

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

	}
}

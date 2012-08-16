package ananas.lib.blueprint.elements.swing;

import javax.swing.JMenu;

import ananas.lib.blueprint.IElement;

public interface IEJMenu extends IEJMenuItem {

	JMenu toJMenu();

	public static class Wrapper extends IEJMenuItem.Wrapper implements IEJMenu {

		@Override
		public JMenu toJMenu() {
			return (JMenu) this.getTarget(true);
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

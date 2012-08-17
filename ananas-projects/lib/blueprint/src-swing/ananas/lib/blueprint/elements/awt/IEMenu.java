package ananas.lib.blueprint.elements.awt;

import java.awt.Menu;

import ananas.lib.blueprint.IElement;
import ananas.lib.blueprint.elements.awt.util.IEventChainNode;

public interface IEMenu extends IEMenuItem {

	Menu toMenu();

	public static class Wrapper extends IEMenuItem.Wrapper implements IEMenu {

		@Override
		public Menu toMenu() {
			return (Menu) this.getTarget(true);
		}

		@Override
		public Object createTarget() {
			return new Menu();
		}

		@Override
		public boolean appendChild(IElement element) {

			if (element == null) {
				return false;

			} else if (element instanceof IEMenuItem) {
				this._addMenuItem((IEMenuItem) element);

			} else {
				return super.appendChild(element);

			}
			return true;
		}

		private void _addMenuItem(IEMenuItem item) {
			this.toMenu().add(item.toMenuItem());
			IEventChainNode pNode = this.getEventChainNode();
			IEventChainNode cNode = item.getEventChainNode();
			cNode.setNextNode(pNode);
		}

	}

}

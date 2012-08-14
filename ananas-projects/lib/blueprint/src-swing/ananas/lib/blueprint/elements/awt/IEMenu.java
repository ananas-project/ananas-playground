package ananas.lib.blueprint.elements.awt;

import java.awt.Menu;

import ananas.lib.blueprint.IElement;

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
				IEMenuItem item = (IEMenuItem) element;
				this.toMenu().add(item.toMenuItem());

			} else {
				return super.appendChild(element);

			}
			return true;
		}

	}

}

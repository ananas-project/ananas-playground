package ananas.lib.blueprint.elements.awt;

import java.awt.MenuBar;

import ananas.lib.blueprint.IElement;

public interface IEMenuBar extends IEMenuComponent {

	MenuBar toMenuBar();

	public static class Wrapper extends IEMenuComponent.Wrapper implements
			IEMenuBar {

		@Override
		public MenuBar toMenuBar() {
			return (MenuBar) this.getTarget(true);
		}

		@Override
		public Object createTarget() {
			return new MenuBar();
		}

		@Override
		public boolean appendChild(IElement element) {

			if (element == null) {
				return false;

			} else if (element instanceof IEMenu) {
				IEMenu menu = (IEMenu) element;
				this.toMenuBar().add(menu.toMenu());

			} else {
				return super.appendChild(element);

			}
			return true;
		}

	}

}

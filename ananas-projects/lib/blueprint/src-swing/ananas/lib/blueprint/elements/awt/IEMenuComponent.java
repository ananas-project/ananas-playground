package ananas.lib.blueprint.elements.awt;

import java.awt.MenuComponent;

public interface IEMenuComponent extends IEObject {

	MenuComponent toMenuComponent();

	public static class Wrapper extends IEObject.Wrapper implements
			IEMenuComponent {

		@Override
		public MenuComponent toMenuComponent() {
			return (MenuComponent) this.getTarget(true);
		}
	}

}

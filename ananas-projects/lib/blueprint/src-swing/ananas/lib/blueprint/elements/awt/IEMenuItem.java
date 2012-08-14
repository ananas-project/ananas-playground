package ananas.lib.blueprint.elements.awt;

import java.awt.MenuItem;

public interface IEMenuItem extends IEMenuComponent {

	MenuItem toMenuItem();

	public static class Wrapper extends IEMenuComponent.Wrapper implements
			IEMenuItem {

		public static final String attr_label = "label";

		@Override
		public MenuItem toMenuItem() {
			return (MenuItem) this.getTarget(true);
		}

		@Override
		public Object createTarget() {
			return new MenuItem();
		}

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {

			if (name == null) {
				return false;

			} else if (attr_label.equals(name)) {
				this.toMenuItem().setLabel(value);

			} else {
				return super.setAttribute(nsURI, name, value);

			}
			return true;
		}

	}

}

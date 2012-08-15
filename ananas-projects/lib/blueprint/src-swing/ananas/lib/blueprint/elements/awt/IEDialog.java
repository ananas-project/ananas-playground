package ananas.lib.blueprint.elements.awt;

import java.awt.Dialog;

public interface IEDialog extends IEWindow {

	Dialog toDialog();

	public static class Wrapper extends IEWindow.Wrapper implements IEDialog {

		private static final Object attr_title = "title";

		public Wrapper() {
		}

		@Override
		public Dialog toDialog() {
			return (Dialog) this.getTarget(true);
		}

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {
			if (name == null) {
				return false;
			} else if (name.equals(attr_title)) {
				this.toDialog().setTitle(value);
			} else {
				return super.setAttribute(nsURI, name, value);
			}
			return true;
		}

	}
}

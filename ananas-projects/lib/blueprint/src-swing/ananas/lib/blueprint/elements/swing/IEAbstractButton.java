package ananas.lib.blueprint.elements.swing;

import javax.swing.AbstractButton;

public interface IEAbstractButton extends IEJComponent {

	AbstractButton toAbstractButton();

	public static class Wrapper extends IEJComponent.Wrapper implements
			IEAbstractButton {

		public final static String attr_text = "text";

		@Override
		public AbstractButton toAbstractButton() {
			return (AbstractButton) this.getTarget(true);
		}

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {

			if (name == null) {
				return false;

			} else if (name.equals(attr_text)) {
				this.toAbstractButton().setText(value);

			} else {
				return super.setAttribute(nsURI, name, value);

			}
			return true;
		}

	}

}

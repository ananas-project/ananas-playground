package ananas.lib.blueprint.elements.swing;

import javax.swing.JPasswordField;

public interface IEJPasswordField extends IEJTextField {

	JPasswordField toJPasswordField();

	public static class Wrapper extends IEJTextField.Wrapper implements
			IEJPasswordField {

		@Override
		public JPasswordField toJPasswordField() {
			return (JPasswordField) this.getTarget(true);
		}
	}
}

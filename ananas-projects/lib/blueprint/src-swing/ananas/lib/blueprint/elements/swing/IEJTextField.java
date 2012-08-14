package ananas.lib.blueprint.elements.swing;

import javax.swing.JTextField;

public interface IEJTextField extends IEJTextComponent {

	JTextField toJTextField();

	public static class Wrapper extends IEJTextComponent.Wrapper implements
			IEJTextField {

		@Override
		public JTextField toJTextField() {
			return (JTextField) this.getTarget(true);
		}
	}
}

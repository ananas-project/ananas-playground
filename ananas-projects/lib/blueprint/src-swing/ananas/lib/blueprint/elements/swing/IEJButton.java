package ananas.lib.blueprint.elements.swing;

import javax.swing.JButton;

public interface IEJButton extends IEAbstractButton {

	JButton toJButton();

	public static class Wrapper extends IEAbstractButton.Wrapper implements
			IEJButton {

		@Override
		public JButton toJButton() {
			return (JButton) this.getTarget(true);
		}

	}

}

package ananas.lib.blueprint.elements.swing;

import javax.swing.JTextArea;

public interface IEJTextArea extends IEJTextComponent {

	JTextArea toJTextArea();

	public static class Wrapper extends IEJTextComponent.Wrapper implements
			IEJTextArea {

		@Override
		public JTextArea toJTextArea() {
			return (JTextArea) this.getTarget(true);
		}
	}

}

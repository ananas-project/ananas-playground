package ananas.lib.blueprint.elements.swing;

import javax.swing.JPanel;

public interface IEJPanel extends IEJComponent {

	JPanel toJPanel();

	public static class Wrapper extends IEJComponent.Wrapper implements
			IEJPanel {

		@Override
		public JPanel toJPanel() {
			return (JPanel) this.getTarget(true);
		}

	}

}

package ananas.lib.blueprint.elements.swing;

import javax.swing.JLayeredPane;

public interface IEJLayeredPane extends IEJComponent {

	JLayeredPane toJLayeredPane();

	public static class Wrapper extends IEJComponent.Wrapper implements
			IEJLayeredPane {

		@Override
		public JLayeredPane toJLayeredPane() {
			return (JLayeredPane) this.getTarget(true);
		}
	}

}

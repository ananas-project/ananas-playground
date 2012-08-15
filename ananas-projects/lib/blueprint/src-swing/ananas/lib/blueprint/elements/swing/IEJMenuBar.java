package ananas.lib.blueprint.elements.swing;

import javax.swing.JMenuBar;

public interface IEJMenuBar extends IEJComponent {

	JMenuBar toJMenuBar();

	public static class Wrapper extends IEJComponent.Wrapper implements
			IEJMenuBar {

		@Override
		public JMenuBar toJMenuBar() {
			return (JMenuBar) this.getTarget(true);
		}
	}
}

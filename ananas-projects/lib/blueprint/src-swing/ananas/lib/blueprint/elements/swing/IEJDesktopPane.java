package ananas.lib.blueprint.elements.swing;

import javax.swing.JDesktopPane;

public interface IEJDesktopPane extends IEJLayeredPane {

	JDesktopPane toJDesktopPane();

	public static class Wrapper extends IEJLayeredPane.Wrapper implements
			IEJDesktopPane {

		@Override
		public JDesktopPane toJDesktopPane() {
			return (JDesktopPane) this.getTarget(true);
		}
	}

}

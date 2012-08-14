package ananas.lib.blueprint.elements.swing;

import javax.swing.JComponent;

import ananas.lib.blueprint.elements.awt.IEContainer;

public interface IEJComponent extends IEContainer {

	JComponent toJComponent();

	public static class Wrapper extends IEContainer.Wrapper implements
			IEJComponent {

		@Override
		public JComponent toJComponent() {
			return (JComponent) this.getTarget(true);
		}

	}

}

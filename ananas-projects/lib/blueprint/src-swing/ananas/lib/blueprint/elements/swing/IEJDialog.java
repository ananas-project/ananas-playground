package ananas.lib.blueprint.elements.swing;

import javax.swing.JDialog;

import ananas.lib.blueprint.elements.awt.IEDialog;

public interface IEJDialog extends IEDialog {

	public static class Wrapper extends IEDialog.Wrapper implements IEJDialog {

		@Override
		public Object createTarget() {
			return new JDialog();
		}

	}

}

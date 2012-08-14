package ananas.lib.blueprint.elements.swing;

import javax.swing.JScrollPane;

import ananas.lib.blueprint.IElement;
import ananas.lib.blueprint.elements.awt.IEComponent;

public interface IEJScrollPane extends IEJComponent {

	JScrollPane toJScrollPane();

	public static class Wrapper extends IEJComponent.Wrapper implements
			IEJScrollPane {

		@Override
		public JScrollPane toJScrollPane() {
			return (JScrollPane) this.getTarget(true);
		}

		@Override
		public boolean appendChild(IElement element) {

			if (element == null) {
				return false;

			} else if (element instanceof IEComponent) {
				this._onAppendComponent((IEComponent) element);

			} else {
				return super.appendChild(element);

			}
			return true;
		}

		private void _onAppendComponent(IEComponent comp) {
			this.toJScrollPane().setViewportView(comp.toComponent());
		}

	}

}

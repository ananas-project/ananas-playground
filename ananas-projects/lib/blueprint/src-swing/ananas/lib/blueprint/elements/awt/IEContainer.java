package ananas.lib.blueprint.elements.awt;

import java.awt.Container;
import java.awt.LayoutManager;

import ananas.lib.blueprint.IElement;

public interface IEContainer extends IEComponent {

	Container toContainer();

	IELayoutManager getLayout();

	void setLayout(IELayoutManager layout);

	public static class Wrapper extends IEComponent.Wrapper implements
			IEContainer {

		private IELayoutManager mLayout;
		private LayoutManager mLayoutNative;
		private IE_position mPosition;

		@Override
		public Container toContainer() {
			return (Container) this.getTarget(true);
		}

		@Override
		public boolean appendChild(IElement element) {

			if (element == null) {
				return false;

			} else if (element instanceof IEComponent) {
				this._addComponent((IEComponent) element);

			} else if (element instanceof IELayoutManager) {
				this.setLayout((IELayoutManager) element);

			} else if (element instanceof IE_position) {
				this.mPosition = (IE_position) element;

			} else {
				return super.appendChild(element);

			}
			return true;
		}

		private void _addComponent(IEComponent component) {

			IELayoutManager layout = this.mLayout;
			if (layout == null) {
				this.toContainer().add(component.toComponent());
				return;
			}

			if (this.mLayoutNative == null) {
				LayoutManager ln = layout.toLayoutManager();
				this.mLayoutNative = ln;
				this.toContainer().setLayout(ln);
			}

			layout.addComponentToContainer(component, this, this.mPosition);
		}

		@Override
		public IELayoutManager getLayout() {
			return this.mLayout;
		}

		@Override
		public void setLayout(IELayoutManager layout) {
			this.mLayout = layout;
			this.mLayoutNative = null;
		}

	}

}

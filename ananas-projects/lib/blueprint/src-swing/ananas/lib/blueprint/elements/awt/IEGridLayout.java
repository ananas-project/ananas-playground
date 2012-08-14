package ananas.lib.blueprint.elements.awt;

import java.awt.GridLayout;
import java.awt.LayoutManager;

public interface IEGridLayout extends IEObject, IELayoutManager {

	GridLayout toGridLayout();

	public static class Wrapper extends IEObject.Wrapper implements
			IEGridLayout {

		private static final String attr_rows = "rows";
		private static final String attr_columns = "columns";

		private int mAttrRows;
		private int mAttrColumns;

		@Override
		public LayoutManager toLayoutManager() {
			return (LayoutManager) this.getTarget(true);
		}

		@Override
		public void addComponentToContainer(IEComponent component,
				IEContainer container, IE_position position) {

			container.toContainer().add(component.toComponent());

		}

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {

			if (name == null) {
				return false;

			} else if (name.equals(attr_rows)) {
				this.mAttrRows = Integer.parseInt(value);

			} else if (name.equals(attr_columns)) {
				this.mAttrColumns = Integer.parseInt(value);

			} else {
				return super.setAttribute(nsURI, name, value);

			}
			return true;
		}

		@Override
		public Object createTarget() {
			if (this.mAttrColumns == 0 && this.mAttrRows == 0)
				return new GridLayout();
			return new GridLayout(this.mAttrRows, this.mAttrColumns);
		}

		@Override
		public GridLayout toGridLayout() {
			return (GridLayout) this.getTarget(true);
		}
	}
}

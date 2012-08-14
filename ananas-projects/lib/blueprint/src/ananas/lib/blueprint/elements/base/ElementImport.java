package ananas.lib.blueprint.elements.base;

import ananas.lib.blueprint.DefaultElement;
import ananas.lib.blueprint.IElement;

public interface ElementImport extends IElement {

	String getValue();

	String getType();

	public class DefaultElementImport extends DefaultElement implements
			ElementImport {

		private String mValue;
		private String mType;

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {

			if (name == null || value == null) {
				return false;

			} else if (name.equals("type")) {
				this.mType = value;

			} else if (name.equals("value")) {
				this.mValue = value;

			} else {
				return super.setAttribute(nsURI, name, value);
			}

			return true;
		}

		@Override
		public String getType() {
			return this.mType;
		}

		@Override
		public String getValue() {
			return this.mValue;
		}

	}
}

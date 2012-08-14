package ananas.lib.blueprint.elements.base;

import ananas.lib.blueprint.DefaultElement;
import ananas.lib.blueprint.IElement;

public interface ElementBlueprint extends IElement {

	public class DefaultElementBlueprint extends DefaultElement implements
			ElementBlueprint {

		private ElementContent mContent;

		@Override
		public boolean appendText(String text) {

			return false;
		}

		@Override
		public boolean appendChild(IElement element) {

			if (element == null) {
				return false;
			} else if (element instanceof ElementImport) {
				this.onImport((ElementImport) element);
				return true;
			} else if (element instanceof ElementContent) {
				this.mContent = (ElementContent) element;
				return true;
			} else {
				return super.appendChild(element);
			}
		}

		private void onImport(ElementImport element) {

			String type = element.getType();
			String value = element.getValue();
			if ("class:NamespaceLoader".equals(type)) {
				element.getOwnerDocument().getImplementation()
						.getNamespaceRegistrar().loadNamespace(value);
			}
		}

		public ElementContent getContent() {
			return this.mContent;
		}

	}
}
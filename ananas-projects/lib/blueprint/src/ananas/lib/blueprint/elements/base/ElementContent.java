package ananas.lib.blueprint.elements.base;

import ananas.lib.blueprint.DefaultElement;
import ananas.lib.blueprint.IElement;

public interface ElementContent extends IElement {

	IElement getContentRoot();

	public class DefaultElementContent extends DefaultElement implements
			ElementContent {

		private IElement mRoot;

		@Override
		public boolean appendChild(IElement element) {
			if (element == null) {
				return false;
			} else {
				this.mRoot = element;
				return true;
			}
		}

		@Override
		public IElement getContentRoot() {
			return this.mRoot;
		}

	}

}

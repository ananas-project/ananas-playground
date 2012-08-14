package ananas.lib.blueprint.elements.awt;

import java.awt.Component;

public interface IEComponent extends IEObject {

	Component toComponent();

	public static class Wrapper extends IEObject.Wrapper implements IEComponent {

		@Override
		public Component toComponent() {
			return (Component) this.getTarget(true);
		}

	}
}

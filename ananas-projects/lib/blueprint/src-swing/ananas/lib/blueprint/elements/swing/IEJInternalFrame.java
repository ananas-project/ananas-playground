package ananas.lib.blueprint.elements.swing;

import javax.swing.JInternalFrame;

import ananas.lib.blueprint.IElement;

public interface IEJInternalFrame extends IEJComponent {

	JInternalFrame toJInternalFrame();

	public static class Wrapper extends IEJComponent.Wrapper implements
			IEJInternalFrame {

		private static final Object attr_title = "title";

		private static final Object attr_x = "x";
		private static final Object attr_y = "y";
		private static final Object attr_height = "height";
		private static final Object attr_width = "width";

		@Override
		public JInternalFrame toJInternalFrame() {
			return (JInternalFrame) this.getTarget(true);
		}

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {
			if (name == null) {
				return false;
			} else if (name.equals(attr_x)) {
			} else if (name.equals(attr_y)) {
			} else if (name.equals(attr_width)) {
			} else if (name.equals(attr_height)) {
			} else if (name.equals(attr_title)) {
				this.toJInternalFrame().setTitle(value);
			} else {
				return super.setAttribute(nsURI, name, value);
			}
			return true;
		}

		@Override
		public Object createTarget() {
			JInternalFrame ret = new JInternalFrame("", true, true, true, true);
			return ret;
		}

		@Override
		public boolean appendChild(IElement element) {

			if (element == null) {
				return false;
			
			} else if (element instanceof IEJMenuBar) {
				IEJMenuBar mb = (IEJMenuBar) element;
				this.toJInternalFrame().setJMenuBar(mb.toJMenuBar());
			
			} else {
				return super.appendChild(element);
			
			}
			return true;
		}

	}

}

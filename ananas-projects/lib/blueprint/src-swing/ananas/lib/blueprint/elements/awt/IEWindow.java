package ananas.lib.blueprint.elements.awt;

import java.awt.Window;

public interface IEWindow extends IEContainer {

	Window toWindow();

	public static class Wrapper extends IEContainer.Wrapper implements IEWindow {

		public static final String attr_width = "width";
		public static final String attr_height = "height";
		public static final String attr_x = "x";
		public static final String attr_y = "y";

		private String mHeight;
		private String mWidth;
		private String mX;
		private String mY;

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {

			if (name == null) {
				return false;

			} else if (name.equals(attr_height)) {
				this.mHeight = value;

			} else if (name.equals(attr_width)) {
				this.mWidth = value;

			} else if (name.equals(attr_x)) {
				this.mX = value;

			} else if (name.equals(attr_y)) {
				this.mY = value;

			} else {
				return super.setAttribute(nsURI, name, value);
			}

			return true;
		}

		@Override
		public Object createTarget() {
			return new Window(null);
		}

		@Override
		public void tagEnd() {

			super.tagEnd();

			Window window = (Window) this.getTarget(true);
			IAttrParser ap = this.getAttrParser();

			if (this.mHeight != null || this.mWidth != null || this.mX != null
					|| this.mY != null) {
				int w = ap.parsePixels(this.mWidth);
				int h = ap.parsePixels(this.mHeight);
				int x = ap.parsePixels(this.mX);
				int y = ap.parsePixels(this.mY);
				window.setBounds(x, y, w, h);
			}
		}

		@Override
		public Window toWindow() {
			return (Window) this.getTarget(true);
		}

	}

}

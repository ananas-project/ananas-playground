package ananas.lib.blueprint.elements.awt;

import java.awt.Frame;

import ananas.lib.blueprint.IElement;

public interface IEFrame extends IEWindow {

	Frame toFrame();

	public static class Wrapper extends IEWindow.Wrapper implements IEFrame {

		public static final String attr_title = "title";

		private String mTitle;

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {

			if (name == null) {
				return false;
			} else if (name.equals(attr_title)) {
				this.mTitle = value;
			} else {
				return super.setAttribute(nsURI, name, value);
			}
			return true;
		}

		@Override
		public boolean appendChild(IElement element) {

			if (element == null) {
				return false;

			} else if (element instanceof IEMenuBar) {
				IEMenuBar mb = (IEMenuBar) element;
				this.toFrame().setMenuBar(mb.toMenuBar());

			} else {
				return super.appendChild(element);

			}
			return true;
		}

		@Override
		public Object createTarget() {
			return new Frame();
		}

		@Override
		public void tagEnd() {

			super.tagEnd();

			Frame frame = (Frame) this.getTarget(true);

			if (this.mTitle != null)
				frame.setTitle(this.mTitle);

		}

		@Override
		public Frame toFrame() {
			return (Frame) this.getTarget(true);
		}

	}

}

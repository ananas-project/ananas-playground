package ananas.lib.blueprint.elements.swing;

import javax.swing.text.JTextComponent;

public interface IEJTextComponent extends IEJComponent {

	JTextComponent toJTextComponent();

	public static class Wrapper extends IEJComponent.Wrapper implements
			IEJTextComponent {

		private static final String attr_text = "text";

		private StringBuffer mStrBuf;
		private String mText;

		@Override
		public JTextComponent toJTextComponent() {
			return (JTextComponent) this.getTarget(true);
		}

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {
			if (name == null) {
				return false;
			} else if (name.equals(attr_text)) {
				this.mText = value;
			} else {
				return super.setAttribute(nsURI, name, value);
			}
			return true;
		}

		@Override
		public boolean appendText(String text) {
			if (text == null)
				return false;
			StringBuffer sbuf = this.mStrBuf;
			if (sbuf == null) {
				this.mStrBuf = sbuf = new StringBuffer();
			}
			this.mStrBuf.append(text.trim());
			return true;
		}

		@Override
		public void tagEnd() {
			super.tagEnd();
			if (this.mStrBuf != null) {
				if (this.mStrBuf.length() > 0) {
					String str = this.mStrBuf.toString();
					this.toJTextComponent().setText(str);
				}
			} else if (this.mText != null) {
				String str = this.mText;
				this.toJTextComponent().setText(str);
			}
		}

	}

}

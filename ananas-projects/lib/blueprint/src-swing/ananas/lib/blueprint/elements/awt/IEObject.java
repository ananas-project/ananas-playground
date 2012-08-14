package ananas.lib.blueprint.elements.awt;

import ananas.lib.blueprint.DefaultElement;
import ananas.lib.blueprint.IElement;

public interface IEObject extends IElement {

	boolean appendInvoke(IE_invoke invoke);

	public static class Wrapper extends DefaultElement implements IEObject {

		private static final IAttrParser sAttrParser;

		static {
			sAttrParser = new DefaultAttrParser();
		}

		public IAttrParser getAttrParser() {
			return sAttrParser;
		}

		@Override
		public boolean appendInvoke(IE_invoke invoke) {
			return false;
		}

		@Override
		public boolean appendChild(IElement element) {
			if (element == null) {
				return false;
			} else if (element instanceof IE_invoke) {
				this._doInvoke((IE_invoke) element);
			} else {
				return super.appendChild(element);
			}
			return true;
		}

		private final void _doInvoke(IE_invoke invoke) {
			boolean rlt = this.appendInvoke(invoke);
			if (!rlt) {
				String msg = "cannot invoke method on the object";
				System.err.println(msg);
				System.err.println("    " + "object=" + this);
				System.err.println("    " + "method=" + invoke.getMethod());
				throw new RuntimeException(msg);
			}
		}

	}

	public static interface IAttrParser {

		int parsePixels(String s);
	}

	public static class DefaultAttrParser implements IAttrParser {

		@Override
		public int parsePixels(String s) {
			if (s == null)
				return 0;
			return Integer.parseInt(s);
		}
	}

}

package ananas.lib.blueprint.elements.awt;

import java.util.LinkedList;
import java.util.List;

import ananas.lib.blueprint.IElement;

public interface IE_invoke extends IEObject {

	String getMethod();

	List<IElement> getParameterList();

	public static class Wrapper extends IEObject.Wrapper implements IE_invoke {

		public final static String attr_method = "method";
		private String mMethod;
		private List<IElement> mParamList;

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {
			if (name == null) {
				return false;
			} else if (name.equals(attr_method)) {
				this.mMethod = value;
			} else {
				return super.setAttribute(nsURI, name, value);
			}
			return true;
		}

		@Override
		public String getMethod() {
			return this.mMethod;
		}

		@Override
		public boolean appendChild(IElement element) {
			if (element == null) {
				return false;
			} else {
				this._getParameterList(true).add(element);
				return true;
			}
		}

		private List<IElement> _getParameterList(boolean create) {
			List<IElement> ret = this.mParamList;
			if (ret == null) {
				if (create) {
					this.mParamList = ret = new LinkedList<IElement>();
				}
			}
			return ret;
		}

		@Override
		public List<IElement> getParameterList() {
			return this.mParamList;
		}

	}

	public static class Core {
	}

}

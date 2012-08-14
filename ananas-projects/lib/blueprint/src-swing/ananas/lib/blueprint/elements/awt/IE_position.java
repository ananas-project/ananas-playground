package ananas.lib.blueprint.elements.awt;

public interface IE_position extends IEObject {

	String getValue();

	public static class Wrapper extends IEObject.Wrapper implements IE_position {

		public final static String attr_value = "value";
		private String mValue;

		@Override
		public boolean setAttribute(String nsURI, String name, String value) {
			if (name == null) {
				return false;
			} else if (name.equals(attr_value)) {
				this.mValue = value;
			} else {
				return super.setAttribute(nsURI, name, value);
			}
			return true;
		}

		@Override
		public String getValue() {
			return this.mValue;
		}
	}

	public static class Core {
	}

}

package ananas.lib.servkit.pool.bp;

import ananas.lib.blueprint.DefaultElement;
import ananas.lib.blueprint.elements.base.ElementImport;

public class EPoolObjectBase extends DefaultElement {

	private static final Object attr_class = "class";
	private String mClassRef;

	@Override
	public boolean setAttribute(String nsURI, String name, String value) {
		if (name == null) {
			return false;
		} else if (name.equals(attr_class)) {
			this.mClassRef = value;
		} else {
			return super.setAttribute(nsURI, name, value);
		}
		return true;
	}

	protected Class<?> findClass(String classRef) {
		if (classRef == null) {
			classRef = this.mClassRef;
		}
		Class<?> cls = null;
		if (classRef.startsWith("#")) {
			ElementImport aImport = (ElementImport) this.getOwnerDocument()
					.findElementById(classRef.substring(1));
			if (aImport == null) {
				throw new RuntimeException("cannot find the import of class:"
						+ classRef);
			}
			cls = aImport.importClass();
		} else {
			try {
				cls = Class.forName(classRef);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		return cls;
	}
}

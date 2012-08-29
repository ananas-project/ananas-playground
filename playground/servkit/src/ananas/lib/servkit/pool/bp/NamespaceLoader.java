package ananas.lib.servkit.pool.bp;

import ananas.lib.blueprint.IImplementation;
import ananas.lib.blueprint.INamespace;
import ananas.lib.blueprint.INamespaceLoader;

public class NamespaceLoader

implements INamespaceLoader {

	public NamespaceLoader() {
	}

	@Override
	public INamespace load(IImplementation impl) {

		String nsURI = "xmlns:ananas:servkit:pool";
		String defaultPrefix = "pool";
		INamespace ns = impl.newNamespace(nsURI, defaultPrefix);

		// begin

		// reg(ns, "Object", IEObject.Wrapper.class, Object.class);
		reg(ns, "factory", EPoolFactory.class, Object.class);
		reg(ns, "group", EPoolGroup.class, Object.class);
		reg(ns, "pool", EPoolPool.class, Object.class);

		// end

		return ns;

	}

	private void reg(INamespace ns, String localName, Class<?> elementClass,
			Class<?> targetClass) {

		ns.registerClass(localName, elementClass, targetClass);
	}

}
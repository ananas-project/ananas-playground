package ananas.lib.servkit.pool.bpr;

import ananas.lib.blueprint.elements.reflect.AbstractReflectNamespaceLoader;

public class NamespaceLoader extends AbstractReflectNamespaceLoader {

	static final String nsURI = "xmlns:ananas:servkit:pool.ref";
	static final String defaultPrefix = "pool";

	public NamespaceLoader() {
		super(nsURI, defaultPrefix);
	}

	@Override
	protected void onLoad(ClassRegistrar reg) {

		reg.reg("factory", BprPoolFactory.class);
		reg.reg("group", BprPoolGroup.class);
		reg.reg("pool", BprSinglePool.class);
	}

}

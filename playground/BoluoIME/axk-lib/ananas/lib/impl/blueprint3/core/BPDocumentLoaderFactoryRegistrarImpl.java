package ananas.lib.impl.blueprint3.core;

import java.util.HashMap;
import java.util.Map;

import ananas.lib.blueprint3.lang.BPDocumentLoaderFactory;
import ananas.lib.blueprint3.lang.BPDocumentLoaderFactoryRegistrar;

public class BPDocumentLoaderFactoryRegistrarImpl implements
		BPDocumentLoaderFactoryRegistrar {

	private final Map<String, BPDocumentLoaderFactory> mMap;
	private BPDocumentLoaderFactory mDefault = null;
	private final String mName;

	public BPDocumentLoaderFactoryRegistrarImpl(String name) {
		this.mMap = new HashMap<String, BPDocumentLoaderFactory>();
		this.mName = name;
	}

	@Override
	public void registerFactory(String key, BPDocumentLoaderFactory factory) {
		this.mMap.put(key, factory);
	}

	@Override
	public void setDefaultFactory(BPDocumentLoaderFactory factory) {
		this.mDefault = factory;
	}

	@Override
	public BPDocumentLoaderFactory getFactory(String key, boolean enableDefault) {
		BPDocumentLoaderFactory ret = this.mMap.get(key);
		if ((ret == null) && (enableDefault)) {
			ret = this.mDefault;
		}
		if (ret == null) {
			System.err.println("warning: no loader for " + this.mName + " '"
					+ key + "'");
		}
		return ret;
	}

	@Override
	public BPDocumentLoaderFactory getFactory(String key) {
		return this.getFactory(key, true);
	}

}

package ananas.lib.impl.blueprint3.core;

import java.util.HashMap;
import java.util.Map;

import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceInfo;

public class NsLoadingManager {

	final Map<String, Class<?>> mLoaded = new HashMap<String, Class<?>>();

	public void load(BPEnvironment envi, BPNamespaceInfo info, boolean lazy) {
		final Class<?> infoClass = info.getClass();
		if (lazy) {
			if (this.mLoaded.containsKey(infoClass.getName())) {
				return;
			}
		}
		envi.getNamespaceLoaderFactory().getLoader().load(envi, info);
		this.mLoaded.put(infoClass.getName(), infoClass);
	}

}

package ananas.lib.impl.blueprint3.core;

import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.lang.BlueprintException;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceInfo;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceLoader;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceLoaderFactory;

public class MainNsLoaderFactory implements BPNamespaceLoaderFactory {

	private BPNamespaceLoader mLoader;

	@Override
	public BPNamespaceLoader getLoader() {
		if (this.mLoader == null) {
			this.mLoader = new MyLoader();
		}
		return this.mLoader;
	}

	private static class MyLoader implements BPNamespaceLoader {

		@Override
		public void load(BPEnvironment environment, BPNamespaceInfo info)
				throws BlueprintException {

			try {
				// System.out.println("Load namespace : "
				// + info.getClass().getName());

				String ldrClass = info.getProperty("loader");
				if (ldrClass == null) {
					throw new BlueprintException(
							"The properties must contain a key named of 'loader' .");
				}
				Class<?> cls = Class.forName(ldrClass);
				Object inst = cls.newInstance();
				BPNamespaceLoader ldr = null;
				if (inst instanceof BPNamespaceLoaderFactory) {
					ldr = ((BPNamespaceLoaderFactory) inst).getLoader();
				} else {
					ldr = (BPNamespaceLoader) inst;
				}
				ldr.load(environment, info);

			} catch (Exception e) {

				if (e instanceof BlueprintException) {
					throw (BlueprintException) e;
				} else {
					throw new BlueprintException(e);
				}

			}
		}
	}

}

package ananas.lib.blueprint.impl;

import java.util.HashMap;
import java.util.HashSet;

import ananas.lib.blueprint.IImplementation;
import ananas.lib.blueprint.INamespace;
import ananas.lib.blueprint.INamespaceLoader;
import ananas.lib.blueprint.INamespaceRegistrar;

class ImplNamespaceRegistrar implements INamespaceRegistrar {

	private final IImplementation mImpl;
	private final HashMap<String, INamespace> mNsTable;
	private final HashSet<Class<?>> mLoaded;

	public ImplNamespaceRegistrar(IImplementation impl) {
		this.mImpl = impl;
		this.mNsTable = new HashMap<String, INamespace>();
		this.mLoaded = new HashSet<Class<?>>();
	}

	@Override
	public IImplementation getImplementation() {
		return this.mImpl;
	}

	@Override
	public INamespace getNamespace(String nsURI) {
		if (nsURI == null)
			return null;
		return this.mNsTable.get(nsURI);
	}

	@Override
	public void loadNamespace(INamespaceLoader loader) {
		IImplementation impl = this.getImplementation();
		INamespace ns = loader.load(impl);
		String nsURI = ns.getNamespaceURI();
		this.mNsTable.put(nsURI, ns);
	}

	@Override
	public void loadNamespace(Class<?> loaderClass) {
		try {
			if (this.mLoaded.contains(loaderClass)) {
				return;
			}
			INamespaceLoader ldr = (INamespaceLoader) loaderClass.newInstance();
			this.loadNamespace(ldr);
			this.mLoaded.add(loaderClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadNamespace(String loaderClassPath) {
		try {
			Class<?> ldrClass = Class.forName(loaderClassPath);
			this.loadNamespace(ldrClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

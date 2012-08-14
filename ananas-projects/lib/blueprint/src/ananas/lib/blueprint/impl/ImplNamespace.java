package ananas.lib.blueprint.impl;

import java.util.HashMap;

import ananas.lib.blueprint.IClass;
import ananas.lib.blueprint.IImplementation;
import ananas.lib.blueprint.INamespace;

class ImplNamespace implements INamespace {

	private final String mDefaultPrefix;
	private final String mNsURI;
	private final IImplementation mImpl;

	private final HashMap<Class<?>, IClass> mTableClass;
	private final HashMap<String, IClass> mTableLocalName;

	public ImplNamespace(IImplementation impl, String nsURI,
			String defaultPrefix) {

		this.mNsURI = nsURI;
		this.mDefaultPrefix = defaultPrefix;
		this.mImpl = impl;

		this.mTableClass = new HashMap<Class<?>, IClass>();
		this.mTableLocalName = new HashMap<String, IClass>();
	}

	@Override
	public String getNamespaceURI() {
		return this.mNsURI;
	}

	@Override
	public IClass findClass(String localName) {
		return this.mTableLocalName.get(localName);
	}

	@Override
	public IImplementation getImplementation() {
		return this.mImpl;
	}

	@Override
	public void registerClass(String localName, Class<?> elementClass,
			Class<?> targetClass) {

		IClass cls = new ImplClass(this, localName, elementClass, targetClass);
		this.mTableLocalName.put(localName, cls);
		this.mTableClass.put(targetClass, cls);
		this.mTableClass.put(elementClass, cls);
	}

	@Override
	public void registerClass(String localName, String elementClass,
			String targetClass) {

		try {
			Class<?> ec = Class.forName(elementClass);
			Class<?> tc = Class.forName(targetClass);
			this.registerClass(localName, ec, tc);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IClass findClass(Class<?> aClass) {
		return this.mTableClass.get(aClass);
	}

	@Override
	public IClass findClass(Object object) {
		return this.findClass(object.getClass());
	}

	@Override
	public String getDefaultPrefix() {
		return this.mDefaultPrefix;
	}

}

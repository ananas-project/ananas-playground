package ananas.lib.impl.blueprint3.core;

import java.util.HashMap;
import java.util.Map;

import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.lang.BPNamespace;
import ananas.lib.blueprint3.lang.BPType;

public class BPNamespaceImpl implements BPNamespace {

	private final String mPrefix;
	private final String mURI;
	private final BPEnvironment mEnvir;
	private final Map<String, BPType> mTypesMap;

	public BPNamespaceImpl(BPEnvironment envi, String uri, String defaultPrefix) {
		this.mTypesMap = new HashMap<String, BPType>();
		this.mEnvir = envi;
		this.mURI = uri;
		this.mPrefix = defaultPrefix;
	}

	@Override
	public String getDefaultPrefix() {
		return this.mPrefix;
	}

	@Override
	public String getNamespaceURI() {
		return this.mURI;
	}

	@Override
	public BPType getType(String localName) {
		return this.mTypesMap.get(localName);
	}

	@Override
	public BPEnvironment getOwnerEnvironment() {
		return this.mEnvir;
	}

	@Override
	public boolean registerType(BPType bpType) {
		String lname = bpType.getLocalName();
		this.mTypesMap.put(lname, bpType);
		return true;
	}

	@Override
	public boolean unregisterType(BPType bpType) {
		String lname = bpType.getLocalName();
		this.mTypesMap.remove(lname);
		return true;
	}

}

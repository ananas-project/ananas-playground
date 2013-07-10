package ananas.lib.blueprint3.lang;

public interface BPNamespace {

	String getNamespaceURI();

	String getDefaultPrefix();

	BPType getType(String localName);

	BPEnvironment getOwnerEnvironment();

	boolean registerType(BPType bpType);

	boolean unregisterType(BPType bpType);

}

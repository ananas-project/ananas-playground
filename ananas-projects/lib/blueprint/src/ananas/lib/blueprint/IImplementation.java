package ananas.lib.blueprint;

public interface IImplementation {

	INamespaceRegistrar getNamespaceRegistrar();

	IDocument newDocument(String docURI);

	INamespace newNamespace(String nsURI, String defaultPrefix);

}

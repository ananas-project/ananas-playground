package ananas.lib.blueprint;

public interface INamespaceRegistrar {

	IImplementation getImplementation();

	INamespace getNamespace(String nsURI);

	void loadNamespace(INamespaceLoader loader);

	void loadNamespace(Class<?> loaderClass);

	void loadNamespace(String loaderClassPath);

}

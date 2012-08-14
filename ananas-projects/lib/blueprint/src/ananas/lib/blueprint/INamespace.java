package ananas.lib.blueprint;

public interface INamespace {

	String getNamespaceURI();

	String getDefaultPrefix();

	IClass findClass(String localName);

	IClass findClass(Class<?> aClass);

	IClass findClass(Object object);

	IImplementation getImplementation();

	void registerClass(String localName, Class<?> elementClass,
			Class<?> targetClass);

	void registerClass(String localName, String elementClass, String targetClass);

}

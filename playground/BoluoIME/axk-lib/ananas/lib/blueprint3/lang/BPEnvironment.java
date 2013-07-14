package ananas.lib.blueprint3.lang;

import java.io.IOException;
import java.net.URI;

import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPImplementation;
import ananas.lib.blueprint3.util.BPBuilderFactory;
import ananas.lib.blueprint3.util.BPVisitorFactory;
import ananas.lib.blueprint3.util.BPXMLReaderFactory;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceInfo;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceLoaderFactory;
import ananas.lib.blueprint3.xml.serializer.BPXmlSerializerFactory;
import ananas.lib.io.Connector;

public interface BPEnvironment {

	BPNamespaceRegistrar getNamespaceRegistrar();

	BPNamespaceLoaderFactory getNamespaceLoaderFactory();

	BPImplementation getImplementation();

	BPXMLReaderFactory getXMLReaderFactory();

	BPXmlSerializerFactory getXmlSerializerFactory();

	BPFileNameMapper getFileNameMapper();

	BPDocumentLoaderFactoryRegistrar getContentTypeRegistrar();

	BPDocumentLoaderFactoryRegistrar getUriSchemeRegistrar();

	BPBuilderFactory getBuilderFactory();

	BPVisitorFactory getVisitorFactory();

	Connector getConnector();

	// ns loading

	void loadNamespace(String aClassName, boolean lazy)
			throws BlueprintException;

	void loadNamespace(Class<?> aClass, boolean lazy) throws BlueprintException;

	void loadNamespace(BPNamespaceInfo loader, boolean lazy)
			throws BlueprintException;

	// doc loading
	BPDocument loadDocument(URI uri) throws IOException;
}

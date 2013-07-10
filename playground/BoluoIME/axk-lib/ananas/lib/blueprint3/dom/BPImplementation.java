package ananas.lib.blueprint3.dom;

import java.net.URI;

import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.lang.BPNamespace;
import ananas.lib.blueprint3.lang.BPType;

public interface BPImplementation {

	BPDocumentGroup createDocumentGroup(BPEnvironment envi);

	BPDocument createDocument(BPDocumentGroup onwerGroup, URI uri);

	BPNamespace createNamespace(BPEnvironment envi, String uri,
			String defaultPrefix);

	BPType createType(BPNamespace ns, String localName, Class<?> ctrlClass,
			Class<?> targetClass);

}

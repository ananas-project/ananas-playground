package ananas.lib.blueprint.elements.base;

import ananas.lib.blueprint.IImplementation;
import ananas.lib.blueprint.INamespace;
import ananas.lib.blueprint.INamespaceLoader;
import ananas.lib.blueprint.elements.base.ElementBlueprint.DefaultElementBlueprint;
import ananas.lib.blueprint.elements.base.ElementContent.DefaultElementContent;
import ananas.lib.blueprint.elements.base.ElementImport.DefaultElementImport;

public class NamespaceLoader implements INamespaceLoader {

	public NamespaceLoader() {
	}

	@Override
	public INamespace load(IImplementation impl) {

		String nsURI = "xmlns:ananas:blueprint:base";
		String defaultPrefix = "bp";
		INamespace ns = impl.newNamespace(nsURI, defaultPrefix);

		this.registerClass(ns, "blueprint", DefaultElementBlueprint.class);
		this.registerClass(ns, "content", DefaultElementContent.class);
		this.registerClass(ns, "import", DefaultElementImport.class);

		return ns;

	}

	private void registerClass(INamespace ns, String localName, Class<?> class1) {
		ns.registerClass(localName, class1, class1);
	}

}

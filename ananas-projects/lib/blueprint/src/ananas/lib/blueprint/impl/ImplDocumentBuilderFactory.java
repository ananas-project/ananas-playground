package ananas.lib.blueprint.impl;

import ananas.lib.blueprint.IBlueprintContext;
import ananas.lib.blueprint.IDocumentBuilder;
import ananas.lib.blueprint.IDocumentBuilderFactory;

class ImplDocumentBuilderFactory implements IDocumentBuilderFactory {

	@Override
	public IDocumentBuilder createDocumentBuilder(IBlueprintContext context) {

		context .getImplementation().getNamespaceRegistrar().loadNamespace(
				ananas.lib.blueprint.elements.base.NamespaceLoader.class);

		return new ImplDocumentBuilder(context);
	}

}

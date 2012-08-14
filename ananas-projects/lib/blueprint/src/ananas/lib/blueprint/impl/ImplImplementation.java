package ananas.lib.blueprint.impl;

import ananas.lib.blueprint.IDocument;
import ananas.lib.blueprint.IImplementation;
import ananas.lib.blueprint.INamespace;
import ananas.lib.blueprint.INamespaceRegistrar;

class ImplImplementation implements IImplementation {

	private final INamespaceRegistrar mNSReg;

	public ImplImplementation() {
		this.mNSReg = new ImplNamespaceRegistrar(this);
	}

	@Override
	public INamespaceRegistrar getNamespaceRegistrar() {
		return this.mNSReg;
	}

	@Override
	public INamespace newNamespace(String nsURI, String defaultPrefix) {
		return new ImplNamespace(this, nsURI, defaultPrefix);
	}

	@Override
	public IDocument newDocument(String docURI) {
		return new ImplDocument(this, docURI);
	}

}

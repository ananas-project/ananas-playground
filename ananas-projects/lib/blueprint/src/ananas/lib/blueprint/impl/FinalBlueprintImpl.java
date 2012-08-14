package ananas.lib.blueprint.impl;

import java.io.InputStream;

import ananas.lib.blueprint.Blueprint;
import ananas.lib.blueprint.IDocument;
import ananas.lib.blueprint.IDocumentBuilder;
import ananas.lib.blueprint.IDocumentBuilderFactory;
import ananas.lib.blueprint.IImplementation;
import ananas.lib.blueprint.io.DefaultConnector;
import ananas.lib.blueprint.io.IConnector;

public final class FinalBlueprintImpl extends Blueprint {

	final static IImplementation sImpl;
	final static IDocumentBuilderFactory sDBF;
	final static IConnector sConnector;

	static {
		sImpl = new ImplImplementation();
		sDBF = new ImplDocumentBuilderFactory();
		sConnector = new DefaultConnector();
	}

	@Override
	public IImplementation getImplementation() {
		return sImpl;
	}

	@Override
	public IDocumentBuilderFactory getDocumentBuilderFactory() {
		return sDBF;
	}

	@Override
	public IDocument loadDocument(String docURI) {
		IDocumentBuilder dbf = this.getDocumentBuilderFactory()
				.createDocumentBuilder(this);
		return dbf.build(docURI);
	}

	@Override
	public IDocument loadDocument(InputStream is, String docURI) {
		IDocumentBuilder dbf = this.getDocumentBuilderFactory()
				.createDocumentBuilder(this);
		return dbf.build(is, docURI);
	}

	@Override
	public IConnector getConnector() {
		return sConnector;
	}

}

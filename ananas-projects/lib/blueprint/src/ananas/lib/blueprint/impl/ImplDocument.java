package ananas.lib.blueprint.impl;

import java.util.HashMap;

import ananas.lib.blueprint.IDocument;
import ananas.lib.blueprint.IElement;
import ananas.lib.blueprint.IImplementation;

class ImplDocument implements IDocument {

	private final IImplementation mImpl;
	private final String mDocURI;
	private IElement mRootElement;
	private final HashMap<String, IElement> mIdToElement;

	public ImplDocument(IImplementation impl, String docURI) {
		this.mImpl = impl;
		this.mDocURI = docURI;
		this.mIdToElement = new HashMap<String, IElement>();
	}

	@Override
	public IElement findElementById(String id) {
		return this.mIdToElement.get(id);
	}

	@Override
	public void registerElement(IElement element) {
		String id = element.getId();
		if (id != null)
			this.mIdToElement.put(id, element);
	}

	@Override
	public IElement getRootElement() {
		return this.mRootElement;
	}

	@Override
	public IImplementation getImplementation() {
		return this.mImpl;
	}

	@Override
	public String getDocumentURI() {
		return this.mDocURI;
	}

	@Override
	public void setRootElement(IElement root) {
		this.mRootElement = root;
	}

	@Override
	public Object findTargetById(String id) {
		IElement element = this.findElementById(id);
		if (element == null)
			return null;
		return element.getTarget();
	}

}

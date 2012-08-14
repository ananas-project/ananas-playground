package ananas.lib.blueprint.impl;

import ananas.lib.blueprint.IClass;
import ananas.lib.blueprint.IDocument;
import ananas.lib.blueprint.IElement;
import ananas.lib.blueprint.INamespace;

class ImplClass implements IClass {

	private final INamespace mOwnerNS;
	private final String mLocalName;
	private final Class<?> mTargetClass;
	private final Class<?> mElementClass;

	public ImplClass(INamespace ns, String localName, Class<?> elementClass,
			Class<?> targetClass) {

		this.mOwnerNS = ns;
		this.mLocalName = localName;
		this.mElementClass = elementClass;
		this.mTargetClass = targetClass;
	}

	@Override
	public INamespace getOwnerNamespace() {
		return this.mOwnerNS;
	}

	@Override
	public String getLocalName() {
		return this.mLocalName;
	}

	@Override
	public Class<?> getElementClass() {
		return this.mElementClass;
	}

	@Override
	public Class<?> getTargetClass() {
		return this.mTargetClass;
	}

	@Override
	public IElement createElement(IDocument ownerDoc) {
		try {
			IElement element = (IElement) this.mElementClass.newInstance();
			element.bindOwnerDocument(ownerDoc);
			element.bindBlueprintClass(this);
			return element;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public IElement createElement(IDocument ownerDoc, Object target) {
		IElement element = this.createElement(ownerDoc);
		element.bindTarget(target);
		return element;
	}

}

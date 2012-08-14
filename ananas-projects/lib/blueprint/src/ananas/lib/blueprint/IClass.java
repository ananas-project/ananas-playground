package ananas.lib.blueprint;

public interface IClass {

	IElement createElement(IDocument ownerDoc);

	IElement createElement(IDocument ownerDoc, Object target);

	INamespace getOwnerNamespace();

	String getLocalName();

	Class<?> getElementClass();

	Class<?> getTargetClass();

}

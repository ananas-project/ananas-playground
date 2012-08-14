package ananas.lib.blueprint;

public interface IDocument {

	IElement findElementById(String id);

	Object findTargetById(String id);

	void registerElement(IElement element);

	IElement getRootElement();

	void setRootElement(IElement root);

	IImplementation getImplementation();

	String getDocumentURI();

}

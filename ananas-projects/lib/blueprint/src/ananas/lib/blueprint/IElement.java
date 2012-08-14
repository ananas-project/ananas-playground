package ananas.lib.blueprint;

/**
 * this class MUST has a Default() constructor
 * */

public interface IElement {

	String getId();

	Object getTarget();

	Object createTarget();

	IDocument getOwnerDocument();

	IClass getBlueprintClass();

	boolean bindTarget(Object target);

	boolean bindOwnerDocument(IDocument ownerDoc);

	boolean bindBlueprintClass(IClass aClass);

	boolean setAttribute(String nsURI, String localName, String value);

	boolean appendText(String text);

	boolean appendChild(IElement element);

	void tagBegin();

	void tagEnd();

	IElement getParent();

	/**
	 * set new parent and return old parent.
	 * 
	 * @param parent
	 *            the new parent.
	 * @return the old parent.
	 * */
	IElement setParent(IElement parent);

}

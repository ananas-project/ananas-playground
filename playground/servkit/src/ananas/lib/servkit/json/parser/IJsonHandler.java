package ananas.lib.servkit.json.parser;


public interface IJsonHandler {

	// document

	void onDocumentPrepare();

	void onSetLocator(IJsonLocator locator);

	void onDocumentBegin();

	void onDocumentEnd();

	void onException(Exception exception);

	// array

	void onArrayBegin();

	void onArrayEnd();

	// object

	void onObjectBegin();

	void onObjectEnd();

	void onObjectKey(String key);

	// other values

	void onString(String s);

	void onBoolean(boolean value);

	void onInteger(int value);

	void onLong(long value);

	void onDouble(double value);

	void onNull();

}

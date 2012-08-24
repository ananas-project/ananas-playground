package ananas.lib.servkit.json.io;

import ananas.lib.servkit.json.JsonException;

public interface IJsonHandler {

	// document

	void onDocumentPrepare();

	void onSetLocator(IJsonLocator locator);

	void onDocumentBegin();

	void onDocumentEnd();

	void onDocumentFinal();

	void onException(Exception exception);

	// array

	void onArrayBegin() throws JsonException;

	void onArrayEnd() throws JsonException;

	// object

	void onObjectBegin() throws JsonException;

	void onObjectEnd() throws JsonException;

	void onObjectKey(String key) throws JsonException;

	// other values

	void onString(String s) throws JsonException;

	void onBoolean(boolean value) throws JsonException;

	void onInteger(int value) throws JsonException;

	void onLong(long value) throws JsonException;

	void onDouble(double value) throws JsonException;

	void onNull() throws JsonException;

}

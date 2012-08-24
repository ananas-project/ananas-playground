package ananas.lib.servkit.json.io;

import java.io.OutputStream;

import ananas.lib.servkit.json.JsonException;

public class DefaultJsonStreamWriter implements IJsonHandler {

	public DefaultJsonStreamWriter(OutputStream os) {
	}

	@Override
	public void onDocumentPrepare() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetLocator(IJsonLocator locator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDocumentBegin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDocumentEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDocumentFinal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onException(Exception exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrayBegin() throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrayEnd() throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onObjectBegin() throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onObjectEnd() throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onObjectKey(String key) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onString(String s) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBoolean(boolean value) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInteger(int value) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLong(long value) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDouble(double value) throws JsonException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNull() throws JsonException {
		// TODO Auto-generated method stub

	}

}

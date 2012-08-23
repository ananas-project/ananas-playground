package test.servkit;

import ananas.lib.servkit.json.parser.IJsonHandler;
import ananas.lib.servkit.json.parser.IJsonLocator;

public class TestingJsonHandler implements IJsonHandler {

	private IJsonLocator mLocator;

	public TestingJsonHandler() {
	}

	@Override
	public void onDocumentPrepare() {
		System.out.println("<json-prepare />");
	}

	@Override
	public void onSetLocator(IJsonLocator locator) {
		this.mLocator = locator;
	}

	@Override
	public void onDocumentBegin() {
		System.out.println("<json>");
	}

	@Override
	public void onDocumentEnd() {
		System.out.println("</json>");
	}

	@Override
	public void onException(Exception exception) {
		System.err.println("exception in file, line:" + this.mLocator.getLine()
				+ " col:" + this.mLocator.getColumn());
		exception.printStackTrace();
	}

	@Override
	public void onArrayBegin() {
		System.out.println("[");
	}

	@Override
	public void onArrayEnd() {
		System.out.println("],");
	}

	@Override
	public void onObjectBegin() {
		System.out.println("{");
	}

	@Override
	public void onObjectEnd() {
		System.out.println("},");
	}

	@Override
	public void onObjectKey(String key) {

		System.out.print ("\"" + key + "\":");

	}

	@Override
	public void onString(String s) {
		System.out.println("\"" + s + "\",");

	}

	@Override
	public void onBoolean(boolean value) {
		System.out.println(value + ",");

	}

	@Override
	public void onInteger(int value) {
		System.out.println(value + ",");

	}

	@Override
	public void onLong(long value) {
		System.out.println(value + ",");

	}

	@Override
	public void onDouble(double value) {
		System.out.println(value + ",");

	}

	@Override
	public void onNull() {
		System.out.println("null");

	}

}

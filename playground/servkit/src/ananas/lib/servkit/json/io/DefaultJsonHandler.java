package ananas.lib.servkit.json.io;


public class DefaultJsonHandler implements IJsonHandler {

	private IJsonLocator mLocator;

	@Override
	public void onArrayBegin() {
	}

	@Override
	public void onArrayEnd() {
	}

	@Override
	public void onObjectBegin() {
	}

	@Override
	public void onObjectEnd() {
	}

	@Override
	public void onObjectKey(String key) {
	}

	@Override
	public void onString(String s) {
	}

	@Override
	public void onBoolean(boolean value) {
	}

	@Override
	public void onInteger(int value) {
	}

	@Override
	public void onLong(long value) {
	}

	@Override
	public void onDouble(double value) {
	}

	@Override
	public void onNull() {
	}

	@Override
	public void onDocumentBegin() {
	}

	@Override
	public void onDocumentEnd() {
	}

	@Override
	public void onDocumentPrepare() {
	}

	@Override
	public void onSetLocator(IJsonLocator locator) {
		this.mLocator = locator;
	}

	@Override
	public void onException(Exception exception) {

		System.out.println(this + ".onException(), line:"
				+ this.mLocator.getLine() + ", col:"
				+ this.mLocator.getColumn());
		exception.printStackTrace();

	}

	@Override
	public void onDocumentFinal() {
		this.mLocator = null;
	}

}

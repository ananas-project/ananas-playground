package ananas.lib.servkit.json.parser;


public class DefaultJsonHandler implements IJsonHandler {

	private IJsonLocator mLocator;

	@Override
	public void onArrayBegin() {
		// TODO Auto-generated method stub
		System.out.println(this + ".onArrayBegin");
	}

	@Override
	public void onArrayEnd() {
		// TODO Auto-generated method stub
		System.out.println(this + ".onArrayEnd");

	}

	@Override
	public void onObjectBegin() {
		// TODO Auto-generated method stub
		System.out.println(this + ".onObjectBegin");

	}

	@Override
	public void onObjectEnd() {
		// TODO Auto-generated method stub
		System.out.println(this + ".onObjectEnd");

	}

	@Override
	public void onObjectKey(String key) {
		// TODO Auto-generated method stub
		System.out.println(this + ".onObjectKey");

	}

	@Override
	public void onString(String s) {
		// TODO Auto-generated method stub
		System.out.println(this + ".onString");

	}

	@Override
	public void onBoolean(boolean value) {
		// TODO Auto-generated method stub
		System.out.println(this + ".onBoolean");

	}

	@Override
	public void onInteger(int value) {
		// TODO Auto-generated method stub
		System.out.println(this + ".onInteger");

	}

	@Override
	public void onLong(long value) {
		// TODO Auto-generated method stub
		System.out.println(this + ".onLong");

	}

	@Override
	public void onDouble(double value) {
		// TODO Auto-generated method stub
		System.out.println(this + ".onDouble");

	}

	@Override
	public void onNull() {
		// TODO Auto-generated method stub
		System.out.println(this + ".onNull");

	}

	@Override
	public void onDocumentBegin() {
		// TODO Auto-generated method stub
		System.out.println(this + ".onDocumentBegin");

	}

	@Override
	public void onDocumentEnd() {
		// TODO Auto-generated method stub
		System.out.println(this + ".onDocumentEnd");

	}

	@Override
	public void onDocumentPrepare() {
		// TODO Auto-generated method stub
		System.out.println(this + ".onDocumentPrepare");

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

}

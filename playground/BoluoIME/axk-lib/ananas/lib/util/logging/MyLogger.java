package ananas.lib.util.logging;

class MyLogger implements Logger {

	public MyLogger(String name) {

	}

	@Override
	public void setLevel(Level level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void trace(String string) {
		this._print(string);
	}

	private void _print(String string) {
		System.out.println(string);
	}

	@Override
	public void warn(String string) {
		this._printErr(string);
	}

	private void _printErr(String string) {
		System.err.println(string);
	}

	@Override
	public void info(String string) {
		this._print(string);
	}

	@Override
	public void error(String string) {
		this._printErr(string);
	}

	@Override
	public void error(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void error(String message, Throwable e) {

		this._printErr(message);
		e.printStackTrace();

	}

}

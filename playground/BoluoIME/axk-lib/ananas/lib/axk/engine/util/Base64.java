package ananas.lib.axk.engine.util;

public abstract class Base64 {

	// abstract method

	public abstract byte[] doDecode(String string);

	public abstract String doEncode(byte[] data);

	// static method

	public static Base64 getInstance() {
		return ImplBase64._theInstance();
	}

	public static byte[] decode(String string) {
		return Base64.getInstance().doDecode(string);
	}

	public static String encode(byte[] data) {
		return Base64.getInstance().doEncode(data);
	}

}

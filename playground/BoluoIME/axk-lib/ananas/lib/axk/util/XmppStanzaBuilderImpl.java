package ananas.lib.axk.util;

import java.io.UnsupportedEncodingException;

final class XmppStanzaBuilderImpl implements XmppStanzaBuilder {

	private static XmppStanzaBuilderFactory s_factory;

	private final StringBuilder mSB = new StringBuilder(32);

	public XmppStanzaBuilderImpl() {
	}

	public static XmppStanzaBuilderFactory getFactory() {
		XmppStanzaBuilderFactory fact = s_factory;
		if (fact == null) {
			s_factory = fact = new MyFactory();
		}
		return fact;
	}

	static class MyFactory implements XmppStanzaBuilderFactory {

		@Override
		public XmppStanzaBuilder newInstance() {
			return new XmppStanzaBuilderImpl();
		}

	}

	@Override
	public void append(String string) {
		this.mSB.append(string);
	}

	@Override
	public String toString() {
		return this.mSB.toString();
	}

	@Override
	public byte[] toByteArray() {
		try {
			return this.mSB.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	@Override
	public char[] toCharArray() {
		int len = this.mSB.length();
		char[] buff = new char[len];
		this.mSB.getChars(0, len, buff, 0);
		return buff;
	}

	@Override
	public void reset() {
		this.mSB.setLength(0);
	}

	@Override
	public void appendAttribute(String qName, String value) {
		this.mSB.append(' ');
		this.mSB.append(qName);
		this.mSB.append("='");
		this.append(value, true);
		this.mSB.append('\'');
	}

	@Override
	public void append(String string, boolean doEscape) {
		if (!doEscape) {
			this.mSB.append(string);
			return;
		}
		// TODO escape
		this.mSB.append(string);
	}

}

package ananas.lib.axk.util;

public interface XmppStanzaBuilder {

	class Factory {

		private static XmppStanzaBuilderFactory getFactory() {
			return XmppStanzaBuilderImpl.getFactory();
		}

		public static XmppStanzaBuilder newInstance() {
			return getFactory().newInstance();
		}

	}

	void append(String string);

	void append(String string, boolean doEscape);

	byte[] toByteArray();

	char[] toCharArray();

	void reset();

	void appendAttribute(String qName, String value);
}

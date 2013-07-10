package ananas.lib.axk.constant;

public interface XmppSubscription {

	XmppSubscription none = Factory.newInstance("none");
	XmppSubscription to = Factory.newInstance("to");
	XmppSubscription from = Factory.newInstance("from");
	XmppSubscription both = Factory.newInstance("both");

	/**
	 * implements
	 * */

	class Factory {

		public static XmppSubscription getInstance(String string) {
			if (s_set == null) {
				s_set = new AbstractXmppConstSet(none);
			}
			return (XmppSubscription) s_set.get(string);
		}

		private static AbstractXmppConstSet s_set;

		private static XmppSubscription newInstance(String string) {
			return new MyImpl(string);
		}

		private static class MyImpl extends AbstractXmppConst implements
				XmppSubscription {

			public MyImpl(String text) {
				super(text);
			}
		}
	}

}

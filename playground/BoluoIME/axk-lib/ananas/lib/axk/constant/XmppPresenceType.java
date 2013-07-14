package ananas.lib.axk.constant;

public interface XmppPresenceType {

	XmppPresenceType subscribe = Factory.newInstance("subscribe");
	XmppPresenceType subscribed = Factory.newInstance("subscribed");
	XmppPresenceType unsubscribed = Factory.newInstance("unsubscribed");
	XmppPresenceType unsubscribe = Factory.newInstance("unsubscribe");
	XmppPresenceType unavailable = Factory.newInstance("unavailable");
	XmppPresenceType error = Factory.newInstance("error");
	XmppPresenceType probe = Factory.newInstance("probe");

	/**
	 * implements
	 * */

	class Factory {

		public static XmppPresenceType getInstance(String string) {
			if (s_set == null) {
				s_set = new AbstractXmppConstSet(unavailable);
			}
			return (XmppPresenceType) s_set.get(string);
		}

		private static AbstractXmppConstSet s_set;

		private static XmppPresenceType newInstance(String string) {
			return new MyImpl(string);
		}

		private static class MyImpl extends AbstractXmppConst implements
				XmppPresenceType {

			public MyImpl(String text) {
				super(text);
			}
		}
	}

}

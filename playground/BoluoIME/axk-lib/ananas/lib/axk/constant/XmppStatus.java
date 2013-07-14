package ananas.lib.axk.constant;

public interface XmppStatus {

	XmppStatus init = Factory.newInstance("init");
	XmppStatus online = Factory.newInstance("online");
	XmppStatus offline = Factory.newInstance("offline");
	XmppStatus dropped = Factory.newInstance("dropped");

	XmppStatus connect = Factory.newInstance("connect");
	XmppStatus error = Factory.newInstance("error");
	XmppStatus closed = Factory.newInstance("closed");
	XmppStatus starttls = Factory.newInstance("tls");
	XmppStatus startsasl = Factory.newInstance("sasl");
	XmppStatus bind = Factory.newInstance("bind");

	class Factory {

		public static XmppStatus getInstance(String string) {
			if (s_set == null) {
				s_set = new AbstractXmppConstSet(init);
			}
			return (XmppStatus) s_set.get(string);
		}

		private static AbstractXmppConstSet s_set;

		private static XmppStatus newInstance(String string) {
			return new MyImpl(string);
		}

		private static class MyImpl extends AbstractXmppConst implements
				XmppStatus {

			public MyImpl(String text) {
				super(text);
			}
		}
	}

}

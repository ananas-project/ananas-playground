package ananas.lib.axk.constant;

public interface XmppShow {

	XmppShow away = Factory.newInstance("away");
	XmppShow chat = Factory.newInstance("chat");
	XmppShow dnd = Factory.newInstance("dnd");
	XmppShow xa = Factory.newInstance("xa");
	XmppShow empty = Factory.newInstance("");

	class Factory {

		public static XmppShow getInstance(String string) {
			if (s_set == null) {
				s_set = new AbstractXmppConstSet(empty);
			}
			return (XmppShow) s_set.get(string);
		}

		private static AbstractXmppConstSet s_set;

		private static XmppShow newInstance(String string) {
			return new MyImpl(string);
		}

		private static class MyImpl extends AbstractXmppConst implements
				XmppShow {

			public MyImpl(String text) {
				super(text);
			}
		}
	}

}

package ananas.lib.axk;

public interface XmppClient extends XmppCommandDispatcher {

	void setXmppEventListener(XmppEventListener listener);

	XmppClientExAPI getExAPI(Class<? extends XmppClientExAPI> apiClass);

}

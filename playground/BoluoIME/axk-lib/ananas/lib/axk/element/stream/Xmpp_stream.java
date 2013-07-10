package ananas.lib.axk.element.stream;

public class Xmpp_stream {

	private XmppStreamListener mListener;

	public XmppStreamListener getListener() {
		return this.mListener;
	}

	public void setListener(XmppStreamListener listener) {
		this.mListener = listener;
	}

	final Xmpp_stream_logger logger = new Xmpp_stream_logger();

	public void dispatchRxObject(Object object) {
		this.logger.log(this, object);
		this.mListener.onReceive(this, object);
	}

}

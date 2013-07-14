package ananas.lib.axk;

public interface XmppAddress {

	String getUser();

	String getHost();

	String getResource();

	XmppAddress toFull();

	XmppAddress toPure();

	String toFullString();

	String toPureString();

	boolean isFull();

	boolean isPure();
}

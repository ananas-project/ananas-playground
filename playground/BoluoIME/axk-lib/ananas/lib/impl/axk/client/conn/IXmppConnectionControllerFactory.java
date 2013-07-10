package ananas.lib.impl.axk.client.conn;

public interface IXmppConnectionControllerFactory {

	IXmppConnectionController newController(XmppConnection conn);

}

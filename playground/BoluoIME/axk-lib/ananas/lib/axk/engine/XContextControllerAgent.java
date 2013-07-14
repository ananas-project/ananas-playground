package ananas.lib.axk.engine;

public interface XContextControllerAgent extends XEngineListener {

	XContextController getContextController();

	void setContextController(XContextController ctrl);

	void setBindedFullJID(String data);

	String getBindedFullJID();

}

package ananas.lib.axk;

public class DefaultXmppAccount implements XmppAccount {

	public int port = 5222;
	public String host;
	public String password;
	public XmppAddress address;
	public boolean useSSL = false;
	public boolean ignoreSSLError = false;
	public String resource = "axk-client";

	public DefaultXmppAccount() {
	}

	public DefaultXmppAccount(XmppAccount acc) {
		this.address = acc.getAddress();
		this.password = acc.getPassword();
		this.host = acc.getHost();
		this.port = acc.getPort();
		this.resource = acc.getResource();
		this.useSSL = acc.isUseSSL();
		this.ignoreSSLError = acc.isIgnoreSSLError();
	}

	public void setAddress(String addr) {
		this.address = new DefaultXmppAddress(addr);
	}

	@Override
	public String getHost() {
		return this.host;
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public XmppAddress getAddress() {
		return this.address;
	}

	@Override
	public String getResource() {
		return this.resource;
	}

	@Override
	public boolean isUseSSL() {
		return this.useSSL;
	}

	@Override
	public boolean isIgnoreSSLError() {
		return this.ignoreSSLError;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(this.getClass().getName());
		sb.append(" jid=" + this.address);
		sb.append(" password=" + this.password);
		sb.append(" host=" + this.host);
		sb.append(" port=" + this.port);
		sb.append(" resource=" + this.resource);
		sb.append(" useSSL=" + this.useSSL);
		sb.append(" ignoreSSLError=" + this.ignoreSSLError);
		sb.append("]");
		return sb.toString();
	}

}

package ananas.lib.axk.engine.util;

import ananas.lib.axk.engine.XAccount;

public class DefaultXAccount implements XAccount {

	public String jid;
	public String host;
	public int port;
	public boolean useSSL;
	public String password;
	public String resource;

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public String getHost() {
		return this.host;
	}

	@Override
	public String getJID() {
		return this.jid;
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
	public String getPassword() {
		return this.password;
	}

}

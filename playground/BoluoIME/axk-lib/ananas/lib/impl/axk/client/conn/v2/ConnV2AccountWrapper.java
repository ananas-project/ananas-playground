package ananas.lib.impl.axk.client.conn.v2;

import ananas.lib.axk.XmppAccount;
import ananas.lib.axk.engine.XAccount;

public class ConnV2AccountWrapper implements XAccount {

	private final XmppAccount m_target;

	public ConnV2AccountWrapper(XmppAccount target) {
		this.m_target = target;
	}

	@Override
	public int getPort() {
		return this.m_target.getPort();
	}

	@Override
	public String getHost() {
		return this.m_target.getHost();
	}

	@Override
	public String getJID() {
		return this.m_target.getAddress().toPureString();
	}

	@Override
	public String getResource() {
		return this.m_target.getResource();
	}

	@Override
	public boolean isUseSSL() {
		return this.m_target.isUseSSL();
	}

	@Override
	public String getPassword() {
		return this.m_target.getPassword();
	}

}

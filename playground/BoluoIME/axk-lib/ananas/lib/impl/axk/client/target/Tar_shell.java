package ananas.lib.impl.axk.client.target;

import ananas.lib.axk.DefaultXmppAccount;
import ananas.lib.axk.XmppAccount;
import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.XmppClient;
import ananas.lib.axk.XmppClientExAPI;
import ananas.lib.axk.XmppEnvironment;
import ananas.lib.axk.api.IExConnection;
import ananas.lib.axk.api.IExCore;
import ananas.lib.axk.api.IExShell;
import ananas.lib.axk.constant.XmppStatus;
import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;

public class Tar_shell extends Tar_abstractClient implements IExShell {

	private final static Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();

	@Override
	public XmppClientExAPI getExAPI(Class<? extends XmppClientExAPI> apic) {

		if (apic == null) {
			return null;

		} else if (apic.equals(IExShell.class)) {
			IExShell api = this;
			return api;

		} else if (apic.equals(IExCore.class)) {
			IExCore api = this;
			return api;

		} else if (apic.equals(IExConnection.class)) {
			IExConnection api = this;
			return api;

		} else {
			return super.getExAPI(apic);
		}
	}

	private IExConnection mTargetConn;
	private IExCore mTargetCore;
	private DefaultXmppAccount mAccountCache;

	private IExConnection _getTargetConnection() {
		IExConnection api = this.mTargetConn;
		if (api == null) {
			api = (IExConnection) super.getExAPI(IExConnection.class);
			this.mTargetConn = api;
			logger.trace("find api : " + api);
		}
		return api;
	}

	private IExCore _getTargetCore() {
		IExCore api = this.mTargetCore;
		if (api == null) {
			api = (IExCore) super.getExAPI(IExCore.class);
			this.mTargetCore = api;
		}
		return api;
	}

	@Override
	public XmppEnvironment getEnvironment() {
		IExCore core = this._getTargetCore();
		return core.getEnvironment();
	}

	@Override
	public XmppAccount getAccount() {
		DefaultXmppAccount acc = this.mAccountCache;
		if (acc == null) {
			IExCore core = this._getTargetCore();
			acc = new DefaultXmppAccount(core.getAccount());
			acc.password = "******";
			this.mAccountCache = acc;
		}
		return acc;
	}

	@Override
	public void setStatus(XmppStatus phase) {
		IExConnection conn = this._getTargetConnection();
		conn.setStatus(phase);
	}

	@Override
	public XmppStatus getStatus() {
		IExConnection conn = this._getTargetConnection();
		return conn.getStatus();
	}

	@Override
	public XmppStatus getPhase() {
		IExConnection conn = this._getTargetConnection();
		return conn.getPhase();
	}

	@Override
	public void close() {
		IExConnection conn = this._getTargetConnection();
		conn.close();
	}

	@Override
	public void connect() {
		IExConnection conn = this._getTargetConnection();
		conn.connect();
	}

	@Override
	public void disconnect() {
		IExConnection conn = this._getTargetConnection();
		conn.disconnect();
	}

	@Override
	public void reset() {
		IExConnection conn = this._getTargetConnection();
		conn.reset();
	}

	@Override
	public boolean sendStanza(byte[] buffer, int offset, int length) {
		IExConnection conn = this._getTargetConnection();
		return conn.sendStanza(buffer, offset, length);
	}

	@Override
	public boolean sendStanza(String string) {
		IExConnection conn = this._getTargetConnection();
		return conn.sendStanza(string);
	}

	@Override
	public XmppClient getOuter() {
		IExCore core = this._getTargetCore();
		return core.getOuter();
	}

	@Override
	public XmppAddress getBindingJID() {
		IExConnection conn = this._getTargetConnection();
		return conn.getBindingJID();
	}

}

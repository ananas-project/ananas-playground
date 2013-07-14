package ananas.lib.axk;

import java.net.URI;

public class DefaultXmppAddress implements XmppAddress {

	private final String mUser;
	private final String mHost;
	private final String mResource;// MUST start with '/'
	private String mFullString;
	private String mPureString;

	public DefaultXmppAddress(String str) {

		final URI uri = URI.create("xmpp://" + str);
		this.mUser = uri.getUserInfo();
		this.mHost = uri.getHost();

		final String res = uri.getPath();

		if (res == null) {
			this.mResource = null;
		} else {
			if (res.length() > 0)
				this.mResource = res;
			else
				this.mResource = null;
		}
	}

	public String getUser() {
		return this.mUser;
	}

	public String getHost() {
		return this.mHost;
	}

	public String getResource() {
		return this.mResource;
	}

	private String _toFullString() {
		String res = this.mResource;
		if (res == null) {
			res = "";
		}
		return (this.mUser + "@" + this.mHost + res);
	}

	private String _toPureString() {
		return (this.mUser + "@" + this.mHost);
	}

	@Override
	public String toString() {
		return this.toFullString();
	}

	@Override
	public boolean isFull() {
		return (this.mResource != null);
	}

	@Override
	public boolean isPure() {
		return (this.mResource == null);
	}

	@Override
	public int hashCode() {
		String str = this.toFullString();
		return str.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XmppAddress) {
			XmppAddress obj2 = (XmppAddress) obj;
			String str1 = this.toFullString();
			String str2 = obj2.toFullString();
			return str1.equals(str2);
		} else {
			return false;
		}
	}

	@Override
	public XmppAddress toFull() {
		if (this.isFull()) {
			return this;
		}
		return new DefaultXmppAddress(this.toFullString());
	}

	@Override
	public XmppAddress toPure() {
		if (this.isPure()) {
			return this;
		}
		return new DefaultXmppAddress(this.toPureString());
	}

	@Override
	public String toFullString() {
		String str = this.mFullString;
		if (str == null) {
			str = this._toFullString();
			this.mFullString = str;
		}
		return str;
	}

	@Override
	public String toPureString() {
		String str = this.mPureString;
		if (str == null) {
			str = this._toPureString();
			this.mPureString = str;
		}
		return str;
	}

}

package ananas.lib.axk.element.iq_roster;

import java.util.ArrayList;
import java.util.List;

import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.constant.XmppSubscription;

public class Xmpp_item {

	private List<Xmpp_group> mGroupList;
	private XmppSubscription mSubscription;
	private String mName;
	private XmppAddress mJID;

	public void setJID(XmppAddress value) {
		this.mJID = value;
	}

	public void setName(String value) {
		this.mName = value;
	}

	public void setSubscription(XmppSubscription value) {
		this.mSubscription = value;
	}

	public XmppSubscription getSubscription() {
		return mSubscription;
	}

	public String getName() {
		return mName;
	}

	public XmppAddress getJID() {
		return mJID;
	}

	public List<Xmpp_group> getGroupList(boolean create) {
		List<Xmpp_group> list = this.mGroupList;
		if (list == null && create) {
			list = new ArrayList<Xmpp_group>();
			this.mGroupList = list;
		}
		return list;
	}

	public void appendGroup(Xmpp_group group) {
		this.getGroupList(true).add(group);
	}

}

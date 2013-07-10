package ananas.lib.axk.api.roster;

import java.util.Collection;

import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.constant.XmppSubscription;

public interface IRosterContact {

	IExRosterManager getRoster();

	int getRevision();

	void setPrepareToPush(boolean value);

	boolean isPrepareToPush();

	Collection<IRosterGroup> getOwnerGroups();

	// attribute
	String getName();

	void setName(String name);

	XmppAddress getAddress();

	void setSubscription(XmppSubscription subscription);

	XmppSubscription getSubscription();

	// binding
	boolean bind(IRosterGroup group);

	boolean unbind(IRosterGroup group);

}

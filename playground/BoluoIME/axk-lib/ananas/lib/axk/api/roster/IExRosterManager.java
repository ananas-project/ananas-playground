package ananas.lib.axk.api.roster;

import java.util.Collection;

import ananas.lib.axk.XmppAddress;
import ananas.lib.axk.XmppClientExAPI;

public interface IExRosterManager extends XmppClientExAPI {

	// network methods

	void pull();

	void push();

	// local methods

	int getRevision();

	void reset();

	boolean isAutoPullAfterBinding();

	void setAutoPullAfterBinding(boolean value);

	IRosterGroup getGroup(String group);

	IRosterGroup getGroup(String group, boolean create);

	IRosterContact getContact(XmppAddress addr);

	IRosterContact getContact(XmppAddress addr, boolean create);

	Collection<IRosterContact> getContacts();

	Collection<IRosterGroup> getGroups();

	IRosterContact addContact(XmppAddress addr, String[] ownerGroups);

	boolean removeContact(XmppAddress addr);

	boolean removeContact(IRosterContact contact);

	boolean removeGroup(String groupName);

	boolean removeGroup(IRosterGroup group);

}

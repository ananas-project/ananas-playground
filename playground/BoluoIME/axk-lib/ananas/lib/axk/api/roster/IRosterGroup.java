package ananas.lib.axk.api.roster;

import java.util.Collection;

public interface IRosterGroup {

	int getRevision();

	IExRosterManager getRoster();

	Collection<IRosterContact> getContacts();

	// binding
	boolean bind(IRosterContact contact);

	boolean unbind(IRosterContact contact);

	// attribute
	String getName();

}

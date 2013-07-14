package ananas.lib.impl.axk.client.target.roster;

import ananas.lib.axk.api.roster.IExRosterManager;

public interface IRosterManagerInner {

	IExRosterManager toOuter();

	IRosterManagerInnerCallback getCallback();
}

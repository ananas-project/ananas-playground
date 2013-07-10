package ananas.lib.axk.command;

import ananas.lib.axk.stanza.StanzaContext;
import ananas.lib.axk.stanza.StanzaContextRef;

public class StanzaCommand extends DataCommand implements StanzaContextRef {

	private StanzaContext m_target;

	@Override
	public StanzaContext getTarget() {
		return this.m_target;
	}

	@Override
	public void setTarget(StanzaContext target) {
		this.m_target = target;
	}

}

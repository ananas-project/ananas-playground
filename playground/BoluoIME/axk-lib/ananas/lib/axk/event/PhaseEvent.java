package ananas.lib.axk.event;

import ananas.lib.axk.constant.XmppStatus;

public interface PhaseEvent extends XmppClientEvent {

	XmppStatus getOldPhase();

	XmppStatus getNewPhase();

}

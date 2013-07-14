package ananas.lib.axk.event;

import ananas.lib.axk.constant.XmppStatus;

public interface StatusEvent extends XmppClientEvent {

	XmppStatus getOldStatus();

	XmppStatus getNewStatus();

}

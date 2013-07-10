package ananas.lib.axk.command;

import ananas.lib.axk.XmppClient;
import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;

public class TraceCommand extends ControlCommand {

	private final static Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();

	@Override
	public void onSendByClient(XmppClient client) {
		logger.trace("    cross : " + client);
	}

}

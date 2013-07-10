package ananas.lib.axk.api;

import ananas.lib.axk.XmppAccount;
import ananas.lib.axk.XmppClient;
import ananas.lib.axk.XmppClientExAPI;
import ananas.lib.axk.XmppEnvironment;

public interface IExCore extends XmppClientExAPI {

	XmppEnvironment getEnvironment();

	XmppAccount getAccount();

	XmppClient getOuter();

}

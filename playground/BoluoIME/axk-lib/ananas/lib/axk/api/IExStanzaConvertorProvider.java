package ananas.lib.axk.api;

import ananas.lib.axk.XmppClientExAPI;
import ananas.lib.axk.stanza.StanzaConvertor;

public interface IExStanzaConvertorProvider extends XmppClientExAPI {

	StanzaConvertor getStanzaConvertor();

}

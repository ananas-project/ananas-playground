package ananas.lib.impl.axk;

import java.util.Vector;

import ananas.lib.axk.XmppEnvironment;
import ananas.lib.axk.XmppEnvironmentLoader;
import ananas.lib.blueprint3.lang.BPNamespace;

public class TheXmppEnvironmentLoader implements XmppEnvironmentLoader {

	@Override
	public XmppEnvironment load() {
		final XmppEnvironment envi = TheXmppEnvironment.getInstance();
		final String uri = "http://etherx.jabber.org/streams";
		final BPNamespace ns = envi.getBPEnvironment().getNamespaceRegistrar()
				.getNamespace(uri);
		if (ns == null) {
			final Vector<String> v = new Vector<String>();

			// v.add("ananas.lib.axk.element.stream.TheNamespaceInfo");
			// v.add("ananas.lib.axk.element.xmpp_tls.TheNamespaceInfo");
			v.add("ananas.lib.axk.element.TheNamespaceInfo");

			for (String s : v) {
				envi.getBPEnvironment().loadNamespace(s, true);
			}
		}
		return envi;
	}

}

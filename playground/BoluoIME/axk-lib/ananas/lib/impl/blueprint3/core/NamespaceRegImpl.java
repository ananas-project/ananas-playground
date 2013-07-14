package ananas.lib.impl.blueprint3.core;

import java.util.HashMap;
import java.util.Map;

import ananas.lib.blueprint3.lang.BPNamespace;
import ananas.lib.blueprint3.lang.BPNamespaceRegistrar;
import ananas.lib.util.logging.AbstractLoggerFactory;
import ananas.lib.util.logging.Logger;

public class NamespaceRegImpl implements BPNamespaceRegistrar {

	private final static Logger logger = (new AbstractLoggerFactory() {
	}).getLogger();

	private final Map<String, BPNamespace> mNsTable;

	public NamespaceRegImpl() {
		this.mNsTable = new HashMap<String, BPNamespace>();
	}

	@Override
	public boolean registerNamespace(BPNamespace ns) {

		logger.trace("reg namespace - " + ns.getNamespaceURI());

		String uri = ns.getNamespaceURI();
		this.mNsTable.put(uri, ns);
		return true;
	}

	@Override
	public boolean unregisterNamespace(BPNamespace pkg) {
		String uri = pkg.getNamespaceURI();
		this.mNsTable.remove(uri);
		return true;
	}

	@Override
	public BPNamespace getNamespace(String nsURI) {
		return this.mNsTable.get(nsURI);
	}

}

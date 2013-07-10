package ananas.lib.blueprint3.loader.eom;

import java.net.URI;

import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPDocumentGroup;
import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.lang.BPNamespace;
import ananas.lib.blueprint3.lang.BPNamespaceRegistrar;
import ananas.lib.blueprint3.lang.BlueprintException;
import ananas.lib.blueprint3.loader.eom.ctrl.Ctrl_eom;
import ananas.lib.blueprint3.loader.eom.target.Tar_eom;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceInfo;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceLoader;
import ananas.lib.util.ClassUriGen;

public class EomNamespaceLoader implements BPNamespaceLoader {

	@Override
	public void load(BPEnvironment envi, BPNamespaceInfo info)
			throws BlueprintException {

		String xmlfile = info.getProperty("eom.xml");

		URI uri = ClassUriGen.getURI(info.getClass(), xmlfile);

		try {
			BPDocumentGroup group = envi.getImplementation()
					.createDocumentGroup(envi);
			BPDocument doc = group.openDocument(uri);

			Ctrl_eom eom = (Ctrl_eom) doc.getRootElement();
			Tar_eom teom = eom.getTarget_eom();

			// System.out.println(eom);

			// IEomNamespaceRegFactory regf = new
			// DefaultEomNamespaceRegFactory();
			// IEomNamespaceReg reg = regf.newReg(teom);

			// reg.load(envi);
			// reg.check(envi);
			// reg.register(envi);

			this.loadNS(envi, teom);

		} catch (Exception e) {
			if (e instanceof BlueprintException) {
				throw (BlueprintException) e;
			} else if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	private void loadNS(BPEnvironment envi, Tar_eom teom) {
		MyBpnsFactory f = new MyBpnsFactory(envi, teom);
		BPNamespace[] nss = f.build();
		BPNamespaceRegistrar nsreg = envi.getNamespaceRegistrar();
		for (BPNamespace ns : nss) {
			nsreg.registerNamespace(ns);
		}
	}

}

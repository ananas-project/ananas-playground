package ananas.lib.blueprint3.loader.reflect;

import java.io.InputStream;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.lang.BlueprintException;
import ananas.lib.blueprint3.loader.reflect.dom.RefDocument;
import ananas.lib.blueprint3.loader.reflect.dom.RefElement_reflect;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceInfo;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceLoader;

public class ReflectNamespaceLoader implements BPNamespaceLoader {

	@Override
	public void load(BPEnvironment envi, BPNamespaceInfo info)
			throws BlueprintException {

		InputStream in = null;

		try {
			String xmlFileName = info.getProperty("reflect.xml");
			if (xmlFileName == null) {
				throw new BlueprintException(
						"The property named 'reflect.xml' is null.");
			}
			in = info.getClass().getResourceAsStream(xmlFileName);
			if (in == null) {
				throw new BlueprintException("Cannot open resource : " + info
						+ "#" + xmlFileName);
			}
			XMLReader rdr = envi.getXMLReaderFactory().newReader();
			MyReflectConfigBuilder builder = new MyReflectConfigBuilder();
			rdr.setContentHandler(builder);
			rdr.parse(new InputSource(in));

			RefDocument doc = builder.getDocument();
			// doc.printSelf(System.out);
			RefElement_reflect root = doc.getRoot();
			root.regNamespaces(envi);

		} catch (Exception e) {

			if (e instanceof BlueprintException) {
				throw (BlueprintException) e;
			} else {
				throw new BlueprintException(e);
			}

		}

		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception e) {
			if (e instanceof BlueprintException) {
				throw (BlueprintException) e;
			} else {
				throw new BlueprintException(e);
			}
		}
	}

}

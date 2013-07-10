package ananas.lib.blueprint3.kit;

import ananas.lib.blueprint3.Blueprint;
import ananas.lib.blueprint3.BlueprintFactory;
import ananas.lib.blueprint3.kit.docloader.DefaultSchemeDocLoader;
import ananas.lib.blueprint3.kit.docloader.DefaultTypeDocLoader;
import ananas.lib.blueprint3.kit.docloader.scheme.ClassSchemeDocLoader;
import ananas.lib.blueprint3.kit.docloader.type.XmlTypeDocLoader;
import ananas.lib.blueprint3.lang.BPDocumentLoaderFactoryRegistrar;
import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.lang.BPFileNameMapper;

public class BlueprintFactoryImpl implements BlueprintFactory {

	@Override
	public Blueprint newBlueprint() {

		String classname = "ananas.lib.impl.blueprint3.core.BlueprintFactoryImpl";
		BlueprintFactory factory = Blueprint.Util.getFactory(classname);
		Blueprint bp = factory.newBlueprint();
		BPEnvironment envi = bp.defaultEnvironment();

		this.initSchemeLoader(envi);
		this.initTypeLoader(envi);
		this.initTypeMapper(envi);

		return bp;
	}

	private void initSchemeLoader(BPEnvironment envi) {
		BPDocumentLoaderFactoryRegistrar reg = envi.getUriSchemeRegistrar();
		reg.setDefaultFactory(DefaultSchemeDocLoader.getFacotry());
		reg.registerFactory("class", ClassSchemeDocLoader.getFactory());
	}

	private void initTypeMapper(BPEnvironment envi) {
		BPFileNameMapper mapper = envi.getFileNameMapper();
		mapper.register(".xml", "text/xml");
	}

	private void initTypeLoader(BPEnvironment envi) {
		BPDocumentLoaderFactoryRegistrar reg = envi.getContentTypeRegistrar();
		reg.setDefaultFactory(DefaultTypeDocLoader.getFacotry());
		reg.registerFactory("text/xml", XmlTypeDocLoader.getFactory());
	}

}

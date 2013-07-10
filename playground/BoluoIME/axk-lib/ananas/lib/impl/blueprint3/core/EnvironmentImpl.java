package ananas.lib.impl.blueprint3.core;

import java.io.IOException;
import java.net.URI;

import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPDocumentGroup;
import ananas.lib.blueprint3.dom.BPImplementation;
import ananas.lib.blueprint3.lang.BPDocumentLoaderFactoryRegistrar;
import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.lang.BPFileNameMapper;
import ananas.lib.blueprint3.lang.BPNamespaceRegistrar;
import ananas.lib.blueprint3.lang.BlueprintException;
import ananas.lib.blueprint3.util.BPBuilderFactory;
import ananas.lib.blueprint3.util.BPVisitorFactory;
import ananas.lib.blueprint3.util.BPXMLReaderFactory;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceInfo;
import ananas.lib.blueprint3.util.nsloader.BPNamespaceLoaderFactory;
import ananas.lib.blueprint3.xml.serializer.BPXmlSerializerFactory;
import ananas.lib.io.Connector;
import ananas.lib.io.impl.DefaultConnector;

public class EnvironmentImpl implements BPEnvironment {

	private final BPNamespaceRegistrar mNsReg;
	private final BPImplementation mImplementation;
	private final BPXMLReaderFactory mParserFactory;
	private BPXmlSerializerFactory mSerializerFactory;
	private final BPBuilderFactory mBuilderFactory;
	private BPVisitorFactory mVisitorFactory;
	private final Connector mConnector;
	private NsLoadingManager mNsLoadingManager;
	private final BPNamespaceLoaderFactory mNsLoaderFactory;
	private final BPDocumentLoaderFactoryRegistrar mTypeToFactoryReg;
	private final BPDocumentLoaderFactoryRegistrar mSchemeToFactoryReg;
	private final BPFileNameMapper mFileNameMapper;

	public EnvironmentImpl() {
		this.mConnector = new DefaultConnector();
		this.mImplementation = new ImplementationImpl();
		this.mParserFactory = new ParserFactoryImpl();
		this.mBuilderFactory = new BuilderFactoryImpl();
		this.mNsReg = new NamespaceRegImpl();
		this.mNsLoaderFactory = new MainNsLoaderFactory();
		this.mSchemeToFactoryReg = new BPDocumentLoaderFactoryRegistrarImpl(
				"scheme");
		this.mTypeToFactoryReg = new BPDocumentLoaderFactoryRegistrarImpl(
				"content-type");
		this.mFileNameMapper = new BPFileNameMapperImpl();
	}

	@Override
	public BPNamespaceRegistrar getNamespaceRegistrar() {
		return this.mNsReg;
	}

	@Override
	public BPImplementation getImplementation() {
		return this.mImplementation;
	}

	@Override
	public BPXmlSerializerFactory getXmlSerializerFactory() {
		return this.mSerializerFactory;
	}

	@Override
	public BPBuilderFactory getBuilderFactory() {
		return this.mBuilderFactory;
	}

	@Override
	public BPVisitorFactory getVisitorFactory() {
		return this.mVisitorFactory;
	}

	@Override
	public Connector getConnector() {
		return this.mConnector;
	}

	@Override
	public BPXMLReaderFactory getXMLReaderFactory() {
		return this.mParserFactory;
	}

	@Override
	public void loadNamespace(String aClassName, boolean lazy)
			throws BlueprintException {
		try {
			Class<?> cls = Class.forName(aClassName);
			this.loadNamespace(cls, lazy);
		} catch (BlueprintException e) {
			throw e;
		} catch (Exception e) {
			throw new BlueprintException(e);
		}
	}

	@Override
	public void loadNamespace(Class<?> aClass, boolean lazy)
			throws BlueprintException {
		try {
			final BPNamespaceInfo info = (BPNamespaceInfo) aClass.newInstance();
			this.loadNamespace(info, lazy);
		} catch (BlueprintException e) {
			throw e;
		} catch (Exception e) {
			throw new BlueprintException(e);
		}
	}

	@Override
	public void loadNamespace(BPNamespaceInfo info, boolean lazy)
			throws BlueprintException {
		NsLoadingManager nslm = this.mNsLoadingManager;
		if (nslm == null) {
			nslm = new NsLoadingManager();
			this.mNsLoadingManager = nslm;
		}
		nslm.load(this, info, lazy);
	}

	@Override
	public BPNamespaceLoaderFactory getNamespaceLoaderFactory() {
		return this.mNsLoaderFactory;
	}

	@Override
	public BPFileNameMapper getFileNameMapper() {
		return this.mFileNameMapper;
	}

	@Override
	public BPDocumentLoaderFactoryRegistrar getContentTypeRegistrar() {
		return this.mTypeToFactoryReg;
	}

	@Override
	public BPDocumentLoaderFactoryRegistrar getUriSchemeRegistrar() {
		return this.mSchemeToFactoryReg;
	}

	@Override
	public BPDocument loadDocument(URI uri) throws IOException {
		BPEnvironment envi = this;
		BPDocumentGroup group = envi.getImplementation().createDocumentGroup(
				envi);
		return group.openDocument(uri, true, true);
	}

}

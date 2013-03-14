package ananas.tools.dtt.impl;

import java.io.File;
import java.util.Properties;

import ananas.lib.blueprint3.core.Blueprint;
import ananas.lib.blueprint3.core.dom.BPDocument;
import ananas.lib.blueprint3.core.lang.BPEnvironment;
import ananas.tools.dtt.Const;
import ananas.tools.dtt.DailyContext;
import ananas.tools.dtt.DailyFactory;
import ananas.tools.dtt.dom.Ctrl_timer;

public class DailyContextImpl implements DailyContext {

	private final File mFileConf;
	private final File mFileRec;

	public DailyContextImpl(Properties prop) {
		this.mFileConf = new File(prop.getProperty(Const.key_file_conf));
		this.mFileRec = new File(prop.getProperty(Const.key_file_current));
	}

	@Override
	public File getConfigFile() {
		return this.mFileConf;
	}

	@Override
	public File getRecordFile() {
		return this.mFileRec;
	}

	static final String[] nsList = {
			"ananas.lib.blueprint3.loader.eom.EomReflectInfo",
			"ananas.tools.dtt.dom.NSInfo" };

	@Override
	public DailyFactory getFactory() {
		try {

			BPEnvironment envi = Blueprint.getInstance().defaultEnvironment();
			for (String s : nsList) {
				envi.loadNamespace(s, true);
			}

			String url = "file://" + this.mFileConf.getAbsolutePath();
			BPDocument doc = Blueprint.loadDocument(url);
			Ctrl_timer conf = (Ctrl_timer) doc.getRootElement();
			return new DailyFactoryImpl(this, conf);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

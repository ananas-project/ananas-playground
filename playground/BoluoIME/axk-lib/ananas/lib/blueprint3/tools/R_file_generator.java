package ananas.lib.blueprint3.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ananas.lib.util.CommandLinePropertiesUtil;

public class R_file_generator implements Runnable {

	interface Keys {

		String base_dir = "-base-dir";
		String res_dir = "-res-dir";
		String gen_dir = "-gen-dir";
		String is_R = "-R";

		String accept_attr = "-accept-attr";
		String accept_files = "-accept-file";
		String accept_xml_files = "-accept-xml-file";
	}

	public static void main(String[] args) {
		final Properties prop = CommandLinePropertiesUtil
				.argumentsToProperties(args);
		R_file_generator gen = new R_file_generator(prop);
		gen.run();
	}

	private Properties mProp;

	private R_file_generator(Properties prop) {
		this.mProp = prop;
	}

	@Override
	public void run() {

		final Properties prop = this.mProp;

		for (Object k : prop.keySet()) {
			String key = k.toString();
			String value = MyHelper.valueForKey(prop, key);
			System.out.println(key + " = " + value);
		}

		String base = MyHelper.valueForKey(prop, Keys.base_dir);
		String res = MyHelper.valueForKey(prop, Keys.res_dir);
		String gen = MyHelper.valueForKey(prop, Keys.gen_dir);
		File resBase = new File(base, res);
		File genBase = new File(base, gen);

		String isR = MyHelper.valueForKey(prop, Keys.is_R);

		if (isR == null) {
			SigleDir sd = new SigleDir(prop);
			sd.mGenBase = genBase;
			sd.mGenDir = genBase;
			sd.mResBase = resBase;
			sd.mResDir = resBase;
			sd.run();
		} else {
			this.findSubDirectories(resBase, resBase, genBase, 32);
		}
	}

	private static class MyHelper {
		static String valueForKey(Properties prop, String key) {
			String value = prop.getProperty(key);
			if (value == null) {
				throw new RuntimeException("no value for key:" + key);
			}
			return value;
		}
	}

	private void findSubDirectories(File currentResDir, File resBase,
			File genBase, int depthLimit) {
		if (depthLimit <= 0) {
			throw new RuntimeException("the path is too deep:" + currentResDir);
		}
		File[] list = currentResDir.listFiles();
		for (File file : list) {
			if (file.isDirectory()) {
				this.findSubDirectories(file, resBase, genBase, depthLimit - 1);
			}
		}

		String curPath = currentResDir.getAbsolutePath();
		String basePath = resBase.getAbsolutePath();
		String diffPath = curPath.substring(basePath.length());

		SigleDir sd = new SigleDir(this.mProp);
		sd.mGenBase = genBase;
		sd.mResBase = resBase;
		sd.mResDir = currentResDir;
		sd.mGenDir = new File(genBase, diffPath);
		sd.run();
	}

	class Kind {

		private final String mName;
		private final Map<String, String> mTable = new HashMap<String, String>();

		public Kind(String kindName) {
			this.mName = kindName;
		}

		public void put(String key, String value) {
			this.mTable.put(key, value);
		}

		public String getName() {
			return this.mName;
		}

		public Collection<String> keys() {
			return this.mTable.keySet();
		}

		public String get(String key) {
			return this.mTable.get(key);
		}
	}

	class KindSet {

		private final Map<String, Kind> mTable = new HashMap<String, Kind>();

		public Kind getKind(String kindName, boolean create) {
			Kind k = this.mTable.get(kindName);
			if (k == null && create) {
				k = new Kind(kindName);
				this.mTable.put(kindName, k);
			}
			return k;
		}

		public Collection<Kind> kinds() {
			return this.mTable.values();
		}

		public boolean hasData() {
			return (this.mTable.size() > 0);
		}
	}

	class MyXmlHandler implements ContentHandler {

		private final Set<String> mAASet;
		private final ResultCollector mRC;

		public MyXmlHandler(ResultCollector resultCollector,
				Set<String> acceptAttrSet) {
			this.mRC = resultCollector;
			this.mAASet = acceptAttrSet;
		}

		@Override
		public void characters(char[] arg0, int arg1, int arg2)
				throws SAXException {
			// do nothing
		}

		@Override
		public void endDocument() throws SAXException {
			// do nothing
		}

		@Override
		public void endElement(String arg0, String arg1, String arg2)
				throws SAXException {
			// do nothing
		}

		@Override
		public void endPrefixMapping(String arg0) throws SAXException {
			// do nothing
		}

		@Override
		public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
				throws SAXException {
			// do nothing
		}

		@Override
		public void processingInstruction(String arg0, String arg1)
				throws SAXException {
			// do nothing
		}

		@Override
		public void setDocumentLocator(Locator arg0) {
			// do nothing
		}

		@Override
		public void skippedEntity(String arg0) throws SAXException {
			// do nothing
		}

		@Override
		public void startDocument() throws SAXException {
			// do nothing
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes atts) throws SAXException {

			// System.out.println("find xml element : " + qName);

			int len = atts.getLength();
			for (int i = 0; i < len; i++) {
				String attrValue = atts.getValue(i);
				String attrQName = atts.getQName(i);
				String attrLName = Helper.getLocalNameFromQName(attrQName);
				if (this.mAASet.contains(attrLName)) {
					String kind, key, value;
					kind = attrQName;
					key = attrValue;
					value = attrValue;
					this.mRC.addItem(kind, key, value);
				}
			}
		}

		@Override
		public void startPrefixMapping(String arg0, String arg1)
				throws SAXException {
			// do nothing
		}
	}

	static class Helper {
		private static String getLocalNameFromQName(String qName) {
			int i = qName.indexOf(':');
			if (i < 0) {
				return qName;
			} else {
				return qName.substring(i + 1);
			}
		}
	}

	class ResultCollector {

		private final SigleDir mSD;
		private final KindSet mKindSet = new KindSet();

		public ResultCollector(SigleDir sigleDir) {
			this.mSD = sigleDir;
		}

		public void addFile(File file) {
			String filePath = file.getAbsolutePath();
			String basePath = this.mSD.mResBase.getAbsolutePath();
			String kind, key, value;
			kind = "file";
			key = file.getName();
			value = "resource://" + filePath.substring(basePath.length());
			this.addItem(kind, key, value);
		}

		private void addItem(String kind, String key, String value) {
			kind = this.toJavaName(kind);
			key = this.toJavaName(key);
			Kind ki = this.mKindSet.getKind(kind, true);
			ki.put(key, value);
		}

		private String toJavaName(String str) {
			boolean isStartWithNum = false;
			char[] chs = str.toCharArray();
			StringBuilder sb = new StringBuilder();
			final int len = chs.length;
			for (int i = 0; i < len; i++) {
				final char ch = chs[i];
				if ('0' <= ch && ch <= '9') {
					sb.append(ch);
					if (i == 0) {
						isStartWithNum = true;
					}
				} else if ('a' <= ch && ch <= 'z') {
					sb.append(ch);
				} else if ('A' <= ch && ch <= 'Z') {
					sb.append(ch);
				} else {
					sb.append('_');
				}
			}
			if (isStartWithNum) {
				return "_" + sb.toString();
			} else {
				return sb.toString();
			}
		}

		public void parseXmlFile(File file) {
			try {

				Set<String> acceptAttrSet = this.mSD
						.getSetByKey(Keys.accept_attr);

				InputStream in = new FileInputStream(file);

				XMLReader rdr = XMLReaderFactory.createXMLReader();
				rdr.setContentHandler(new MyXmlHandler(this, acceptAttrSet));
				rdr.parse(new InputSource(in));

				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		final static String CRLF = "\r\n";

		public void writeToFileR() throws IOException {
			File dest = new File(this.mSD.mGenDir, "R.java");
			if (!dest.exists()) {
				dest.getParentFile().mkdirs();
				dest.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(dest);
			Writer wtr = new OutputStreamWriter(out);
			System.out.println("write to " + dest);
			wtr.write("package " + this.getPackageName() + ";" + CRLF);
			wtr.write(CRLF);
			wtr.write("// this file is gen by class:" + CRLF);
			wtr.write("// " + this + ";" + CRLF);
			wtr.write("// don't modify it!" + CRLF);
			wtr.write(CRLF);
			wtr.write("interface R {" + CRLF);
			Collection<Kind> kinds = this.mKindSet.kinds();
			for (Kind kind : kinds) {
				String kName = kind.getName();
				wtr.write("    interface " + kName + " {" + CRLF);
				Collection<String> keys = kind.keys();
				for (String key : keys) {
					String value = kind.get(key);
					wtr.write("        String " + key + " = \"" + value + "\";"
							+ CRLF);
				}
				wtr.write("    }" + CRLF);
			}
			wtr.write("}" + CRLF);
			wtr.flush();
			out.flush();
			out.close();
		}

		private String getPackageName() {

			File ptr = this.mSD.mGenDir;
			File base = this.mSD.mGenBase;
			Stack<String> stack = new Stack<String>();
			for (; ptr != null; ptr = ptr.getParentFile()) {
				if (ptr.equals(base)) {
					break;
				} else {
					stack.push(ptr.getName());
				}
			}
			StringBuilder sb = new StringBuilder();
			for (; stack.size() > 0;) {
				String str = stack.pop();
				if (sb.length() > 0) {
					sb.append('.');
				}
				sb.append(str);
			}
			return sb.toString();
		}

		public boolean hasData() {
			return this.mKindSet.hasData();
		}
	}

	class SigleDir {

		File mResBase;
		File mResDir;
		File mGenBase;
		File mGenDir;
		private final Properties mProp;

		public SigleDir(Properties prop) {
			this.mProp = prop;
		}

		public void run() {
			// System.out.println("=====================================");
			// System.out.println(" baseRes = " + this.mResBase);
			System.out.println("     ResDir = " + this.mResDir);
			// System.out.println(" baseGen = " + this.mGenBase);
			// System.out.println("     Gen = " + this.mGenDir);

			Set<String> acceptFiles = this.getSetByKey(Keys.accept_files);
			Set<String> acceptXmlFiles = this
					.getSetByKey(Keys.accept_xml_files);

			ResultCollector rc = new ResultCollector(this);

			File[] list = this.mResDir.listFiles();
			for (File file : list) {
				if (!file.isDirectory()) {
					String exName = this.getExName(file);
					if (acceptFiles.contains(exName)) {
						rc.addFile(file);
					}
					if (acceptXmlFiles.contains(exName)) {
						rc.parseXmlFile(file);
					}
				}
			}

			try {
				if (rc.hasData())
					rc.writeToFileR();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private String getExName(File file) {
			String str = file.getName();
			int i = str.lastIndexOf('.');
			if (i >= 0) {
				return str.substring(i).toLowerCase();
			} else {
				return "";
			}
		}

		private Set<String> getSetByKey(String key) {
			String value = MyHelper.valueForKey(this.mProp, key);
			String[] array = CommandLinePropertiesUtil.listToArray(value);
			Set<String> set = new HashSet<String>();
			for (String str : array) {
				set.add(str.toLowerCase());
			}
			return set;
		}
	}

}

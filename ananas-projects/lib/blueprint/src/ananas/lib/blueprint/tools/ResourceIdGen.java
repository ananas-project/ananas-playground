package ananas.lib.blueprint.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ResourceIdGen {

	/**
	 * @param arg
	 *            in command-line call this with the parameter of
	 *            "-base-dir ${project_loc} -res-dir res -gen-dir gen -R-class test.blueprint.R -accept-file .xml+.png"
	 * */

	public static void main(String[] arg) {
		ResourceIdGen gen = new ResourceIdGen();
		gen.parseCommandLine(arg);
		gen.run();
	}

	private HashMap<String, String> mParamTable;

	private String mAcceptFile;

	private File mBaseDir;

	public final static String p_base_dir = "-base-dir";
	public final static String p_res_dir = "-res-dir";
	public final static String p_gen_dir = "-gen-dir";
	public final static String p_r_class = "-R-class";
	public final static String p_accept_file = "-accept-file";

	private ResourceIdGen() {
	}

	private void parseCommandLine(String[] arg) {
		final HashMap<String, String> table = new HashMap<String, String>();
		String key = "";
		for (String str : arg) {
			str = str.trim();
			if (str.startsWith("-")) {
				key = str;
			} else {
				System.out.println("'" + key + "':'" + str + "'");
				table.put(key, str);
			}
		}
		this.mParamTable = table;
	}

	private void run() {
		ResultSet rlt = new ResultSet();
		this.scanResDir(rlt);
		this.outputJava(rlt);
	}

	private void outputJava(ResultSet rlt) {
		System.out.println("write to java file");
		System.out.println("begin[");
		try {
			final String baseDir = this.mParamTable.get(p_base_dir);
			final String genDir = this.mParamTable.get(p_gen_dir);
			final String rClass = this.mParamTable.get(p_r_class);
			String classPath = rClass.replace('.', '/');
			File file = new File(baseDir, genDir + "/" + classPath + ".java");
			file.getParentFile().mkdirs();
			OutputStream os = new FileOutputStream(file);
			rlt.writeToJavaFile(os, rClass);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("]eof");
	}

	private void scanResDir(ResultSet rlt) {
		String baseDir = this.mParamTable.get(p_base_dir);
		String resDir = this.mParamTable.get(p_res_dir);
		this.mAcceptFile = this.mParamTable.get(p_accept_file);
		File root = new File(baseDir, resDir);
		this.mBaseDir = root;
		this._scanDir(root, rlt);
	}

	private void _scanDir(File dir, ResultSet rlt) {
		if (!dir.isDirectory())
			return;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				this._scanDir(file, rlt);
			} else {
				this._scanFile(file, rlt);
			}
		}
	}

	private void _scanFile(File file, ResultSet rlt) {

		System.out.print("scan " + file.getAbsolutePath());

		String exName = null;

		{
			// filter by extend-name
			String name = file.getName();
			int index = name.lastIndexOf('.');
			exName = (index >= 0) ? name.substring(index) : "";
			if (this.mAcceptFile.indexOf(exName) < 0) {
				System.out.println(" [skip]");
				return;
			}
		}

		System.out.println(" [done]");

		{
			// scan file name

			String p1 = file.getAbsolutePath();
			String p2 = this.mBaseDir.getAbsolutePath();
			String p3 = p1.substring(p2.length());
			p3 = p3.replace('\\', '/');

			String namespace = "filename";
			String type = "file";
			String key = "" + file.getName();
			String value = "resource://" + p3;
			rlt.add(namespace, type, key, value);
		}
		{
			// scan file content
			if (exName.equalsIgnoreCase(".xml"))
				this._scanFileContent(file, rlt);
		}
	}

	private void _scanFileContent(File file, ResultSet rlt) {
		try {
			MyXmlHandler h = new MyXmlHandler(file, rlt);
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(file, h);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class MyXmlHandler extends DefaultHandler {

		private File mFile;
		private ResultSet mResult;

		public MyXmlHandler(File file, ResultSet rlt) {
			this.mFile = file;
			this.mResult = rlt;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attr) throws SAXException {

			String namespace = this.mFile.getAbsolutePath();
			for (int i = attr.getLength() - 1; i >= 0; i--) {
				String name = attr.getLocalName(i);
				String value = attr.getValue(i);
				if ("id".equals(name)) {
					this.mResult.add(namespace, "id", value, value);
				}
			}
		}
	}

	static class ResultSetNamespace {

		private final String mName;
		private final HashMap<String, String> mTableKV;

		public ResultSetNamespace(String name) {
			this.mName = name;
			this.mTableKV = new HashMap<String, String>();
		}

		public void put(String key, String value) {
			if (this.mTableKV.containsKey(key)) {
				System.err.println("warning : the resource is re-define");
				System.err.println("    " + "namespace = " + this.mName);
				System.err.println("    " + "key       = " + key);
				System.err.println("    " + "value     = " + value);
			} else {
				System.out.println("info : res_namespace.put():" + this.mName
						+ "#" + key + " = " + value);
			}
			this.mTableKV.put(key, value);
		}
	}

	static class ResultSetTypespace {

		private final String mName;
		private final HashMap<String, String> mTableKV;

		public ResultSetTypespace(String name) {
			this.mName = name;
			this.mTableKV = new HashMap<String, String>();
		}

		public void put(String key, String value) {
			System.out.println("info : res_type.put():" + this.mName + "."
					+ key + " = " + value);
			String v2 = this.mTableKV.get(key);
			if (v2 != null)
				if (!value.equals(v2)) {
					System.err.println("warning : the value is re-defined");
					System.err.println("    " + "type   = " + this.mName);
					System.err.println("    " + "key    = " + key);
					System.err.println("    " + "value1 = " + v2);
					System.err.println("    " + "value2 = " + value);
				}
			this.mTableKV.put(key, value);
		}

	}

	static class ResultSet {

		final HashMap<String, ResultSetNamespace> mTableNS;
		final HashMap<String, ResultSetTypespace> mTableTS;

		public ResultSet() {
			this.mTableNS = new HashMap<String, ResultSetNamespace>();
			this.mTableTS = new HashMap<String, ResultSetTypespace>();
		}

		public void writeToJavaFile(OutputStream os, String aClass)
				throws IOException {

			PrintStream out = new PrintStream(os, true, "utf-8");

			int index = aClass.lastIndexOf('.');
			String pkgName = aClass.substring(0, index);
			String className = aClass.substring(index + 1);

			out.println("package " + pkgName + ";");
			out.println("// This file is generate by {" + this + "} tool.");
			out.println("// Don't modify it !");
			out.println("class " + className + " {");

			Set<String> keys = this.mTableTS.keySet();
			for (String key : keys) {
				ResultSetTypespace typeSpace = this.mTableTS.get(key);
				String type = typeSpace.mName;
				out.println("    " + "public static class " + type + " {");
				Set<String> keys2 = typeSpace.mTableKV.keySet();
				for (String key2 : keys2) {
					String value2 = typeSpace.mTableKV.get(key2);
					out.println("        " + "public static final String "
							+ key2 + " = \"" + value2 + "\";");
				}
				out.println("    " + "}");
			}

			out.println("}");
			out.flush();
			os.flush();
		}

		public ResultSetNamespace getNS(String name) {
			ResultSetNamespace ns = this.mTableNS.get(name);
			if (ns == null) {
				ns = new ResultSetNamespace(name);
				this.mTableNS.put(name, ns);
			}
			return ns;
		}

		public ResultSetTypespace getTS(String name) {
			ResultSetTypespace ts = this.mTableTS.get(name);
			if (ts == null) {
				ts = new ResultSetTypespace(name);
				this.mTableTS.put(name, ts);
			}
			return ts;
		}

		private String _makeNameNormal(String name) {
			final char[] ca = name.toCharArray();
			for (int i = ca.length - 1; i >= 0; i--) {
				final char ch = ca[i];
				if (ch <= 0) {
					ca[i] = '_';
				} else if ('0' <= ch && ch <= '9') {
				} else if ('a' <= ch && ch <= 'z') {
				} else if ('A' <= ch && ch <= 'Z') {
					ca[i] = (char) (ch - 'A' + 'a');
				} else {
					ca[i] = '_';
				}
			}
			return new String(ca);
		}

		public void add(String namespace, String type, String key, String value) {

			namespace = this._makeNameNormal(namespace);
			type = this._makeNameNormal(type);
			key = this._makeNameNormal(key);

			ResultSetTypespace ts = this.getTS(type);
			ResultSetNamespace ns = this.getNS(namespace);
			ts.put(key, value);
			ns.put(key, value);
		}

	}

}

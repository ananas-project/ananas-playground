package ananas.lib.blueprint3.loader.eom;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.dom.BPElement;
import ananas.lib.blueprint3.dom.BPText;
import ananas.lib.blueprint3.lang.BPEnvironment;
import ananas.lib.blueprint3.lang.BPNamespace;
import ananas.lib.blueprint3.lang.BPType;
import ananas.lib.blueprint3.loader.eom.target.ITargetNode;
import ananas.lib.blueprint3.loader.eom.target.Tar_attribute;
import ananas.lib.blueprint3.loader.eom.target.Tar_class;
import ananas.lib.blueprint3.loader.eom.target.Tar_element;
import ananas.lib.blueprint3.loader.eom.target.Tar_eom;
import ananas.lib.blueprint3.loader.eom.target.Tar_namespace;
import ananas.lib.blueprint3.loader.eom.target.Tar_text;
import ananas.lib.blueprint3.loader.eom.target.util.TargetTravel;
import ananas.lib.blueprint3.loader.eom.target.util.TargetTravelCallback;
import ananas.lib.blueprint3.util.IMacroProperties;

public class MyBpnsFactory {

	private final BPEnvironment mEnvi;
	private final Tar_eom mModel;

	private final Map<String, MyTempNS> mTempNsMap;
	private final Map<String, MyTempClass> mTempClassMap;

	public MyBpnsFactory(BPEnvironment envi, Tar_eom model) {
		this.mEnvi = envi;
		this.mModel = model;
		this.mTempClassMap = new HashMap<String, MyTempClass>();
		this.mTempNsMap = new HashMap<String, MyTempNS>();
	}

	public BPNamespace[] build() {

		final TargetTravel tt = new TargetTravel();

		MyRegister reg;

		tt.go(this.mModel, new MyPreloading());
		tt.go(this.mModel, new MyChecking());
		tt.go(this.mModel, new MyBuilding());
		tt.go(this.mModel, new MyVerify());
		tt.go(this.mModel, reg = new MyRegister());

		return reg.toNsArray();
	}

	class MyChecking implements TargetTravelCallback {

		@Override
		public void findChild(ITargetNode child, ITargetNode parent) {

			if (child instanceof Tar_class) {
				Tar_class cls = (Tar_class) child;
				this.checkClass(cls);

			} else if (child instanceof Tar_attribute) {
				Tar_attribute attr = (Tar_attribute) child;
				this.checkAttr(attr);

			} else if (child instanceof Tar_text) {
				Tar_text text = (Tar_text) child;
				this.checkText(text);

			} else if (child instanceof Tar_element) {
				Tar_element elt = (Tar_element) child;
				this.checkElement(elt);
			}

		}

		private void checkText(Tar_text text) {

			String tid = text.getParent().getTargetId();
			MyTempClass tmpClass = MyBpnsFactory.this.mTempClassMap.get(tid);
			BPType type = tmpClass.getBPType();
			Class<?> ctrlClass = type.getControllerClass();

			String mtdName = Const.add_text_method_name;

			try {
				Method mtd = ctrlClass.getMethod(mtdName, BPText.class);
				if (mtd == null) {
					throw new RuntimeException("no method:" + mtdName);
				}
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException) e;
				} else {
					throw new RuntimeException(e);
				}
			}
		}

		private void checkClass(Tar_class cls) {

			String tid = cls.getTargetId();
			MyTempClass tmpClass = MyBpnsFactory.this.mTempClassMap.get(tid);
			BPType type = tmpClass.getBPType();
			Class<?> ctrlClass = type.getControllerClass();

			String ref = cls.getExtends();
			if (ref != null) {
				Class<?> base = this.getCtrlClassByRef(ref, cls);
				if (base.isAssignableFrom(ctrlClass)) {
					// ok
				} else {
					throw new RuntimeException(ctrlClass + " no super class : "
							+ base);
				}
			}

		}

		private void checkElement(Tar_element elt) {

			String tid = elt.getParent().getTargetId();
			MyTempClass tmpClass = MyBpnsFactory.this.mTempClassMap.get(tid);
			BPType type = tmpClass.getBPType();
			Class<?> ctrlClass = type.getControllerClass();

			final String chName = elt.getName();
			final String chType = elt.getType();

			String mtdName;
			Class<?> mtdType;

			if (chName != null) {
				mtdName = Const.add_child_method_prefix + chName;
				mtdName = Func.toJavaName(mtdName);
			} else {
				mtdName = Const.add_child_method_prefix;
			}

			if (chType == null) {
				mtdType = BPElement.class;
			} else {
				mtdType = this.getCtrlClassByRef(chType, elt);
			}

			try {
				Method mtd = ctrlClass.getMethod(mtdName, mtdType);
				if (mtd == null) {
					throw new RuntimeException("no method:" + mtdName);
				}
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException) e;
				} else {
					throw new RuntimeException(e);
				}
			}

		}

		private Class<?> getCtrlClassByRef(String ref, ITargetNode node) {

			MyTempNS tmpNS = null;
			for (; node != null; node = node.getParent()) {
				if (node instanceof Tar_namespace) {
					String tid = node.getTargetId();
					tmpNS = MyBpnsFactory.this.mTempNsMap.get(tid);
					break;
				}
			}

			IMacroProperties prop = tmpNS.mTar.getProperties();
			ref = prop.processMacro(ref);

			if (ref.startsWith("#")) {
				String key = ref.substring(1);
				MyTempClass tmpCls = tmpNS.mTempClassMap.get(key);
				if (tmpCls == null) {
					tmpCls = MyBpnsFactory.this.mTempClassMap.get(key);
				}
				if (tmpCls == null) {
					throw new RuntimeException("no class for ref:" + ref);
				}
				BPType bpType = tmpCls.getBPType();
				return bpType.getControllerClass();
			} else {

				try {
					return Class.forName(ref);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}

		}

		private void checkAttr(Tar_attribute attr) {

			String tid = attr.getParent().getTargetId();
			MyTempClass tmpClass = MyBpnsFactory.this.mTempClassMap.get(tid);
			BPType type = tmpClass.getBPType();
			Class<?> ctrlClass = type.getControllerClass();

			String mtdName = Const.set_attr_method_prefix + attr.getName();
			mtdName = Func.toJavaName(mtdName);

			try {
				Method mtd = ctrlClass.getMethod(mtdName, BPAttribute.class);
				if (mtd == null) {
					throw new RuntimeException("no method:" + mtdName);
				}
			} catch (Exception e) {
				if (e instanceof RuntimeException) {
					throw (RuntimeException) e;
				} else {
					throw new RuntimeException(e);
				}
			}
		}

	}

	class MyBuilding implements TargetTravelCallback {

		@Override
		public void findChild(ITargetNode child, ITargetNode parent) {

			if (child instanceof Tar_class) {

				// reg class to ns

				String tid = child.getTargetId();
				MyTempClass tmpCls = MyBpnsFactory.this.mTempClassMap.get(tid);
				BPType type = tmpCls.getBPType();
				BPNamespace bpns = type.getOwnerNamespace();
				bpns.registerType(type);

				// System.out.println("new type : " + type);
			}

		}

	}

	class MyVerify implements TargetTravelCallback {

		@Override
		public void findChild(ITargetNode child, ITargetNode parent) {
			// TODO Auto-generated method stub

		}

	}

	class MyRegister implements TargetTravelCallback {

		final List<BPNamespace> mNsList = new ArrayList<BPNamespace>();

		@Override
		public void findChild(ITargetNode child, ITargetNode parent) {

			// reg ns to envi

			if (child instanceof Tar_namespace) {
				MyTempNS tmpNS = MyBpnsFactory.this.mTempNsMap.get(child
						.getTargetId());
				if (tmpNS.mTar.enableExport()) {
					this.mNsList.add(tmpNS.getBPNamespace());
				}
			}

		}

		public BPNamespace[] toNsArray() {
			List<BPNamespace> list = this.mNsList;
			return list.toArray(new BPNamespace[list.size()]);
		}

	}

	class MyPreloading implements TargetTravelCallback {

		@Override
		public void findChild(ITargetNode child, ITargetNode parent) {

			// System.out.println(child + ".parent  =  " + parent);

			// set parent
			child.setParent(parent);

			// create temp object

			if (child instanceof Tar_namespace) {
				Tar_namespace tar = (Tar_namespace) child;
				MyTempNS tmpNS = new MyTempNS(tar);
				String tid = tar.getTargetId();
				MyBpnsFactory.this.mTempNsMap.put(tid, tmpNS);

				String name = tar.getNamespaceURI();
				MyBpnsFactory.this.mTempNsMap.put(name, tmpNS);

				// System.out.println("new ns : " + tar);

			} else if (child instanceof Tar_class) {
				Tar_class tar = (Tar_class) child;
				MyTempClass tmpCls = new MyTempClass(tar);
				String tid = tar.getTargetId();
				MyBpnsFactory.this.mTempClassMap.put(tid, tmpCls);

				String name = tar.getName();
				MyTempNS tmpNS = tmpCls.getNS();
				tmpNS.mTempClassMap.put(name, tmpCls);
				MyBpnsFactory.this.mTempClassMap.put(name, tmpCls);

				// System.out.println("new cls : " + tar);
			}

		}
	}

	class MyTempNS {

		private final Map<String, MyTempClass> mTempClassMap;
		private final Tar_namespace mTar;
		private BPNamespace mBPNS;

		public MyTempNS(Tar_namespace tar) {
			this.mTar = tar;
			this.mTempClassMap = new HashMap<String, MyTempClass>();
		}

		public BPNamespace getBPNamespace() {
			BPNamespace ns = this.mBPNS;
			if (ns == null) {
				String uri = this.mTar.getNamespaceURI();
				String defaultPrefix = this.mTar.getDefaultPrefix();
				BPEnvironment envi = MyBpnsFactory.this.mEnvi;
				ns = envi.getImplementation().createNamespace(envi, uri,
						defaultPrefix);
				this.mBPNS = ns;
			}
			return ns;
		}
	}

	class MyTempClass {

		private final Tar_class mTar;
		private MyTempNS mNS;
		private BPType mBpType;

		public MyTempClass(Tar_class tar) {
			this.mTar = tar;
		}

		public BPType getBPType() {
			BPType type = this.mBpType;
			if (type == null) {
				BPNamespace ns = this.getNS().getBPNamespace();
				String lname = this.mTar.getLocalName();
				Class<?> clsCtrl = this.getClassForCtrl();
				Class<?> clsTarget = this.getClassForTarget();
				MyBpType type2 = new MyBpType(ns, lname, clsCtrl, clsTarget);
				this.mBpType = type = type2;
			}
			return type;
		}

		private Class<?> getClassForTarget() {
			MyClassFinder cf = new MyClassFinder();
			Tar_class tar = this.mTar;
			cf.javaName = tar.getJavaName();
			cf.className = tar.getTargetClass();
			cf.defaultKey = Const.ns_default_target_class;
			cf.properties = tar.getOwnerNS().getProperties();
			return cf.toClass();
		}

		private Class<?> getClassForCtrl() {
			MyClassFinder cf = new MyClassFinder();
			Tar_class tar = this.mTar;
			cf.javaName = tar.getJavaName();
			cf.className = tar.getControllerClass();
			cf.defaultKey = Const.ns_default_controller_class;
			cf.properties = tar.getOwnerNS().getProperties();
			return cf.toClass();
		}

		public MyTempNS getNS() {
			MyTempNS ns = this.mNS;
			if (ns == null) {
				String tid = this.mTar.getOwnerNS().getTargetId();
				ns = MyBpnsFactory.this.mTempNsMap.get(tid);
				this.mNS = ns;
			}
			return ns;
		}
	}

	class MyClassFinder {

		public IMacroProperties properties;
		public String defaultKey;
		public String className;
		public String javaName;

		public Class<?> toClass() {
			this.properties.put(Const.ns_java_name, javaName + "");
			String cname = this.className;
			if (cname == null) {
				cname = this.properties.get(this.defaultKey);
			}
			if (cname == null) {
				return null;
			}
			cname = this.properties.processMacro(cname);
			try {
				return Class.forName(cname);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

}

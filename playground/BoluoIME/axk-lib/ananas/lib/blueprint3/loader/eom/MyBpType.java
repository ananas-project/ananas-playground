package ananas.lib.blueprint3.loader.eom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ananas.lib.blueprint3.dom.BPAttribute;
import ananas.lib.blueprint3.dom.BPDocument;
import ananas.lib.blueprint3.dom.BPElement;
import ananas.lib.blueprint3.dom.BPText;
import ananas.lib.blueprint3.lang.BPNamespace;
import ananas.lib.blueprint3.lang.BPType;

class MyBpType implements BPType {

	private final Class<?> mTargetClass;
	private final Class<?> mCtrlClass;
	private final String mLocalName;
	private final BPNamespace mOwnerNS;

	private final List<MethodContext> mMethodList = new ArrayList<MethodContext>();
	private final Map<String, MethodContext> mMethodMap = new HashMap<String, MethodContext>();
	private Method mMethodForTextAppend;

	public MyBpType(BPNamespace ownerNS, String localName, Class<?> ctrlClass,
			Class<?> targetClass) {
		this.mOwnerNS = ownerNS;
		this.mLocalName = localName;
		this.mCtrlClass = ctrlClass;
		this.mTargetClass = targetClass;

		if (ctrlClass == null || targetClass == null) {
			throw new RuntimeException("no ctrlClass|targetClass for "
					+ ownerNS.getNamespaceURI() + "#" + localName);
		}

		this._loadMethods();
	}

	private void _loadMethods() {
		Method[] methods = this.mCtrlClass.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (name == null) {

			} else if (name.startsWith(Const.add_child_method_prefix)) {
				String prefix = Const.add_child_method_prefix;
				String localName = name.substring(prefix.length());
				this._add_method_for_element(localName, method);

			} else if (name.startsWith(Const.set_attr_method_prefix)) {
				String prefix = Const.set_attr_method_prefix;
				String localName = name.substring(prefix.length());
				this._add_method_for_attr(localName, method);

			} else if (name.equals(Const.add_text_method_name)) {
				this.mMethodForTextAppend = method;

			} else {
			}
		}
	}

	private void _add_method_for_element(String localName, Method method) {
		MethodContext mc = new MethodContext(method);
		String key = this.keyForElement(localName);
		this.mMethodMap.put(key, mc);
		this.mMethodList.add(mc);
	}

	private void _add_method_for_attr(String localName, Method method) {
		MethodContext mc = new MethodContext(method);
		String key = this.keyForAttr(localName);
		this.mMethodMap.put(key, mc);
	}

	private static class MethodContext {

		private final Method mMethod;
		private final Class<?> mParamType;

		public MethodContext(Method method) {
			this.mMethod = method;
			this.mParamType = this._loadParamType(method);
		}

		private Class<?> _loadParamType(Method method) {
			Class<?>[] ps = method.getParameterTypes();
			if (ps.length != 1) {
				throw new RuntimeException("bad method : " + method);
			}
			return ps[0];
		}

		public Method getMethod() {
			return mMethod;
		}

		public Class<?> getParamType() {
			return mParamType;
		}

	}

	@Override
	public Class<?> getTargetClass() {
		return this.mTargetClass;
	}

	@Override
	public Class<?> getControllerClass() {
		return this.mCtrlClass;
	}

	@Override
	public String getLocalName() {
		return this.mLocalName;
	}

	@Override
	public BPNamespace getOwnerNamespace() {
		return this.mOwnerNS;
	}

	@Override
	public boolean appendElementToParent(BPElement parent, BPElement child) {
		String key = this.keyForElement(child.getLocalName());
		MethodContext method = this.mMethodMap.get(key);
		if (method == null) {
			for (MethodContext mc : this.mMethodList) {
				Class<?> type1 = mc.getParamType();
				if (type1.isInstance(child)) {
					method = mc;
					break;
				}
			}
		}
		if (method == null) {
			throw new RuntimeException("no method append child : " + child
					+ " >>> " + parent);
		}
		try {
			Method method2 = method.getMethod();
			boolean rlt = (Boolean) method2.invoke(parent, child);
			return rlt;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String keyForElement(String localName) {
		return ("ele:" + localName);
	}

	@Override
	public boolean setAttributeForParent(BPElement parent, BPAttribute attr) {
		String key = this.keyForAttr(attr.getLocalName());
		MethodContext method = this.mMethodMap.get(key);
		if (method == null) {
			// String attrLName = attr.getLocalName();
			throw new RuntimeException("cannot find method for : " + parent
					+ "#" + key);
		}
		try {
			boolean rlt = (Boolean) method.getMethod().invoke(parent, attr);
			return rlt;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String keyForAttr(String localName) {
		return ("attr:" + localName);
	}

	public String toString() {
		String uri = this.mOwnerNS.getNamespaceURI();
		return ("[" + this.getClass().getName() + " fullName=" + uri + "#"
				+ this.getLocalName() + " target=" + this.mTargetClass
				+ " ctrl=" + this.mCtrlClass + "]");

	}

	@Override
	public BPElement createElement(BPDocument doc) {
		try {
			Class<?> cls = this.getControllerClass();
			BPElement element = (BPElement) cls.newInstance();
			element.bindType(this);
			element.bindOwnerDocument(doc);
			return element;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean appendTextToParent(BPElement parent, BPText text) {
		Method method = this.mMethodForTextAppend;
		if (method == null) {
			throw new RuntimeException(
					"cannot find method for text append to : " + parent);
		}
		try {
			return (Boolean) method.invoke(parent, text);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

}

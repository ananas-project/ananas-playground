package ananas.lib.blueprint3.loader.reflect.dom;

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

public class MyElementType implements BPType {

	public static class Config {

		public Class<?> targetClass;
		public Class<?> ctrlClass;
		public BPNamespace ownerNS;
		public String localName;
	}

	private final Config mConf;

	class MethodContext {

		private Method mMethod;
		private Class<?> mParamClass;

		public MethodContext(Method method) {
			this.mMethod = method;
			this.mParamClass = method.getParameterTypes()[0];
		}

		public Method getMethod() {
			return this.mMethod;
		}

		public Class<?> getParameterClass() {
			return this.mParamClass;
		}
	}

	private final Map<String, MethodContext> mMethodMap; // for localName
	private final List<MethodContext> mMethodList; // for types

	public MyElementType(Config conf) {
		this.mConf = conf;
		this.mMethodMap = new HashMap<String, MethodContext>();
		this.mMethodList = new ArrayList<MethodContext>();
	}

	@Override
	public Class<?> getTargetClass() {
		return this.mConf.targetClass;
	}

	@Override
	public Class<?> getControllerClass() {
		return this.mConf.ctrlClass;
	}

	@Override
	public String getLocalName() {
		return this.mConf.localName;
	}

	@Override
	public BPNamespace getOwnerNamespace() {
		return this.mConf.ownerNS;
	}

	@Override
	public boolean appendElementToParent(BPElement parent, BPElement child) {

		if (!(child instanceof BPElement)) {
			return false;
		}
		final BPElement ch2 = (BPElement) child;
		Method method = null;

		// by localName
		String key = this.keyForElement(ch2.getLocalName());
		method = this.getMethodByKey(key);

		// by type
		if (method == null) {
			for (MethodContext mc : this.mMethodList) {
				Class<?> cls = mc.getParameterClass();
				if (cls.isInstance(ch2)) {
					method = mc.getMethod();
					break;
				}
			}
		}

		try {
			if (method == null) {
				return false;
			}
			boolean rlt = (Boolean) method.invoke(parent, ch2);
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

	@Override
	public boolean setAttributeForParent(BPElement parent, BPAttribute attr) {

		String key = this.keyForAttr(attr.getLocalName());
		Method method = this.getMethodByKey(key);
		if (method == null) {
			return false;
		}

		try {
			Boolean rlt = (Boolean) method.invoke(parent, attr);
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

	private Method getMethodByKey(String key) {
		MethodContext mc = this.mMethodMap.get(key);
		if (mc == null)
			return null;
		return mc.getMethod();
	}

	public void reflectMethods(String setAttrPrefix, String addChildPrefix) {

		Method[] methods = this.mConf.ctrlClass.getMethods();
		for (Method mtd : methods) {
			String name = mtd.getName();

			if (name.startsWith(addChildPrefix)) {
				String localName = name.substring(addChildPrefix.length());
				this._addAppendChildMethod(mtd, localName);

			} else if (name.startsWith(setAttrPrefix)) {
				String localName = name.substring(setAttrPrefix.length());
				this._addSetAttrMethod(mtd, localName);

			} else {
			}
		}

	}

	private String keyForAttr(String localName) {
		return ("attr##" + localName);
	}

	private String keyForElement(String localName) {
		return ("element##" + localName);
	}

	private void _addSetAttrMethod(Method mtd, String localName) {

		// System.out.println("_addSetAttrMethod:" + mtd);

		MethodContext value = new MethodContext(mtd);
		String key = this.keyForAttr(localName);
		this.mMethodMap.put(key, value);
	}

	private void _addAppendChildMethod(Method mtd, String localName) {

		// System.out.println("_addAppendChildMethod:" + mtd);

		if (localName == null) {
			localName = "";
		}
		MethodContext value = new MethodContext(mtd);
		if (localName.length() > 0) {
			String key = this.keyForElement(localName);
			this.mMethodMap.put(key, value);
		} else {
			this.mMethodList.add(value);
		}
	}

	@Override
	public BPElement createElement(BPDocument doc) {
		throw new RuntimeException("no impl");
	}

	@Override
	public boolean appendTextToParent(BPElement parent, BPText text) {
		throw new RuntimeException("no impl");
	}

}

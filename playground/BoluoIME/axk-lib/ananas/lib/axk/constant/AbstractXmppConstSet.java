package ananas.lib.axk.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractXmppConstSet {

	private final Map<String, Object> mTable;
	private final Object mDefault;

	public AbstractXmppConstSet(Object defaultValue) {
		Collection<Object> array = this.findAllConst(defaultValue);
		Map<String, Object> map = new HashMap<String, Object>();
		for (Object item : array) {
			map.put(item.toString(), item);
		}
		this.mTable = map;
		this.mDefault = defaultValue;
	}

	private Collection<Object> findAllConst(Object value) {
		List<Object> out = new ArrayList<Object>();
		Class<?> cls = value.getClass();
		Field[] list = cls.getFields();
		for (Field f : list) {
			Class<?> cls2 = f.getType();
			if (cls2.isInstance(value)) {
				Object obj;
				try {
					obj = f.get(cls2);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if (obj.getClass().equals(value.getClass())) {
					// System.out.println(obj + "");
					out.add(obj);
				}
			}
		}
		return out;
	}

	public Object get(String string) {
		Object rlt = this.mTable.get(string);
		if (rlt == null) {
			rlt = this.mDefault;
		}
		return rlt;
	}

}

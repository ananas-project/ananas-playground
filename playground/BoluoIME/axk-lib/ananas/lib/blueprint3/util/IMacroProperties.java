package ananas.lib.blueprint3.util;

import java.util.Set;

public interface IMacroProperties {

	// const

	String ns_set_attr_method_prefix = "ns:setAttributeMethodNamePrefix";
	String ns_append_child_method_prefix = "ns:appendChildMethodNamePrefix";

	String ns_default_prefix = "ns:defaultPrefix";
	String ns_uri = "ns:namespaceURI";

	String ns_defaultControllerClass = "ns:defaultControllerClass";
	String ns_defaultTargetClass = "ns:defaultTargetClass";
	String ns_localName = "ns:localName";

	// methods

	void put(String key, String value);

	String get(String key);

	String get(String key, boolean canBeNull, String defaultValue);

	String get(String key, boolean canBeNull, String defaultValue,
			boolean processMacro);

	Set<String> keySet();

	String processMacro(String value);
}

package ananas.lib.blueprint3.loader.eom;

public interface Const {

	// method name
	String set_attr_method_prefix = "set_attribute_";
	String add_child_method_prefix = "append_child_";
	String add_text_method_name = "append_text";

	// property keys in xml
	String ns_default_controller_class = "class:controllerClass";
	String ns_default_target_class = "class:targetClass";
	String ns_local_name = "class:localName";
	String ns_default_prefix = "namespace:defaultPrefix";
	String ns_uri = "namespace:uri";
	String ns_java_name = "class:javaName";

}

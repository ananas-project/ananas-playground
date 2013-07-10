package ananas.lib.blueprint3.xmlparser;

public interface INamespaceMapper {

	String prefixToURI(String prefix);

	String findUriByQName(String qName);

}

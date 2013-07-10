package ananas.lib.blueprint3.lang;

public interface BPFileNameMapper {

	void register(String dotName, String contentType);

	String getContentType(String dotName);
}

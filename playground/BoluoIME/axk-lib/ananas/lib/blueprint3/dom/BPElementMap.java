package ananas.lib.blueprint3.dom;

public interface BPElementMap {

	void put(String id, BPElement element);

	void remove(String id);

	void remove(BPElement element);

	BPElement get(String id);

	void reset();
}

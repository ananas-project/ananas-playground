package ananas.lib.impl.blueprint3.core;

import java.util.HashMap;
import java.util.Map;

import ananas.lib.blueprint3.dom.BPElement;
import ananas.lib.blueprint3.dom.BPElementMap;

public class BPElementMapImpl implements BPElementMap {

	final Map<String, BPElement> mTable = new HashMap<String, BPElement>();

	@Override
	public void put(String id, BPElement element) {
		this.mTable.put(id, element);
	}

	@Override
	public void remove(String id) {
		this.mTable.remove(id);
	}

	@Override
	public void remove(BPElement element) {
		throw new RuntimeException("no impl");
	}

	@Override
	public BPElement get(String id) {
		return this.mTable.get(id);
	}

	@Override
	public void reset() {
		this.mTable.clear();
	}

}

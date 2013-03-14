package ananas.tools.dtt.dom;

import ananas.lib.blueprint3.core.dom.BPAttribute;

public class Ctrl_task extends AbstractDttObject {

	private String mName;
	private long mTimeQuota;

	public Ctrl_task() {
	}

	public String getName() {
		return this.mName;
	}

	public long getTimeQuota() {
		return this.mTimeQuota;
	}

	public boolean set_attribute_name(BPAttribute attr) {
		this.mName = attr.getValue();
		return true;
	}

	public boolean set_attribute_timeQuota(BPAttribute attr) {
		this.mTimeQuota = Long.parseLong(attr.getValue());
		return true;
	}

	public void setName(String name) {
		this.mName = name;
	}

}

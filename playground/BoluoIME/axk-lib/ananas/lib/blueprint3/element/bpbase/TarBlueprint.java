package ananas.lib.blueprint3.element.bpbase;

public class TarBlueprint extends TarObject {

	private TarBody mBody;
	private TarHead mHead;

	public boolean setHead(TarHead head) {
		this.mHead = head;
		return true;
	}

	public boolean setBody(TarBody body) {
		this.mBody = body;
		return true;
	}

	public TarBody getBody() {
		return this.mBody;
	}

	public TarHead getHead() {
		return this.mHead;
	}
}

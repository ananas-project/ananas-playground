package ananas.app.ots.v2.service;

public abstract class AbstractOTSRuntime implements OTSRuntime {

	protected final OTSServiceContext context;

	public AbstractOTSRuntime(OTSServiceContext context) {
		this.context = context;
	}

	public OTSServiceContext getServiceContext() {
		return this.context;
	}

}

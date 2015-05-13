package ananas.app.ots.v2.service;

import ananas.app.ots.v2.pojo.OTSLocation;

public interface OTSRuntime {

	void onOpen();

	void onLocation(OTSLocation location);

	void onClose();

	OTSServiceContext getServiceContext();
}

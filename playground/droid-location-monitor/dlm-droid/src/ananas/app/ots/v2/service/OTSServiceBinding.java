package ananas.app.ots.v2.service;

import ananas.app.ots.v2.pojo.OTSLocation;
import ananas.app.ots.v2.pojo.OTSServiceConfig;
import ananas.app.ots.v2.pojo.OTSServiceStatus;
import ananas.app.ots.v2.pojo.OTSServiceTask;

public interface OTSServiceBinding {

	void start();

	void stop();

	void restart();

	void setConfig(OTSServiceConfig conf);

	OTSServiceConfig getConfig();

	OTSServiceTask getTask();

	OTSServiceStatus getStatus();

	OTSLocation[] getLastLocations(int limitCount);

}

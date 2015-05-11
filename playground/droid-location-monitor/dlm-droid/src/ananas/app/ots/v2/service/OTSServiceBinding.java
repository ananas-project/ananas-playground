package ananas.app.ots.v2.service;

import ananas.app.ots.v2.pojo.OTSServiceConfig;
import ananas.app.ots.v2.pojo.OTSServiceStatus;

public interface OTSServiceBinding {

	void start();

	void stop();

	void restart();

	void setConfig(OTSServiceConfig conf);

	OTSServiceConfig getConfig();

	void setStatus(OTSServiceStatus status);

	OTSServiceStatus getStatus();

	String getStatusText();

}

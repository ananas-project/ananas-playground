package ananas.app.ots.v2.service;

import ananas.app.ots.v2.pojo.OTSServiceConfig;
import ananas.app.ots.v2.pojo.OTSServiceStatus;
import ananas.app.ots.v2.pojo.OTSServiceTask;
import android.app.Service;

public interface OTSServiceContext {

	OTSServiceTask getTask();

	OTSServiceConfig getConfig();

	OTSServiceStatus getStatus();

	Service getService();

	void setStatus(OTSServiceStatus status);

	void saveStatus();

	void saveAll();

}

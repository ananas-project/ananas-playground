package ananas.app.ots.v2.service;

import ananas.app.ots.v2.pojo.OTSLocation;
import ananas.app.ots.v2.pojo.OTSServiceConfig;
import ananas.app.ots.v2.pojo.OTSServiceStatus;
import ananas.app.ots.v2.pojo.OTSServiceTask;
import ananas.app.ots.v2.tools.ContextTools;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class AbstractOTSService extends Service implements OTSServiceBinding {

	private static final myRuntimeHolder holder;
	// private final Gson gson = new Gson();

	/******
	 * the life-cycle of a runtime
	 * 
	 * <runtime> <open/> <start/> <stop/> <start/> <stop/> <start/> <stop/>
	 * <close/> </runtime>
	 * 
	 * */

	static {
		holder = new myRuntimeHolder();
	}

	private static class myServiceContext implements OTSServiceContext {

		private final Service service;
		public OTSServiceConfig config;
		public OTSServiceTask task;
		public OTSServiceStatus status;

		public myServiceContext(Service service) {
			this.service = service;
			config = new OTSServiceConfig();
			task = new OTSServiceTask();
			status = new OTSServiceStatus();
		}

		public void init() {
			task = new OTSServiceTask();
			status = new OTSServiceStatus();
			long now = System.currentTimeMillis();
			task.setStartTime(now);
			task.setTaskId("task-" + now);
			task.setWorking(true);
		}

		@Override
		public OTSServiceTask getTask() {
			return task;
		}

		@Override
		public OTSServiceConfig getConfig() {
			return config;
		}

		@Override
		public OTSServiceStatus getStatus() {
			return status;
		}

		@Override
		public Service getService() {
			return this.service;
		}

		@Override
		public void setStatus(OTSServiceStatus status) {
			this.status = status;
		}

		public void loadConfig() {
			ContextTools tools = new ContextTools(service);
			this.config = tools.loadContextPOJO(OTSServiceConfig.class);
		}

		public void loadTask() {
			ContextTools tools = new ContextTools(service);
			this.task = tools.loadContextPOJO(OTSServiceTask.class);
		}

		public void loadStatus() {
			ContextTools tools = new ContextTools(service);
			this.status = tools.loadContextPOJO(OTSServiceStatus.class);
		}

		public void saveConfig() {
			ContextTools tools = new ContextTools(service);
			tools.saveContextPOJO(this.config);
		}

		public void saveTask() {
			ContextTools tools = new ContextTools(service);
			tools.saveContextPOJO(this.task);
		}

		@Override
		public void saveStatus() {
			ContextTools tools = new ContextTools(service);
			tools.saveContextPOJO(this.status);
		}

		public void loadAll() {
			this.loadConfig();
			this.loadTask();
			this.loadStatus();
		}

		public void saveAll() {
			this.saveConfig();
			this.saveStatus();
			this.saveTask();
		}

	}

	private static class myRuntimeHolder {

		// config << task << status

		public OTSServiceRuntime current;

		public myRuntimeHolder() {
		}

		private synchronized OTSServiceRuntime selectCurrentRuntimeSync(
				OTSServiceRuntime rtNew) {
			OTSServiceRuntime rtOld = this.current;
			this.current = rtNew;
			return rtOld;
		}

		public void setCurrentRuntime(final OTSServiceRuntime rtNew) {
			final OTSServiceRuntime rtOld = this
					.selectCurrentRuntimeSync(rtNew);
			if (rtOld != null) {
				rtOld.close();
			}
			if (rtNew != null) {
				rtNew.open();
			}
		}

		public void update(Service service) {
			myServiceContext context = new myServiceContext(service);
			context.loadAll();
			if (context.task.isWorking()) {
				OTSServiceRuntime rt = new OTSServiceRuntime(context);
				this.setCurrentRuntime(rt);
			} else {
				this.setCurrentRuntime(null);
			}
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return new myBinder(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		holder.update(this);
	}

	@Override
	public void onDestroy() {
		OTSServiceRuntime rt = holder.current;
		if (rt != null) {
			OTSServiceContext sc = rt.getServiceContext();
			if (sc != null) {
				sc.saveAll();
			}
		}
		holder.setCurrentRuntime(null);
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	private class myBinder extends Binder implements OTSServiceBinding {

		private final OTSServiceBinding target;

		public myBinder(OTSServiceBinding t) {
			this.target = t;
		}

		public void start() {
			target.start();
		}

		public void stop() {
			target.stop();
		}

		public void restart() {
			target.restart();
		}

		public void setConfig(OTSServiceConfig conf) {
			target.setConfig(conf);
		}

		public OTSServiceConfig getConfig() {
			return target.getConfig();
		}

		public OTSServiceStatus getStatus() {
			return target.getStatus();
		}

		@Override
		public OTSServiceTask getTask() {
			return target.getTask();
		}

		@Override
		public OTSLocation[] getLastLocations(int limitCount) {
			return target.getLastLocations(limitCount);
		}

	}

	@Override
	public void start() {
		myServiceContext sc = new myServiceContext(this);
		sc.loadAll();
		if (sc.task.isWorking()) {
			return;
		}
		sc.init();
		sc.saveAll();
		holder.update(this);
	}

	@Override
	public void stop() {
		myServiceContext sc = new myServiceContext(this);
		sc.loadAll();
		sc.task.setWorking(false);
		sc.saveAll();
		holder.update(this);
	}

	@Override
	public void restart() {
		holder.setCurrentRuntime(null);
		holder.update(this);
	}

	@Override
	public void setConfig(OTSServiceConfig conf) {
		myServiceContext sc = new myServiceContext(this);
		sc.loadAll();
		sc.config = conf;
		sc.saveAll();
	}

	@Override
	public OTSServiceConfig getConfig() {
		myServiceContext sc = new myServiceContext(this);
		sc.loadConfig();
		return sc.config;
	}

	@Override
	public OTSServiceTask getTask() {
		myServiceContext sc = new myServiceContext(this);
		sc.loadTask();
		return sc.task;
	}

	@Override
	public OTSServiceStatus getStatus() {
		OTSServiceRuntime rt = holder.current;
		if (rt != null) {
			OTSServiceContext sc = rt.getServiceContext();
			return sc.getStatus();
		}
		return new OTSServiceStatus();
	}

	@Override
	public OTSLocation[] getLastLocations(int limitCount) {
		OTSServiceRuntime rt = holder.current;
		if (rt != null) {
			return rt.getLastLocations(limitCount);
		}
		return new OTSLocation[0];
	}

}

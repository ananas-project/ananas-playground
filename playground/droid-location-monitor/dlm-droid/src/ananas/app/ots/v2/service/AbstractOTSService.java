package ananas.app.ots.v2.service;

import ananas.app.ots.v2.pojo.OTSServiceConfig;
import ananas.app.ots.v2.pojo.OTSServiceStatus;
import ananas.app.ots.v2.tools.ContextTools;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.google.gson.Gson;

public class AbstractOTSService extends Service implements OTSServiceBinding {

	private static final myRuntimeHolder holder;
	private final Gson gson = new Gson();

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

	private static class myRuntimeHolder {

		public OTSServiceRuntime current;
		public OTSServiceConfig config;
		public OTSServiceStatus status;

		public myRuntimeHolder() {
			config = new OTSServiceConfig();
			status = new OTSServiceStatus();
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

		public void loadConfig(Service service) {
			ContextTools tools = new ContextTools(service);
			this.config = tools.loadContextPOJO(OTSServiceConfig.class);
		}

		public void loadStatus(Service service) {
			ContextTools tools = new ContextTools(service);
			this.status = tools.loadContextPOJO(OTSServiceStatus.class);
		}

		public void saveConfig(Service service) {
			ContextTools tools = new ContextTools(service);
			tools.saveContextPOJO(this.config);
		}

		public void saveStatus(Service service) {
			ContextTools tools = new ContextTools(service);
			tools.saveContextPOJO(this.status);
		}

		public void update(Service service) {
			if (status.isRunning()) {
				if (this.current == null) {
					OTSServiceRuntime rt = new OTSServiceRuntime(service,
							config, status);
					this.setCurrentRuntime(rt);
				}
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
		holder.loadConfig(this);
		holder.loadStatus(this);
		holder.update(this);
	}

	@Override
	public void onDestroy() {
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

		public void setStatus(OTSServiceStatus status) {
			target.setStatus(status);
		}

		public OTSServiceStatus getStatus() {
			return target.getStatus();
		}

		@Override
		public String getStatusText() {
			return target.getStatusText();
		}

	}

	@Override
	public void start() {
		holder.status.setRunning(true);
		holder.saveStatus(this);
		holder.update(this);
	}

	@Override
	public void stop() {
		holder.status.setRunning(false);
		holder.saveStatus(this);
		holder.update(this);
	}

	@Override
	public void restart() {
		holder.status.setRunning(false);
		holder.update(this);
		holder.status.setRunning(true);
		holder.update(this);
	}

	@Override
	public void setConfig(OTSServiceConfig conf) {
		if (conf == null) {
			return;
		}
		holder.config = new OTSServiceConfig(conf);
		holder.saveConfig(this);
	}

	@Override
	public OTSServiceConfig getConfig() {
		return new OTSServiceConfig(holder.config);
	}

	@Override
	public void setStatus(OTSServiceStatus status) {
		if (status == null) {
			return;
		}
		holder.status = new OTSServiceStatus(status);
		holder.saveStatus(this);
		holder.update(this);
	}

	@Override
	public OTSServiceStatus getStatus() {
		OTSServiceStatus status = null;
		OTSServiceRuntime rt = holder.current;
		if (rt == null) {
			status = new OTSServiceStatus();
		} else {
			status = rt.getStatus();
		}
		return new OTSServiceStatus(status);
	}

	@Override
	public String getStatusText() {
		String s = gson.toJson(this.getStatus());
		return System.currentTimeMillis() + " : " + s;
	}
}

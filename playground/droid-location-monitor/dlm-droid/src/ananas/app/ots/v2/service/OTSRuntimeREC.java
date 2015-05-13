package ananas.app.ots.v2.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import ananas.app.ots.v2.FileManager;
import ananas.app.ots.v2.pojo.LocationFragmentFormat;
import ananas.app.ots.v2.pojo.OTSLocation;
import ananas.app.ots.v2.pojo.OTSServiceStatus;
import ananas.app.ots.v2.pojo.OTSServiceTask;
import ananas.app.ots.v2.tools.StringTools;

import com.google.gson.Gson;

public class OTSRuntimeREC extends AbstractOTSRuntime {

	private final OTSServiceTask task;
	private final Gson gson = new Gson();
	private final List<OTSLocation> locBuffer = new Vector<OTSLocation>();
	private final FileManager fileMan = FileManager.Factory.getDefault();

	public OTSRuntimeREC(OTSServiceContext context) {
		super(context);
		this.task = new OTSServiceTask(context.getTask());
	}

	public void open() {
		// update status
		OTSServiceStatus status = context.getStatus();
		status.setRunning(true);
		status.setTaskStartTime(task.getStartTime());
		status.setTaskId(task.getTaskId());
		context.setStatus(status);
	}

	public void close() {
		// flush data in buffer
		this.flush();
		// update status
		OTSServiceStatus status = context.getStatus();
		status.setRunning(false);
		context.setStatus(status);

	}

	private static class Tools {

		public static long timeCode(long time) {
			return (time - (time % 60000));
		}
	}

	private void saveLocation(OTSLocation loc2) {
		final List<OTSLocation> buf = locBuffer;
		final int len = buf.size();
		boolean saved = false;
		if (len > 0) {
			final OTSLocation loc1 = buf.get(0);
			long timeCode1 = Tools.timeCode(loc1.getDeviceTime());
			long timeCode2 = Tools.timeCode(loc2.getDeviceTime());
			if ((timeCode1 != timeCode2) || (len > 256)) {
				// flush buffer
				this.flush();
				saved = true;
			}
		}
		buf.add(loc2);
		OTSServiceStatus status = context.getStatus();
		status.setTaskId(task.getTaskId());
		status.setTaskStartTime(task.getStartTime());
		status.setLocation(loc2);
		status.setCountLocation(status.getCountLocation() + 1);
		context.setStatus(status);
		if (saved) {
			context.saveStatus();
		}
	}

	private void flush() {
		final List<OTSLocation> buf = locBuffer;
		if (buf.size() <= 0) {
			return;
		}
		LocationFragmentFormat lff = new LocationFragmentFormat();
		lff.setList(buf);
		lff.setTaskId(task.getTaskId());
		LocationFragmentFormat.normal(lff);
		String js = gson.toJson(lff);
		OTSLocation loc = buf.get(0);
		File dir = fileMan.getFragmentDir();
		File file = new File(dir, "loc-frag-" + loc.getDeviceTime() + ".txt");
		try {
			StringTools.saveString(js, null, file, true);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			buf.clear();
		}
	}

	@Override
	public void onLocation(OTSLocation location) {
		this.saveLocation(location);
	}

}

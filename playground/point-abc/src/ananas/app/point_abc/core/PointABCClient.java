package ananas.app.point_abc.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class PointABCClient implements IPointABCClient {

	final String base_url = "http://puyatech.com:8080/point-abc-server/ServiceABC";
	// final String base_url =
	// "http://192.168.1.217:8080/point-abc-server/ServiceABC";

	//

	private String mUserId = "default.user";
	private String mTeamId = "default.team";
	private double mMyLat;
	private double mMyLon;

	final Map<String, Integer> mLocalDistanceTable = new HashMap<String, Integer>();
	final Map<String, Integer> mRemoteDistanceTable = new HashMap<String, Integer>();

	public Team mTeam  = new Team() ;

	public PointABCClient() {
	}

	public void setTeamId(int teamId) {
		this.mTeamId = teamId + "";
	}

	public void setUserId(int userId) {
		this.mUserId = userId + "";
	}

	public void push(IPointABCClientListener listener, IThreadProvider tp) {
		(new Thread(new MyPushRunnable(listener, tp))).start();
	}

	public void pull(IPointABCClientListener listener, IThreadProvider tp) {
		(new Thread(new MyPullRunnable(listener, tp))).start();
	}

	public void setDistance(String name, int meters) {
		if (name != null)
			this.mLocalDistanceTable.put(name, meters);
	}

	class MyAbstractRunnable implements Runnable {

		public final IThreadProvider mTP;
		public final IPointABCClientListener mL;

		public MyAbstractRunnable(IPointABCClientListener listener,
				IThreadProvider tp) {
			this.mTP = tp;
			this.mL = listener;
		}

		@Override
		public void run() {
		}
	}

	class MyPushRunnable extends MyAbstractRunnable {

		public MyPushRunnable(IPointABCClientListener listener,
				IThreadProvider tp) {
			super(listener, tp);

		}

		@Override
		public void run() {
			_dispEvent(this, IPointABCClientListener.code_begin, "push");
			try {
				final PointABCClient self = PointABCClient.this;
				JSONObject json = new JSONObject();
				json.put("team", self.mTeamId);
				json.put("user", self.mUserId);
				json.put("longitude", self.mMyLon);
				json.put("latitude", self.mMyLat);
				JSONObject dist = new JSONObject();
				for (String key : self.mLocalDistanceTable.keySet()) {
					int d = self.mLocalDistanceTable.get(key);
					dist.put(key, d);
				}
				json.put("distance", dist);
				// post with http

				String url = self.base_url;
				HttpURLConnection conn = (HttpURLConnection) (new URL(url))
						.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				OutputStream out = conn.getOutputStream();
				out.write(json.toString().getBytes("UTF-8"));
				out.flush();
				out.close();
				conn.connect();
				int code = conn.getResponseCode();
				if (code != 200) {
					String msg = conn.getResponseMessage();
					throw new RuntimeException("http " + code + " " + msg);
				}
				conn.disconnect();

			} catch (Exception e) {
				e.printStackTrace();
				_dispEvent(this, IPointABCClientListener.code_error, "push:"
						+ e.getMessage());
			}
			_dispEvent(this, IPointABCClientListener.code_end, "push");
		}

	}

	private void _dispEvent(MyAbstractRunnable ar, int code, String message) {
		try {
			MyEventRunn runn = new MyEventRunn(ar, code, message);
			ar.mTP.run(runn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MyEventRunn implements Runnable {

		private MyAbstractRunnable mAR;
		private int mCode;
		private String mMessage;

		public MyEventRunn(MyAbstractRunnable ar, int code, String message) {
			this.mAR = ar;
			this.mCode = code;
			this.mMessage = message;
		}

		@Override
		public void run() {
			try {
				PointABCClient client = PointABCClient.this;
				this.mAR.mL.onEvent(client, mCode, mMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class MyPullRunnable extends MyAbstractRunnable {

		public MyPullRunnable(IPointABCClientListener listener,
				IThreadProvider tp) {
			super(listener, tp);
		}

		@Override
		public void run() {
			_dispEvent(this, IPointABCClientListener.code_begin, "pull");
			try {
				final PointABCClient self = PointABCClient.this;
				String url = self.base_url + "?team=" + self.mTeamId;
				JSONObject json = this._getJsonByURL(url);
				Team team = new Team(json);

				String[] users = team.listUsers();
				for (String uid : users) {
					url = self.base_url + "?team=" + self.mTeamId + "&user="
							+ uid;
					json = this._getJsonByURL(url);
					User user = new User(json);
					team.addUser(user);
				}
				self.mTeam = team;

			} catch (Exception e) {
				e.printStackTrace();
				_dispEvent(this, IPointABCClientListener.code_error, "error:"
						+ e.getMessage());

			}
			_dispEvent(this, IPointABCClientListener.code_end, "pull");
		}

		private JSONObject _getJsonByURL(String url)
				throws MalformedURLException, IOException, JSONException {

			HttpURLConnection conn = (HttpURLConnection) (new URL(url))
					.openConnection();
			conn.connect();
			int code = conn.getResponseCode();
			if (code != 200) {
				String msg = conn.getResponseMessage();
				throw new RuntimeException("http " + code + " " + msg);
			}
			InputStream in = conn.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buff = new byte[128];
			for (int cb = in.read(buff); cb > 0; cb = in.read(buff)) {
				baos.write(buff, 0, cb);
			}
			in.close();
			conn.disconnect();
			buff = baos.toByteArray();
			JSONObject json = new JSONObject(new String(buff, "UTF-8"));
			return json;
		}
	}

	@Override
	public void setUserId(String uid) {
		this.mUserId = uid;
	}

	@Override
	public void setTeamId(String tid) {
		this.mTeamId = tid;
	}

	@Override
	public String getUserId() {
		return this.mUserId;
	}

	@Override
	public String getTeamId() {
		return this.mTeamId;
	}

	@Override
	public Team getTeam() {
		return this.mTeam;
	}

}

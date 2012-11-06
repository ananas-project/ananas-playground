package ananas.app.point_abc.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

public class Team {

	private final JSONObject mJSON;
	private String[] mUserList;
	private final Map<String, User> mUserTable = new HashMap<String, User>();

	public Team(JSONObject json) {
		this.mJSON = json;
	}

	public Team() {
		this.mJSON = new JSONObject();
	}

	public String[] listUsers() {
		String[] list = this.mUserList;
		if (list == null) {
			try {
				Vector<String> v = new Vector<String>();
				JSONArray mem = this.mJSON.getJSONArray("members");
				for (int i = mem.length() - 1; i >= 0; i--) {
					String uid = mem.getString(i);
					v.addElement(uid);
				}
				list = v.toArray(new String[v.size()]);
			} catch (Exception e) {
				e.printStackTrace();
				list = new String[0];
			}
			this.mUserList = list;
		}
		return list;
	}

	public void addUser(User user) {
		String key = user.getName();
		this.mUserTable.put(key, user);
	}

}

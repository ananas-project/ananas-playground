package ananas.webapp.point_abc_server;

import java.util.Hashtable;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Team {

	private final String mName;

	private final Map<String, User> mUsers = new Hashtable<String, User>();

	private JSONObject mJSON;

	public Team(String name) {
		this.mName = name;
	}

	public User getUser(String name) {
		return this.mUsers.get(name + "");
	}

	public User openUser(String name) {
		name = name + "";
		User user = this.mUsers.get(name);
		if (user == null) {
			user = new User(name);
			this.mUsers.put(name, user);
			this.mJSON = null;
		}
		return user;
	}

	public JSONObject getJSON() {
		JSONObject json = this.mJSON;
		if (json == null) {
			json = this._buildJSON();
			this.mJSON = json;
		}
		return json;
	}

	private JSONObject _buildJSON() {
		// { "team":"name", "members":["a","c","b"] }
		JSONObject json = new JSONObject();
		json.put("team", this.mName);
		JSONArray array = new JSONArray();
		for (String userName : this.mUsers.keySet()) {
			array.add(userName);
		}
		json.put("members", array);
		return json;
	}

}

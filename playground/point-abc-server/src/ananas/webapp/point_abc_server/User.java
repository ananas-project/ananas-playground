package ananas.webapp.point_abc_server;

import com.alibaba.fastjson.JSONObject;

public class User {

	// private final String mName;
	private JSONObject mJSON;

	public User(String name) {
		// this.mName = name;
	}

	public JSONObject getJSON() {
		JSONObject json = this.mJSON;
		if (json == null) {
			json = new JSONObject();
			this.mJSON = json;
		}
		return json;
	}

	public void putJSON(JSONObject json) {
		this.mJSON = json;
	}

}

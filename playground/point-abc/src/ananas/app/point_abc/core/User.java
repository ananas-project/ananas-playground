package ananas.app.point_abc.core;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	private final JSONObject mJSON;
	private String mName;

	public User(JSONObject json) {
		this.mJSON = json;
	}

	public String getName() {
		String name = this.mName;
		if (name == null) {
			try {
				name = this.mJSON.getString("user");
			} catch (JSONException e) {
				e.printStackTrace();
				name = "-";
			}
		}
		return name;
	}

}

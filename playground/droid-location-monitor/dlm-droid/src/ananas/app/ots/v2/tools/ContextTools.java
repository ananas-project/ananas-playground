package ananas.app.ots.v2.tools;

import ananas.app.ots.v2.pojo.POJO;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class ContextTools {

	private final Context context;
	private final Gson gs;

	public ContextTools(Context context) {
		this.context = context;
		this.gs = new Gson();
	}

	public <T extends POJO> T loadContextPOJO(Class<T> clazz) {
		String name = clazz.getName();
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		String value = sp.getString(name, "{}");
		return gs.fromJson(value, clazz);
	}

	public void saveContextPOJO(POJO pojo) {
		String name = pojo.getClass().getName();
		String value = gs.toJson(pojo);
		SharedPreferences sp = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		sp.edit().putString(name, value).commit();
	}

}

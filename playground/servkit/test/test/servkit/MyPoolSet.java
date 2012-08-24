package test.servkit;

import ananas.lib.servkit.json.JSON;
import ananas.lib.servkit.pool.DefaultPool;
import ananas.lib.servkit.pool.DefaultPoolSet;

public class MyPoolSet extends DefaultPoolSet {

	public MyPoolSet() {

		this.addPool(new DefaultPool(JSON.class_array, 100));
		this.addPool(new DefaultPool(JSON.class_object, 100));

		this.addPool(new DefaultPool(JSON.class_long, 100));
		this.addPool(new DefaultPool(JSON.class_int, 100));
		this.addPool(new DefaultPool(JSON.class_double, 100));

		this.addPool(new DefaultPool(JSON.class_string, 100));

	}
}

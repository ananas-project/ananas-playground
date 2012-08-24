package ananas.lib.servkit.json.io;

import ananas.lib.servkit.json.object.IJsonValue;
import ananas.lib.servkit.pool.IBasePool;

public interface IJsonObjectBuilder extends IJsonHandler {

	IJsonValue getRoot();

	IBasePool getPool();

}

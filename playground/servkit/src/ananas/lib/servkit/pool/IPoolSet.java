package ananas.lib.servkit.pool;

public interface IPoolSet extends IBasePool {

	void addPool(IPool pool);

	IPool getPool(Class<?> aClass);

}

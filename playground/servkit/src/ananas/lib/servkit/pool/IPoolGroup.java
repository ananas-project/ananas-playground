package ananas.lib.servkit.pool;

public interface IPoolGroup extends IPool {

	void addPool(Class<?> aClass, IPool pool);

}

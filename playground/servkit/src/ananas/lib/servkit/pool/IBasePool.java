package ananas.lib.servkit.pool;

public interface IBasePool {

	IPoolable alloc(Class<?> aClass);

	void dealloc(IPoolable object);

}

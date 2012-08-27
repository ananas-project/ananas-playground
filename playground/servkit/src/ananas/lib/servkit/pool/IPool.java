package ananas.lib.servkit.pool;

public interface IPool {

	IPoolable alloc(Class<?> aClass);

}

package ananas.lib.servkit.pool;

public interface IClassPool extends IPool {

	Class<?> getPoolableClass();

	int getSize();

	void reset();

}

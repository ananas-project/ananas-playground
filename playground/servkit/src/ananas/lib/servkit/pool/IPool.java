package ananas.lib.servkit.pool;

public interface IPool extends IBasePool {

	Class<?> getTargetClass();

	int getSize();
}

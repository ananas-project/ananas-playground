package ananas.lib.servkit.pool;

public interface IClassPoolFactory {

	IClassPool newPool(Class<?> aClass, int size, boolean resetable);
}

package ananas.lib.servkit.pool;

public interface ISinglePoolFactory {

	ISinglePool newPool(IPoolableFactory itemFactory, int size,
			boolean resetable);
}

package ananas.lib.servkit.pool;

public interface IPoolable {

	void onFree();

	void onAlloc();

	void free();

	boolean isFree();

	void bindToPool(IPool pool);

	IPool getPool();

}

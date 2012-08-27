package ananas.lib.servkit.pool;

public interface IPoolableEx extends IPoolable {

	void onFree();

	void onAlloc();

}

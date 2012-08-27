package ananas.lib.servkit.pool;

public interface IProbe {

	void free();

	boolean isFree();

	void onAlloc();

	void onFree();

	IPoolable toPoolable();

}

package ananas.lib.servkit.pool;

public interface IPoolable {

	void free();

	IProbe  toProbe();

}

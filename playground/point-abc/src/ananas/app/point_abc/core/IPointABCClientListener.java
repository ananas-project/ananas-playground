package ananas.app.point_abc.core;

public interface IPointABCClientListener {

	int code_begin = 1;
	int code_end = 2;
	int code_error = 3;

	void onEvent(IPointABCClient client, int code, String message);

}

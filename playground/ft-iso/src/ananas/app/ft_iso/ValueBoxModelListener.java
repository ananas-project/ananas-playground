package ananas.app.ft_iso;

public interface ValueBoxModelListener {

	void onChanged(ValueBoxModel model);

	int key_minus = 1;
	int key_plus = 2;
	int key_title = 3;

	void onClick(ValueBoxModel model, int key);

}

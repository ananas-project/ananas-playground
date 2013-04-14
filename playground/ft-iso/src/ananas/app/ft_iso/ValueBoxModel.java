package ananas.app.ft_iso;

public interface ValueBoxModel {

	void doPlus();

	void doMinus();

	void doSelect();

	String getTitle();

	String getValue();

	boolean isEnableSet();

	boolean isEnableSelect();

	void addListener(ValueBoxModelListener l);

	void removeListener(ValueBoxModelListener l);
}

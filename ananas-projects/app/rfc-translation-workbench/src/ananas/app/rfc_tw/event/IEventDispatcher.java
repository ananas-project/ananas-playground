package ananas.app.rfc_tw.event;

public interface IEventDispatcher {

	void dispacheEvent(Event event);

	void addListener(IEventListener listener);

	void removeListener(IEventListener listener);

}

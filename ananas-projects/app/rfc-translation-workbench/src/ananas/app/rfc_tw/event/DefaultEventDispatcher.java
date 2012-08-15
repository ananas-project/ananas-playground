package ananas.app.rfc_tw.event;

import java.util.LinkedList;
import java.util.List;

public class DefaultEventDispatcher implements IEventDispatcher {

	private final List<IEventListener> mListenerList;

	public DefaultEventDispatcher() {
		this.mListenerList = new LinkedList<IEventListener>();
	}

	@Override
	public void dispacheEvent(Event event) {
		for (IEventListener item : this.mListenerList) {
			try {
				item.onEvent(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addListener(IEventListener listener) {
		if (!this.mListenerList.contains(listener)) {
			this.mListenerList.add(listener);
		}
	}

	@Override
	public void removeListener(IEventListener listener) {
		this.mListenerList.remove(listener);
	}

}

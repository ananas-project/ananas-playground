package ananas.app.rfc_tw.model;

import ananas.app.rfc_tw.event.DefaultEvent;
import ananas.app.rfc_tw.event.DefaultEventDispatcher;
import ananas.app.rfc_tw.event.IEventDispatcher;
import ananas.app.rfc_tw.event.IEventListener;

public interface IProject {

	String getOriginalText();

	void setOriginalText(String text);

	void addOriginalTextListener(IEventListener listener);

	void removeOriginalTextListener(IEventListener listener);

	IDictionary getDictionary();

	public static class Factory {

		public static IProject newProject() {
			return new MyImpl();
		}

		private static class MyImpl implements IProject {

			private String mOriginalText;
			private final IEventDispatcher mOriginalTextEventDisp;
			private IDictionary mDictionary;

			public MyImpl() {
				this.mOriginalTextEventDisp = new DefaultEventDispatcher();
			}

			@Override
			public String getOriginalText() {
				return this.mOriginalText;
			}

			@Override
			public void setOriginalText(String text) {
				this.mOriginalText = text;
				this.mOriginalTextEventDisp.dispacheEvent(new DefaultEvent());
			}

			@Override
			public void addOriginalTextListener(IEventListener listener) {
				this.mOriginalTextEventDisp.addListener(listener);
			}

			@Override
			public void removeOriginalTextListener(IEventListener listener) {
				this.mOriginalTextEventDisp.removeListener(listener);
			}

			@Override
			public IDictionary getDictionary() {
				return this.mDictionary;
			}
		}
	}

}

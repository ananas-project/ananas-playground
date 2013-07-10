package ananas.lib.util.logging;

public interface Level {

	Level ALL = Facotry.newInst("all");

	class Facotry {

		public static Level newInst(String string) {
			return new MyImpl(string);
		}

		private static class MyImpl implements Level {

			private final String mText;

			public MyImpl(String string) {
				this.mText = string;
			}

			public String toString() {
				return this.mText;
			}
		}
	}
}

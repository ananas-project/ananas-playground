package ananas.lib.axk.engine;

public interface XPhase {

	XPhase init = F._new("init");
	XPhase connect = F._new("connect");
	XPhase tls = F._new("tls");
	XPhase sasl = F._new("sasl");
	XPhase bind = F._new("bind");
	XPhase online = F._new("online");
	XPhase error = F._new("error");
	XPhase closed = F._new("closed");

	class F {

		private static XPhase _new(String string) {
			return new MyImpl(string);
		}

		private static class MyImpl implements XPhase {

			private final String mString;

			public MyImpl(String string) {
				this.mString = string;
			}

			@Override
			public String toString() {
				return this.mString;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result
						+ ((mString == null) ? 0 : mString.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				MyImpl other = (MyImpl) obj;
				if (mString == null) {
					if (other.mString != null)
						return false;
				} else if (!mString.equals(other.mString))
					return false;
				return true;
			}

		}
	}
}

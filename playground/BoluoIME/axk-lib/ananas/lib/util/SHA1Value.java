package ananas.lib.util;

public class SHA1Value {

	public static void main(String[] arg) {
		{
			SHA1Value sv1 = new SHA1Value();
			System.out.println(sv1 + " int hash:" + sv1.hashCode());
		}
		SHA1Value sv0;
		{
			sv0 = new SHA1Value("4a18e7705fd76219883167cade0fa57753c36d36");
			System.out.println(sv0);
		}
		{
			SHA1Value sv1 = new SHA1Value(sv0);
			System.out.println(sv1 + " int hash:" + sv1.hashCode());
		}
		{
			byte[] buff = sv0.getBytes();
			SHA1Value sv1 = new SHA1Value(buff, 0, buff.length);
			System.out.println(sv1 + " int hash:" + sv1.hashCode());
		}
		{
			SHA1Value sv1 = new SHA1Value(sv0.toString());
			System.out.println(sv1.toString(true) + " int hash:"
					+ sv1.hashCode());
		}
	}

	private final static int s_byte_array_size = 20;
	private final byte[] mBytes;

	public SHA1Value() {
		this.mBytes = new byte[s_byte_array_size];
	}

	public SHA1Value(SHA1Value other) {
		this.mBytes = other.mBytes;
	}

	public SHA1Value(String s) throws NumberFormatException {

		final int strlen = s.length();
		if (strlen != (s_byte_array_size * 2)) {
			throw new NumberFormatException("not a sha-1 string");
		}
		this.mBytes = new byte[s_byte_array_size];
		for (int i = this.mBytes.length - 1; i >= 0; i--) {
			int ix2 = i * 2;
			char ch0 = s.charAt(ix2);
			char ch1 = s.charAt(ix2 + 1);
			int n1, n0;
			n0 = MyHelper.charToInt(ch0);
			n1 = MyHelper.charToInt(ch1);
			byte b = (byte) ((n0 * 16) + n1);
			this.mBytes[i] = b;
		}
	}

	public SHA1Value(byte[] buff, int off, int len)
			throws NumberFormatException {
		if (len != s_byte_array_size) {
			throw new NumberFormatException("not a sha-1 bytes array");
		}
		this.mBytes = new byte[len];
		for (int i = len - 1; i >= 0; i--) {
			this.mBytes[i] = buff[off + i];
		}
	}

	private static final class MyHelper {

		private static char[] s_num_char_array_up = null;
		private static char[] s_num_char_array_low = null;

		public static char[] getNumCharArray(boolean upcase) {
			if (upcase) {
				char[] array = s_num_char_array_up;
				if (array == null) {
					array = "0123456789ABCDEF".toCharArray();
					s_num_char_array_up = array;
				}
				return array;
			} else {
				char[] array = s_num_char_array_low;
				if (array == null) {
					array = "0123456789abcdef".toCharArray();
					s_num_char_array_low = array;
				}
				return array;
			}
		}

		public static int charToInt(char ch) {
			if ('0' <= ch && ch <= '9') {
				return (ch - '0');
			} else if ('a' <= ch && ch <= 'f') {
				return (ch - 'a') + 10;
			} else if ('A' <= ch && ch <= 'F') {
				return (ch - 'A') + 10;
			} else {
				throw new NumberFormatException("not a sha-1 string");
			}
		}
	}

	public String toString(boolean upcase) {
		char[] array = MyHelper.getNumCharArray(upcase);
		StringBuilder sb = new StringBuilder();
		for (byte b : this.mBytes) {
			int n = b & 0x00ff;
			sb.append(array[0x0f & (n >> 4)]);
			sb.append(array[0x0f & n]);
		}
		return sb.toString();
	}

	public String toString() {
		return this.toString(false);
	}

	public byte[] getBytes() {
		return this.getBytes(null);
	}

	public byte[] getBytes(byte[] buff) {
		if (buff == null) {
			buff = new byte[this.mBytes.length];
		} else {
			if (buff.length != this.mBytes.length) {
				buff = new byte[this.mBytes.length];
			}
		}
		for (int i = buff.length - 1; i >= 0; i--) {
			buff[i] = this.mBytes[i];
		}
		return buff;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return (this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof SHA1Value) {
			SHA1Value o2 = (SHA1Value) obj;
			byte[] buff2 = o2.mBytes;
			byte[] buff1 = this.mBytes;
			for (int i = buff1.length - 1; i >= 0; i--) {
				if (buff2[i] != buff1[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int n1, n2, n3, n0;
		n0 = n1 = n2 = n3 = 0;
		for (int i = this.mBytes.length - 4; i >= 0; i -= 4) {
			n0 ^= this.mBytes[i];
			n1 ^= this.mBytes[i + 1];
			n2 ^= this.mBytes[i + 2];
			n3 ^= this.mBytes[i + 3];
		}
		int n = ((n0 << 24) & 0xff000000) | ((n1 << 16) & 0x00ff0000)
				| ((n2 << 8) & 0xff00) | ((n3) & 0xff);
		return n;
	}

}

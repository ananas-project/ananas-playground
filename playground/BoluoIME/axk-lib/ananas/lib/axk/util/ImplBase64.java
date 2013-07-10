package ananas.lib.axk.util;

import java.io.ByteArrayOutputStream;

class ImplBase64 extends Base64 {

	private final static Base64 sInst = new ImplBase64();

	public static Base64 _theInstance() {
		return sInst;
	}

	@Override
	public byte[] doDecode(String string) {
		return _impl_decode(string);
	}

	@Override
	public String doEncode(byte[] data) {
		return _impl_encode(data);
	}

	// ////////////////////////////////////////////////////////////////

	// / 0 1 2 3 4 5 6 7
	// / 01234567890123456789012345678901234567890123456789012345678901234567890
	private static char[] sTable64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();

	public static String _impl_encode(final byte[] buf) {
		return _impl_encode(buf, 0, buf.length, null).toString();
	}

	public static StringBuffer _impl_encode(final byte[] buf, final int offset,
			final int length, final StringBuffer strbuf) {
		final StringBuffer sbuf;
		if (strbuf == null) {
			sbuf = new StringBuffer();
		} else {
			sbuf = strbuf;
			strbuf.setLength(0);
		}
		final int end = (offset + length);
		int buf24 = 0;
		int cntBit = 0;
		int countChar = 0;
		for (int i = offset; i < end; i++) {
			// in
			final int b = buf[i];
			buf24 = (buf24 << 8) | (b & 0x000000ff);
			cntBit += 8;
			// out
			while (6 <= cntBit) {
				final int _6bits = buf24 >> (cntBit - 6);
				cntBit -= 6;
				sbuf.append(sTable64[_6bits & 0x3f]);
				countChar++;
				if ((countChar % 76) == 0) {
					if (countChar > 0)
						sbuf.append("\r\n");
				}
			}
		}
		if (0 < cntBit) {
			final int _6bits = buf24 << (6 - cntBit);
			sbuf.append(sTable64[_6bits & 0x3f]);
		}
		for (int i = (length % 3); (0 < i) && (i < 3); i++) {
			sbuf.append('=');
		}
		return sbuf;
	}

	public static byte[] _impl_decode(final String b64string) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final char[] chars = b64string.toCharArray();
		final int end = chars.length;
		int buf24 = 0;
		int cntbit = 0;
		for (int i = 0; i < end; i++) {
			final int val;
			final char c = chars[i];
			if (c == '+') {
				val = 62;
			} else if (c == '/') {
				val = 63;
			} else if (('0' <= c) && (c <= '9')) {
				val = 52 + (c - '0');
			} else if (('a' <= c) && (c <= 'z')) {
				val = 26 + (c - 'a');
			} else if (('A' <= c) && (c <= 'Z')) {
				val = (c - 'A');
			} else {
				val = -1;
			}
			if (0 <= val) {
				buf24 = (buf24 << 6) | (val & 63);
				cntbit += 6;
			}
			while (cntbit >= 8) {
				cntbit -= 8;
				baos.write((buf24 >> cntbit) & 0xff);
			}
		}
		return baos.toByteArray();
	}

}

package xukun.cd_stream_rx;

import java.io.IOException;
import java.io.OutputStream;

public class LevelMeter extends OutputStream {

	class Track {

		final byte[] buffer = new byte[256];
		int cb = 0;
		int sigma;

		public void append(int newb) {

			newb = Math.abs(newb);

			int index = (cb++) & 0xffff;
			index = index % buffer.length;

			byte oldb = buffer[index];
			buffer[index] = (byte) newb;
			sigma -= oldb;
			sigma += newb;

			if (index == 0) {
				String s = "================================================";
				final int len = s.length();
				int len2 = (len * sigma) / (buffer.length * 32);
				len2 = Math.min(len2, len);
				len2 = Math.max(len2, 0);
				s = s.substring(len - len2);
				System.out.println(s);
			}

		}
	}

	final Track[] tracks = new Track[4];
	int cb;

	public LevelMeter() {

		for (int i = tracks.length - 1; i >= 0; i--) {
			tracks[i] = new Track();
		}

	}

	@Override
	public void write(int b) throws IOException {

		final int index = (cb++) & 0xffff;
		final Track tr = tracks[index % tracks.length];
		tr.append(b);

	}

}

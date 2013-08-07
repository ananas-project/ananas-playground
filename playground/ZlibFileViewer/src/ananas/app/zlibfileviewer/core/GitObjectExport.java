package ananas.app.zlibfileviewer.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.zip.InflaterInputStream;

public class GitObjectExport {

	private final File _obj_file;
	private final File _dest_dir;

	public GitObjectExport(File objectFile, File destDirectory) {
		this._obj_file = objectFile;
		this._dest_dir = destDirectory;
	}

	public File getObjectFile() {
		return this._obj_file;
	}

	public File getDestDirectory() {
		return this._dest_dir;
	}

	public boolean doExport() {
		try {
			final File src = this._obj_file;
			final InputStream in0 = new FileInputStream(src);
			final InputStream in = new InflaterInputStream(in0);

			final File tmpWithHead = new File(this._dest_dir, src.getName()
					+ ".with.head");
			final File tmpWithoutHead = new File(this._dest_dir, src.getName()
					+ ".without.head");

			final OutputStream outH = new FileOutputStream(tmpWithHead);
			final MyKillHeadOutputStream outNoH = new MyKillHeadOutputStream(
					tmpWithoutHead);
			final MySha1OS outSha1 = new MySha1OS();
			byte[] buff = new byte[1024];
			for (int cb = in.read(buff, 0, buff.length); cb > 0; cb = in.read(
					buff, 0, buff.length)) {
				outH.write(buff, 0, cb);
				outNoH.write(buff, 0, cb);
				outSha1.write(buff, 0, cb);
			}

			outH.flush();
			outNoH.flush();
			outSha1.flush();

			outH.close();
			outNoH.close();
			outSha1.close();
			in.close();
			in0.close();

			System.out.println("src file name is " + src.getName());
			System.out.println("calc sha1 is " + outSha1.getResultString());
			System.out.println("calc sha1 is " + outSha1.getResultString());
			final String sha1 = outSha1.getResultString();

			// rename
			final File fileWithHead, fileWithoutHead;
			fileWithHead = new File(tmpWithHead.getParentFile(), sha1
					+ ".gitobj");
			fileWithoutHead = new File(tmpWithHead.getParentFile(), sha1 + "."
					+ outNoH.getHeadString() + ".gitobj");
			tmpWithHead.renameTo(fileWithHead);
			tmpWithoutHead.renameTo(fileWithoutHead);

			this.setProperty(GitObjectExport.key_sha_1, sha1);
			this.setProperty(GitObjectExport.key_output_path_with_head,
					fileWithHead.getAbsolutePath());
			this.setProperty(GitObjectExport.key_output_path_no_head,
					fileWithoutHead.getAbsolutePath());

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	class MyKillHeadOutputStream extends OutputStream {

		private final FileOutputStream _out;
		private boolean head_end = false;
		private final StringBuilder _hd = new StringBuilder();

		public MyKillHeadOutputStream(File file) throws FileNotFoundException {
			this._out = new FileOutputStream(file);
		}

		public String getHeadString() {
			return this._hd.toString();
		}

		@Override
		public void write(int b) throws IOException {
			if (this.head_end) {
				_out.write(b);
			} else {
				if (b == 0) {
					this.head_end = true;
				} else {
					_hd.append((char) b);
				}
			}
		}

		@Override
		public void close() throws IOException {
			_out.close();
		}
	}

	class MySha1OS extends OutputStream {

		private final MessageDigest _md;

		public MySha1OS() throws NoSuchAlgorithmException {
			this._md = MessageDigest.getInstance("SHA-1");
		}

		private void __write(byte[] buffer, int offset, int length)
				throws IOException {

			_md.update(buffer, offset, length);

		}

		private final byte[] _1byte_buffer = new byte[1];
		private byte[] _result;

		@Override
		public void write(int b) throws IOException {
			_1byte_buffer[0] = (byte) b;
			this.__write(_1byte_buffer, 0, 1);
		}

		@Override
		public void write(byte[] buffer, int offset, int length)
				throws IOException {
			this.__write(buffer, offset, length);
		}

		public byte[] getResult() {
			return this._result;
		}

		public void close() {
			this._result = _md.digest();
		}

		public String getResultString() {
			StringBuilder sb = new StringBuilder();
			char[] chs = "0123456789abcdef".toCharArray();
			byte[] bytes = this.getResult();
			for (byte b : bytes) {
				int h, l;
				h = b >> 4;
				l = b;
				sb.append(chs[h & 0x0f]);
				sb.append(chs[l & 0x0f]);
			}
			return sb.toString();
		}

	}

	private final Properties _prop = new Properties();

	public static final String key_ = "";
	public static final String key_output_path_no_head = "1";
	public static final String key_output_path_with_head = "2";
	public static final String key_sha_1 = "3";

	public String getProperty(String key) {
		return _prop.getProperty(key, "null");
	}

	public void setProperty(String key, String value) {
		_prop.setProperty(key, value);
	}

}

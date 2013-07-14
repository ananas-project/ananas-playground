package ananas.lib.impl.axk.client.conn.v2;

import java.io.OutputStream;

public class ConnV2TxBuffer implements Runnable {

	private ConnV2OutputAgent m_outputAgent;
	private byte[] m_buffer;
	private int m_offset;
	private int m_length;

	public ConnV2TxBuffer(byte[] buffer, int offset, int length) {
		this.m_buffer = buffer;
		this.m_offset = offset;
		this.m_length = length;
	}

	public void setOutputAgent(ConnV2OutputAgent oa) {
		this.m_outputAgent = oa;
	}

	public Runnable getRunnable() {
		return this;
	}

	@Override
	public void run() {
		try {
			// System.err.println(this + ".not impl");
			OutputStream out = this.m_outputAgent.getOutputStream();
			out.write(this.m_buffer, this.m_offset, this.m_length);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

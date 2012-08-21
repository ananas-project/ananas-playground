package xk.rtl.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PushPort
 */
public class PushPort extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PushPort() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		System.out.println(this + ".doPost()");

		OutputStream os = System.out;
		InputStream is = request.getInputStream();
		System.out.println("bytes[");
		this.pump(is, os);
		System.out.println("]");

	}

	private void pump(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[123];
		for (int cb = is.read(buf); cb > 0; cb = is.read(buf)) {
			os.write(buf, 0, cb);
		}
		os.flush();
	}

}

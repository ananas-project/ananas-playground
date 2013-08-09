package ananas.objecthub.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ananas.objecthub.core.IObject;
import ananas.objecthub.core.IObjectRepo;
import ananas.objecthub.util.StreamPump;

/**
 * Servlet implementation class Object
 */
public class Object extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String key_object_sha1 = "object.sha-1";
	private static final String mime_type = "app/object+sha-1";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Object() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		IObjectRepo om = IObjectRepo.Agent.getInstance();
		String sha1 = request.getHeader(key_object_sha1);
		IObject obj = om.getObject(sha1);
		if (obj == null) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		response.setContentLength(obj.getFileLength());
		response.setContentType(mime_type);
		InputStream in = obj.openInputStream();
		OutputStream out = response.getOutputStream();
		(new StreamPump()).doPump(in, out);
		in.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doHead(HttpServletRequest, HttpServletResponse)
	 */
	protected void doHead(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}

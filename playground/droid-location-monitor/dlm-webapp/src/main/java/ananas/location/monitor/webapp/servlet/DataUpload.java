package ananas.location.monitor.webapp.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import ananas.location.monitor.webapp.FileManager;
import ananas.location.monitor.webapp.tools.IOTools;

@MultipartConfig
public class DataUpload extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DataUpload() {
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

		InputStream in = null;

		try {
			FileManager fm = FileManager.Factory.getManager();
			Collection<Part> parts = request.getParts();
			for (Part part : parts) {
				long len = part.getSize();
				String name = part.getName();
				String type = part.getContentType();
				in = part.getInputStream();

				System.out.println();
				System.out.println();
				System.out.println("name:" + name);
				System.out.println("type:" + type);
				System.out.println("len:" + len);
				System.out.println();
				
		//		IOTools.pump(in, System.out, null);


	//		obj=	fm.addObject ( type, len, in   ) ;
				
				IOTools.close(in);
				in = null;

			}

		} finally {
			IOTools.close(in);
		}

		String url = "upload-done.html?hash=" + in;
		request.getRequestDispatcher(url).forward(request, response);

	}

}

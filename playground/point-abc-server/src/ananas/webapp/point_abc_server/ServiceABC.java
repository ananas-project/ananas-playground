package ananas.webapp.point_abc_server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class ServiceABC
 */
public class ServiceABC extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Core mCore;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServiceABC() {
		super();
		this.mCore = Core.getDefault();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JSONObject json = null;
		final String team = request.getParameter("team");
		final Team aTeam = this.getCore().getTeam(team);
		if (aTeam != null) {
			final String user = request.getParameter("user");
			User aUser = aTeam.getUser(user);
			if (aUser != null) {
				json = aUser.getJSON();
			} else {
				json = aTeam.getJSON();
			}
		} else {
			json = new JSONObject();
			json.put("team", team + "");
		}

		response.setStatus(200);
		response.setContentType("text/plain");
		ServletOutputStream out = response.getOutputStream();
		out.print(json.toString());
	}

	private Core getCore() {
		if (this.mCore == null) {
			this.mCore = Core.getDefault();
		}
		return this.mCore;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServletInputStream in = request.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[128];
		for (int cb = in.read(buf); cb > 0; cb = in.read(buf)) {
			baos.write(buf, 0, cb);
		}
		buf = baos.toByteArray();
		JSONObject json = JSON.parseObject(new String(buf, "UTF-8"));
		String team = json.getString("team");
		String user = json.getString("user");
		Team aTeam = this.getCore().openTeam(team);
		User aUser = aTeam.openUser(user);
		aUser.putJSON(json);
	}
}

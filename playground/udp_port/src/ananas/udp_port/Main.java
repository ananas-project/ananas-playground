package ananas.udp_port;

public class Main {

	public static void main(String[] arg) {
		Runnable runn = null;
		for (String param : arg) {
			if (param == null) {
			} else if (param.equalsIgnoreCase("server")) {
				runn = new UdpPortServer();
			} else {
			}
		}
		if (runn == null) {
			runn = new UdpPortClient();
		}
		runn.run();
	}

}

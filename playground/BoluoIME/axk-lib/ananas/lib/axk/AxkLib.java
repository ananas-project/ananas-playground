package ananas.lib.axk;

import java.io.InputStream;
import java.util.Properties;

import ananas.lib.axk.api.IExConnection;
import ananas.lib.axk.constant.XmppStatus;
import ananas.lib.axk.event.PhaseEvent;

public class AxkLib {

	public static void main(String[] arg) {

		AxkLib lib = new AxkLib();
		Properties prop = lib.loadTestingProperties();

		DefaultXmppAccount account = new DefaultXmppAccount();
		account.setAddress(prop.getProperty("jid"));
		account.host = prop.getProperty("host");
		account.port = Integer.parseInt(prop.getProperty("port"));
		account.password = prop.getProperty("password");
		account.resource = prop.getProperty("resource");
		account.ignoreSSLError = false;
		account.useSSL = false;

		XmppEnvironment envi = XmppUtil.getDefaultEnvironment();
		XmppClient client = envi.getClientFactory().newClient(account);

		client.setXmppEventListener(new XmppEventListener() {

			@Override
			public void onEvent(XmppEvent event) {
				// System.out.println("" + event);
				if (event == null) {
				} else if (event instanceof PhaseEvent) {
					PhaseEvent pe = (PhaseEvent) event;
					XmppStatus pnew = pe.getNewPhase();
					XmppStatus pold = pe.getOldPhase();
					System.out.println("xmpp phase : " + pold + " -> " + pnew);
				} else {
				}
			}
		});

		IExConnection core = (IExConnection) client
				.getExAPI(IExConnection.class);
		core.connect();
	}

	private Properties loadTestingProperties() {
		Properties prop = new Properties();
		try {
			String name = "account.properties";
			InputStream in = this.getClass().getResourceAsStream(name);
			if (in == null) {
				System.err.println("no file : " + name);
			}
			prop.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}

}

package br.unb.cic.qrgame.core;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import android.os.AsyncTask;
import android.util.Log;
import br.unb.cic.qrgame.core.XMPPInformation;

public final class XMPPConnect extends
		AsyncTask<XMPPInformation, Void, ExceptionType> {
	@Override
	protected ExceptionType doInBackground(XMPPInformation... info) {
		ConnectionConfiguration connConfig;
		connConfig = new ConnectionConfiguration(info[0].getHost(),
				info[0].getPort());
		connConfig.setSecurityMode(SecurityMode.disabled);
		connConfig.setDebuggerEnabled(true);

		XMPPConnection connection = new XMPPTCPConnection(connConfig);

		try {
			connection.connect();
		} catch (Exception e) {
			Log.e("ServerConnection", e.toString());
			info[0].setConnection(null);
			return ExceptionType.SERVER;
		}

		try {
			connection.login(info[0].getUser(), info[0].getPassword());
		} catch (Exception e) {
			Log.e("LoginConnection", e.toString());
			info[0].setConnection(null);
			return ExceptionType.LOGIN;
		}

		info[0].setConnection(connection);
		return ExceptionType.NONE;
	}
}

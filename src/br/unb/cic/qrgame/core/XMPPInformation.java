package br.unb.cic.qrgame.core;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.Context;
import android.content.ContextWrapper;

public final class XMPPInformation extends ContextWrapper {
	private final String host = AppProperties.INSTANCE.readProperty(this,
			"host");
	private final int port = Integer.parseInt(AppProperties.INSTANCE
			.readProperty(this, "port"));

	private String user;
	private String password;
	private XMPPConnection connection;
	private MultiUserChat chat;
	
	public XMPPInformation(Context context) {
		super(context);
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public MultiUserChat getChat() {
		return chat;
	}
	
	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setChat(MultiUserChat chat) {
		this.chat = chat;
	}
}

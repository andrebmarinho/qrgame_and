package br.unb.cic.qrgame.domain;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import br.unb.cic.qrgame.core.ExceptionType;
import br.unb.cic.qrgame.core.XMPPConnect;
import br.unb.cic.qrgame.core.XMPPInformation;
import br.unb.cic.qrgame.exception.ChatException;
import br.unb.cic.qrgame.exception.LoginException;
import br.unb.cic.qrgame.exception.ServerException;
import android.content.Context;
import android.util.Log;

public class XMPPClient {
	XMPPInformation info;

	public XMPPClient(Context context) {
		info = new XMPPInformation(context);
	}

	public void loginToServer(String user, String password)
			throws ServerException, LoginException {
		info.setUser(user);
		info.setPassword(password);

		ExceptionType eType;
		try {
			eType = new XMPPConnect().execute(info).get();
		} catch (Exception e) {
			Log.e("XMPPConnectInterrupt", e.toString());
			throw new ServerException("Não foi possível conectar ao servidor");
		}

		switch (eType) {
		case NONE:
			settingPresence("available");
			break;

		case SERVER:
			throw new ServerException("Não foi possível conectar ao servidor");

		case LOGIN:
			throw new LoginException("Dados de Usuário e/ou senha incorretos");
		}

	}

	public void settingPresence(String option) {
		Presence presence;
		XMPPConnection connection = info.getConnection();

		if (option.equals("available")) {
			presence = new Presence(Presence.Type.available);
			presence.setStatus("Ativo");
		} else {
			presence = new Presence(Presence.Type.unavailable);
			presence.setStatus("Ausente");
		}

		try {
			connection.sendPacket(presence);
		} catch (NotConnectedException e) {
			e.printStackTrace();
			Log.i("client", e.getMessage());
		}
	}

	public void createChat(String chatName) throws ChatException {
		final String room = chatName + "@conference." + info.getHost();
		MultiUserChat chat;

		try {
			assert (info.getConnection() != null);
			chat = new MultiUserChat(info.getConnection(), room);
			chat.create(info.getConnection().getUser());
			Form form = chat.getConfigurationForm();
			Form submitForm = form.createAnswerForm();
			List<FormField> fields = form.getFields();

			for (int i = 0; i < fields.size(); i++) {
				FormField field = fields.get(i);
				if ((!FormField.TYPE_HIDDEN.equals(field.getType()))
						&& (field.getVariable() != null)) {
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}

			List<String> owners = new ArrayList<String>();
			owners.add(info.getConnection().getUser());
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			submitForm.setAnswer("muc#roomconfig_roomname", room);
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			chat.sendConfigurationForm(submitForm);
		} catch (Exception e) {
			Log.e("createChat", e.toString());
			info.setChat(null);
			throw new ChatException("Nao foi possivel criar a sala");
		}
		info.setChat(chat);
	}

	public void joinChat(String chatName) throws ChatException {
		final String room = chatName + "@conference." + info.getHost();
		MultiUserChat chat;

		try {
			assert (info.getConnection() != null);
			chat = new MultiUserChat(info.getConnection(), room);
			chat.join(info.getConnection().getUser());
		} catch (Exception e) {
			Log.e("joinChat", e.toString());
			throw new ChatException("Nao foi possivel entrar na sala");
		}
		info.setChat(chat);
	}

	public List<String> findRoomMembers() {
		List<String> listMembers = new ArrayList<String>();
		MultiUserChat chat = info.getChat();

		if (chat == null) {
			return null;
		}

		List<String> occupants = chat.getOccupants();
		for (int i = 0; i < occupants.size(); i++) {
			String name = StringUtils.parseResource(occupants.get(i));
			listMembers.add(name);
		}

		return listMembers;
	}
}

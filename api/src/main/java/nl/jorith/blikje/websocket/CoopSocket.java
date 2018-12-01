package nl.jorith.blikje.websocket;

import com.google.gson.Gson;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value="/ws/coop/{coopKey}")
public class CoopSocket {
	private static final Logger logger = Logger.getLogger(CoopSocket.class.getName());

	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	private static Map<String, List<Blikje>> coopSessions = Collections.synchronizedMap(new HashMap<>());

	@OnOpen
	public void registerClient(Session client, @PathParam("coopKey") String coopKey) {
		String coopKeyUpperCase = coopKey.toUpperCase();

		client.getUserProperties().put("coopKey", coopKeyUpperCase);
		client.getUserProperties().put("connectedSince", new Date());

		clients.add(client);

		logger.info("Client [" + client.getId() + "] connected. Coop key: '" + coopKey + "'");
		logger.info("Number of clients: " + clients.size());
	}

	@OnClose
	public void unregisterClient(Session client) {
		clients.remove(client);
		logger.info("Client [" + client.getId() + "] disconnected");
		logger.info("Number of clients: " + clients.size());

	}

	@OnMessage
	public void onMessage(String message, Session sender) {
		logger.info("onMessage: " + message);
		String senderCoopKey = (String) sender.getUserProperties().get("coopKey");

		SocketMessage socketMessage = new Gson().fromJson(message, SocketMessage.class);

		int amount;
		if (socketMessage.getAdd() != null && socketMessage.getAdd()) {
			amount = 1;
		} else {
			amount = -1;
		}

		CoopMode.INSTANCE.updateSession(senderCoopKey, socketMessage.getBlikje(), amount);

		synchronized (clients) {
			for (Session client : clients) {
				if (client.isOpen()) {
					String clientCoopKey = (String) client.getUserProperties().get("coopKey");

					if (clientCoopKey.equals(senderCoopKey)) {
						try {
							client.getBasicRemote().sendText(message);
						} catch (IOException e) {
							logger.log(Level.WARNING, "Error while sending message", e);
							throw new RuntimeException(e);
						}
					}
				}
			}
		}
	}
}
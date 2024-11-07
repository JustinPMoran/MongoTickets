package coms309.Chats;

import java.io.IOException;
import java.util.*;

import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
import jakarta.transaction.Transactional;
import jakarta.websocket.*;
import jakarta.websocket.OnMessage;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/chat/{user_id}/{chat_id}")  // this is Websocket url
public class ChatSocket {

  // cannot autowire static directly (instead we do it by the below
  // method
	private static ChatLineRepository msgRepo;

	private static ChatRepository chatRepository;

	private static AccountRepository accountRepository;

	/*
   * Grabs the ChatLineRepository singleton from the Spring Application
   * Context.  This works because of the @Controller annotation on this
   * class and because the variable is declared as static.
   * There are other ways to set this. However, this approach is
   * easiest.
	 */
	@Autowired
	public void setChatLineRepository(ChatLineRepository repo) {
		msgRepo = repo;  // we are setting the static variable
	}

	@Autowired
	public void setChatRepository(ChatRepository repo) {
		chatRepository = repo;  // we are setting the static variable
	}

	@Autowired
	public void setAccountRepository(AccountRepository repo) {
		accountRepository = repo;  // we are setting the static variable
	}

	// Store all socket session and their corresponding username.
//	private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
//	private static Map<String, Session> usernameSessionMap = new Hashtable<>();
	private static final Map<Integer, Set<Session>> chatSessions = new HashMap<>();



	private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("user_id") int user_id, @PathParam("chat_id") int chat_id)
      throws IOException {

		// Add error handling for incorrect user id / chat id
		// Similar to Python get function. Creates a default hashset if key is not present
		chatSessions.computeIfAbsent(chat_id, k -> new HashSet<>()).add(session);

		logger.info("Entered into Open");

//		System.out.println(chatRepository.findById(chat_id).getLines());

		StringBuilder string = new StringBuilder();
		for (ChatLine line : chatRepository.findById(chat_id).getLines()){
			string.append(line).append("\n");
		}

//		session.getAsyncRemote().sendText(String.valueOf(string));

    // store connecting user information
//		sessionUsernameMap.put(session, user_id);
//		usernameSessionMap.put(user_id, session);

		//Send chat history to the newly connected user
//		sendChatLineToPArticularUser(username, getChatHistory());
		
    // broadcast that new user joined
//		String ChatLine = "User: " + username + " has Joined the Chat";
//		broadcast(ChatLine);
	}


	@OnMessage
	public void onMessage(Session session, @PathParam("user_id") int user_id, @PathParam("chat_id") int chatId, String message ) throws IOException {

		// Handle new ChatLines
		logger.info("Entered into ChatLine: Got ChatLine:" + message);

		Set<Session> sessions = chatSessions.get(chatId);
		if (sessions != null) {
			for (Session s : sessions) {
				if (s.isOpen() && !s.equals(session)) {
					s.getAsyncRemote().sendText(message);
				}
			}
		}

		Chat chat = chatRepository.findById(chatId);
		ChatLine line = new ChatLine(chat, accountRepository.findById(user_id), message);
		chat.addLine(line);
//		line.setCh
//		// Saving chat history to repository
		msgRepo.save(line);
	}


	@OnClose
	public void onClose(Session session, @PathParam("chat_id") int chatId) throws IOException {
		logger.info("Entered into Close");


		Set<Session> sessions = chatSessions.get(chatId);
		if (sessions != null) {
			sessions.remove(session);
			if (sessions.isEmpty()) {
				chatSessions.remove(chatId); // Clean up if no more sessions
			}
		}

		System.out.println("Connection closed");
	}


	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}


//	private void sendChatLineToPArticularUser(String username, String ChatLine) {
//		try {
//			usernameSessionMap.get(username).getBasicRemote().sendText(ChatLine);
//		}
//    catch (IOException e) {
//			logger.info("Exception: " + e.getChatLine().toString());
//			e.printStackTrace();
//		}
//	}
//
//
//	private void broadcast(String ChatLine) {
//		sessionUsernameMap.forEach((session, username) -> {
//			try {
//				session.getBasicRemote().sendText(ChatLine);
//			}
//      catch (IOException e) {
//				logger.info("Exception: " + e.getChatLine().toString());
//				e.printStackTrace();
//			}
//
//		});
//
//	}
	

  // Gets the Chat history from the repository
//	private String getChatHistory() {
//		List<ChatLine> ChatLines = msgRepo.findAll();
//
//    // convert the list to a string
//		StringBuilder sb = new StringBuilder();
//		if(ChatLines != null && ChatLines.size() != 0) {
//			for (ChatLine ChatLine : ChatLines) {
//				sb.append(ChatLine.getUserName() + ": " + ChatLine.getContent() + "\n");
//			}
//		}
//		return sb.toString();
//	}



} // end of Class
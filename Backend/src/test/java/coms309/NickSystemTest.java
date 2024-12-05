package coms309;

import coms309.Chats.Chat;
import coms309.Chats.ChatLine;
import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
import coms309.Chats.ChatLineRepository;
import coms309.Chats.ChatRepository;
import coms309.Friends.FriendshipRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.sampled.Line;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

// Create chat, get chat by id, add members, send message, get lines
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class NickSystemTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private ChatLineRepository msgRepo;


    private Account account1;
    private Account account2;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        account1 = new Account("user1", "pass","uniqueuser1@example.com");
        account2 = new Account("user2", "password","uniqueuser2@example.com");
        accountRepository.save(account1);
        accountRepository.save(account2);
    }

    @After
    public void tearDown() {
        friendshipRepository.deleteAll();
        accountRepository.deleteAll();
    }





    @Test
    public void getAllChatsTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/chats");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        assertTrue(response.getBody().asString().startsWith("["));
    }

    @Test
    public void testGetChatLines() {
        Response createChatResponse = RestAssured.given()
                .contentType("application/json")
                .post("/chats");

        assertEquals(200, createChatResponse.getStatusCode());
        String responseBody = createChatResponse.getBody().asString();
        assertTrue(responseBody.contains("Chat ID:"));

        int chatId = Integer.parseInt(responseBody.split("Chat ID: ")[1].trim());

        Chat chat = chatRepository.findById(chatId);
        Account acc = accountRepository.findById(1);

        ChatLine line1 = new ChatLine(chat, acc, "Hello, World!");
        ChatLine line2 = new ChatLine(chat, acc,"Testing chat lines!");
        chat.addLine(line1);
        chat.addLine(line2);
        chatRepository.save(chat);
        msgRepo.save(line1);
        msgRepo.save(line2);


        Response getLinesResponse = RestAssured.given()
                .contentType("application/json")
                .get("/chats/" + chatId + "/lines");

        assertEquals(200, getLinesResponse.getStatusCode());

        String responseString = getLinesResponse.getBody().asString();
        assertTrue(responseString.contains("Hello, World!"));
        assertTrue(responseString.contains("Testing chat lines!"));

    }



    @Test
    public void getChatByIdTest() {
        Chat chat = new Chat();
        chatRepository.save(chat);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/chats/" + chat.getId());

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        assertTrue(response.getBody().asString().contains(String.valueOf(chat.getId())));
    }

    @Test
    public void createChatTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .post("/chats");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        assertTrue(response.getBody().asString().contains("success"));
    }

    @Test
    public void addMemberToChatTest() {
        Chat chat = new Chat();
        chatRepository.save(chat);
        Account account = new Account();
        accountRepository.save(account);

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .put("/chats/add_member/" + chat.getId() + "/" + account.getId());

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String resp = response.getBody().asString();
        assertTrue(response.getBody().asString().contains("success"));

        chat = chatRepository.findById(chat.getId());
        assertTrue(!chat.getMembers().isEmpty());

        chatRepository.deleteById(chat.getId());
    }

    @Test
    public void testGetFriends() {
        Account acc = new Account();
        accountRepository.save(acc);
        Response response = RestAssured.given()
                .contentType("application/json")
                .get("/get_friendships/" + acc.getId());

        assertTrue(response.getBody().asString().startsWith("["));
    }
    @Test
    public void testSendFriendRequest_Success() {
        int senderId = account1.getId();
        int receiverId = account2.getId();
        Response response = RestAssured.given()
                .contentType("application/json")
                .queryParam("senderId", senderId)
                .queryParam("receiverId", receiverId)
                .post("/friendship/send-request");

        assertTrue(response.getBody().asString().contains("Friend request sent successfully"));
    }
    @Test
    public void testSendFriendRequest_ExistingFriendship() {

        int senderId = account1.getId();
        int receiverId = account2.getId();

        Response resp = RestAssured.given()
                .contentType("application/json")
                .queryParam("senderId", senderId)
                .queryParam("receiverId", receiverId)
                .post("/friendship/send-request");

        Response response = RestAssured.given()
                .contentType("application/json")
                .queryParam("senderId", senderId)
                .queryParam("receiverId", receiverId)
                .post("/friendship/send-request");

        System.out.println(response.getBody().asString());
        assertTrue(response.getBody().asString().contains("Friend request already exists or users are already friends"));
    }
    @Test
    public void testAcceptFriendRequest_Success() {

        // First, create a pending friendship between sender and receiver in the database

        List<Account> accounts = accountRepository.findAll();

        int senderId = accounts.get(0).getId();  // Sender ID
        int receiverId = accounts.get(1).getId(); // Receiver ID

        Response resp = RestAssured.given()
                .contentType("application/json")
                .queryParam("senderId", senderId)
                .queryParam("receiverId", receiverId)
                .post("/friendship/send-request");

        Response response = RestAssured.given()
                .contentType("application/json")
                .queryParam("senderId", senderId)
                .queryParam("receiverId", receiverId)
                .post("/friendship/accept-request");

        System.out.println(response.getBody().asString());
        System.out.println(resp.getBody().asString());
        assertTrue(response.getBody().asString().contains("Friend request accepted"));
    }
    @Test
    public void testRejectFriendRequest_Success() {
        int senderId = account1.getId();  // Sender ID
        int receiverId = account2.getId(); // Receiver ID

        // First, create a pending friendship between sender and receiver in the database

        Response resp = RestAssured.given()
                .contentType("application/json")
                .queryParam("senderId", senderId)
                .queryParam("receiverId", receiverId)
                .post("/friendship/send-request");

        Response response = RestAssured.given()
                .contentType("application/json")
                .queryParam("senderId", senderId)
                .queryParam("receiverId", receiverId)
                .post("/friendship/reject-request");

        assertTrue(response.getBody().asString().contains("Friend request rejected"));
    }
    @Test
    public void testRemoveFriend_Success() {
        int accountId1 = account1.getId();
        int accountId2 = account2.getId();


        Response resp = RestAssured.given()
                .contentType("application/json")
                .queryParam("senderId", accountId1)
                .queryParam("receiverId", accountId2)
                .post("/friendship/send-request");

        Response resp1 = RestAssured.given()
                .contentType("application/json")
                .queryParam("senderId", accountId1)
                .queryParam("receiverId", accountId2)
                .post("/friendship/accept-request");

        Response response = RestAssured.given()
                .contentType("application/json")
                .queryParam("accountId1", accountId1)
                .queryParam("accountId2", accountId2)
                .delete("/friendship/remove");

//        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("Friend removed successfully"));
    }










}

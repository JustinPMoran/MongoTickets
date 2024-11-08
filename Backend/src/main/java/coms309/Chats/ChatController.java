package coms309.Chats;

import com.sun.net.httpserver.Authenticator;
import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author Pablo Leguizamo
 *
 */

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ChatController {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ChatRepository chatRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/chats")
    List<Chat> getAllChats(){
        return chatRepository.findAll();
    }

    @GetMapping(path = "/chats/{chat_id}/lines")
    List<ChatLine> getChatLines(@PathVariable int chat_id){
        Chat chat = chatRepository.findById(chat_id);
        System.out.println(chat.getLines());
        return chat.getLines();
    }

    @GetMapping(path = "/chats/{chat_id}")
    Chat getChatbyID(@PathVariable int chat_id){
        return chatRepository.findById(chat_id);
    }

    @PostMapping(path="/chats")
    String createChat(){
        chatRepository.save(new Chat());


        return success + " Chat ID: " + chatRepository.findTopByOrderByIdDesc().getId();
    }

    @PutMapping(path="/chats/add_member/{chat_id}/{account_id}")
    String addMember(@PathVariable int chat_id, @PathVariable int account_id){
        Chat chat = chatRepository.findById(chat_id);
        Account acc = accountRepository.findById(account_id);
        if(chat == null) {return failure;}
        if(acc == null) {return failure;}
        chat.addMember(acc);
        acc.addChat(chat);
        chatRepository.save(chat);

        return success;
    }

}

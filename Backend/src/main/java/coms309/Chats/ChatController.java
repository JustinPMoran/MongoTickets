package coms309.Chats;

import com.sun.net.httpserver.Authenticator;
import coms309.Accounts.Account;
import coms309.Accounts.AccountRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all chats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all chats",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class)) })
    })
    @GetMapping(path = "/chats")
    List<Chat> getAllChats(){
        return chatRepository.findAll();
    }


    @Operation(summary = "Get all lines in a chat by chat ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the chat lines",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChatLine.class)) }),
            @ApiResponse(responseCode = "404", description = "Chat not found",
                    content = @Content)
    })
    @GetMapping(path = "/chats/{chat_id}/lines")
    List<ChatLine> getChatLines(@PathVariable int chat_id){
        Chat chat = chatRepository.findById(chat_id);
        System.out.println(chat.getLines());
        return chat.getLines();
    }

    @Operation(summary = "Get a chat by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the chat",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class)) }),
            @ApiResponse(responseCode = "404", description = "Chat not found",
                    content = @Content)
    })
    @GetMapping(path = "/chats/{chat_id}")
    Chat getChatbyID(@PathVariable int chat_id){
        return chatRepository.findById(chat_id);
    }

    @Operation(summary = "Create a new chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the chat",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class)) }),
            @ApiResponse(responseCode = "400", description = "Failed to create chat",
                    content = @Content)
    })
    @PostMapping(path="/chats")
    String createChat(){
        chatRepository.save(new Chat());


        return success + " Chat ID: " + chatRepository.findTopByOrderByIdDesc().getId();
    }

    @Operation(summary = "Add a member to a chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Added member to chat",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Chat.class)) }),
            @ApiResponse(responseCode = "404", description = "Chat or account not found",
                    content = @Content)
    })
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

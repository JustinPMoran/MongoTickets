package coms309;

import coms309.Accounts.Account;
import coms309.Chats.Chat;
import coms309.Chats.ChatRepository;
import coms309.Tickets.Ticket;
import coms309.Tickets.TicketRepository;
import coms309.Accounts.AccountRepository;
import coms309.LiveTickets.LiveRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 *
 * @author Pablo Leguizamo
 *
 */

@SpringBootApplication
class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Create 3 users with their machines
    /**
     *
     * @param accountRepository repository for the User entity
     * @param ticketRepository repository for the Ticket entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Ticket object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(AccountRepository accountRepository, TicketRepository ticketRepository, ChatRepository chatRepository) {
        return args -> {
            Account account1 = new Account("Anoop", "pass", "anoop@iastate.edu");

            Ticket ticket1 = new Ticket( "G7", "8", 24.99 , true);
            Ticket ticket2 = new Ticket( "G6", "9", 25.99 , true);
//            Ticket ticket2 = new Ticket("Volleyball", "Sep 24", 15.99, "A2", 3);
//            Ticket ticket3 = new Ticket("Basketball", "Oct 15", 34.99, "A11",1);
//            account1.addTicket(ticket1);
//            account1.addTicket(ticket2);

//            Chat chat = chatRepository.findById(1);

//            chat.addMember(accountRepository.findById(5));
//            chatRepository.save(chat);

//            Chat chat = new Chat();

//
//            chat.addLine();
//            user2.setTicket(ticket2);
//            user3.setTicket(ticket3);
//            accountRepository.save(account1);
//            userRepository.save(user2);
//            userRepository.save(user3);


        };
    }


}

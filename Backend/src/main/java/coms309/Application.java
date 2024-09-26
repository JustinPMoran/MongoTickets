package coms309;

import coms309.Tickets.Ticket;
import coms309.Tickets.TicketRepository;
import coms309.Users.User;
import coms309.Users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author Pablo Leguizamo
 *
 */

//@SpringBootApplication
class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Create 3 users with their machines
    /**
     *
     * @param userRepository repository for the User entity
     * @param ticketRepository repository for the Ticket entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Ticket object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, TicketRepository ticketRepository) {
        return args -> {
            User user1 = new User("Anoop", "anoop@iastate.edu");
            User user2 = new User("Nick", "nick@iastate.edu");
            User user3 = new User("Justin", "justin@iastate.edu");
            Ticket ticket1 = new Ticket("Football", "Sep 23", 24.99, "G7", 8);
            Ticket ticket2 = new Ticket("Volleyball", "Sep 24", 15.99, "A2", 3);
            Ticket ticket3 = new Ticket("Basketball", "Oct 15", 34.99, "A11",1);
            user1.setTicket(ticket1);
            user2.setTicket(ticket2);
            user3.setTicket(ticket3);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);


        };
    }


}

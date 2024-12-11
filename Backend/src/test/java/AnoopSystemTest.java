import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnoopSystemTest {


    @LocalServerPort
    int port = 8080;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testCreateEvent() {
        JSONObject event = new JSONObject();
        try {
            event.put("name", "Test Event");
            event.put("date", "2024-12-25");
            event.put("location", "Test Location");
            event.put("description", "This is a test event.");
            event.put("max_capacity", "100");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(event.toString())
                .when()
                .post("/events");

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("success"));
    }

    @Test
    public void testGetAllEvents() {
        Response response = RestAssured.given()
                .when()
                .get("/events");

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("id"));
        System.out.println(response.getBody().asString());
    }

    @Test
    public void testGetEventById() {
        int eventId = 2;

        Response response = RestAssured.given()
                .when()
                .get("/events/" + eventId);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("id"));
    }

    @Test
    public void testUpdateEvent() {
        int eventId = 2;

        JSONObject updatedEvent = new JSONObject();
        try {
            updatedEvent.put("id", eventId);
            updatedEvent.put("name", "Updated Event");
            updatedEvent.put("date", "2024-12-31");
            updatedEvent.put("location", "Updated Location");
            updatedEvent.put("description", "Updated description.");
            updatedEvent.put("max_capacity", "200");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updatedEvent.toString())
                .when()
                .put("/events/" + eventId);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("success"));
        System.out.println(response.getBody().asString());
    }

    @Test
    public void testDeleteEvent() {
        int eventId = 1;

        Response response = RestAssured.given()
                .when()
                .delete("/events/" + eventId);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("success"));
    }

////////////////////////////////////////////////TEST TICKETS////////////////////////////////////////////////////////////
    int ticketId = 43;


    @Test
    public void testCreateTicket() {
        JSONObject ticket = new JSONObject();
        try {
            ticket.put("price", 50.0);
            ticket.put("row", "A");
            ticket.put("section", "VIP");
            ticket.put("is_active", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response ticketResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(ticket.toString())
                .when()
                .post("/tickets/" + 2);

        assertEquals(200, ticketResponse.getStatusCode());
        assertTrue(ticketResponse.getBody().asString().contains("success"));
    }


    @Test
    public void testGetAllTickets() {
        Response response = RestAssured.given()
                .when()
                .get("/tickets");

        assertEquals(200, response.getStatusCode());
        System.out.println(response.getBody().asString());
    }

    @Test
    public void testUpdateTicket() {

        JSONObject updatedTicket = new JSONObject();
        try {
            updatedTicket.put("price", 60.0);
            updatedTicket.put("row", "B");
            updatedTicket.put("section", "Standard");
            updatedTicket.put("is_active", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updatedTicket.toString())
                .when()
                .put("/tickets/" + ticketId);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("id"));
        System.out.println(response.getBody().asString());
    }

    @Test
    public void testDeleteTicket() {
        Response response = RestAssured.given()
                .when()
                .delete("/tickets/" + ticketId);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("success"));
    }

    @Test
    public void testAssignTicketToAccount() {
        int accountId = 1; // Use an ID of an existing account
        Response response = RestAssured.given()
                .queryParam("ticketId", ticketId)
                .queryParam("accountId", accountId)
                .when()
                .put("/tickets/assign");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testAssignTicketToEvent() {
        int eventId = 2;
        Response response = RestAssured.given()
                .queryParam("ticketId", ticketId)
                .queryParam("eventId", eventId)
                .when()
                .put("/ticket/assign_to_event/" + eventId + "/" + ticketId);

        assertEquals(200, response.getStatusCode());
    }


}

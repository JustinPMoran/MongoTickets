package coms309;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import coms309.Events.Event;

import java.io.IOException;

public class ISUEventScraper {
    public static void main(String[] args) {
        String url = "https://www.mu.iastate.edu/events";

        System.out.println("Debug started");
        try {

            Document doc = Jsoup.connect(url).get();
            Elements events = doc.select(".fc-list-table ");
            System.out.println("Events found: " + events.size());





            for (Element event : events) {

                System.out.println("in for loop");


                String title = event.select(".event-listing__title").text();
                String date = event.select(".event-listing__date").text();
                String location = event.select(".event-listing__location").text();
                String description = event.select(".event-listing__desc").text();



                Event e = new Event(title, date, location, description, "250");
                System.out.println("Title: " + title);
                System.out.println("Date: " + date);
                System.out.println("Location: " + location);
                System.out.println("Description: " + description);
                System.out.println("--------------------\n\n e\n\n");



                System.out.println(e.getName() + e.getDate() + e.getLocation() + e.getDescription() + e.getMax_capacity());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
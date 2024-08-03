// package PackageOne;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EventManager {
    private static LinkedList<Event> eventList = new LinkedList<>();
    private static BinarySearchTree eventBST = new BinarySearchTree();
    private static Queue<Event> eventQueue = new LinkedList<>();
    private static PriorityQueue<Event> eventPriorityQueue = new PriorityQueue<>(Comparator.comparing(Event::getDateTime));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command;

        while (true) {
            System.out.println("Enter a command (add,display, remove, update, list, findbydate, save, load, checkconflicts, sort, notify, exit):");
            command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "add":
                    addEvent(scanner);
                    break;
                case "display":
                    displayEvent(scanner);
                    break;
                case "remove":
                    removeEvent(scanner);
                    break;
                case "update":
                    updateEvent(scanner);
                    break;
                case "list":
                    listAllEvents();
                    break;
                case "findbydate":
                    findEventByDate(scanner);
                    break;
                    //other developers developing functions
                // case "save":
                //     saveEventsToFile(scanner);
                //     break;
                // case "load":
                //     loadEventsFromFile(scanner);
                //     break;
                // case "checkconflicts":
                //     checkEventConflicts();
                //     break;
                // case "sort":
                //     sortEventsByDate();
                //     break;
                // case "notify":
                //     notifyUpcomingEvents();
                //     break;
                // case "exit":
                //     System.out.println("Exiting the program.");
                //     return;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }

    private static void addEvent(Scanner scanner) {
        System.out.println("Enter event ID:");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter event name:");
        String name = scanner.nextLine();
        
        System.out.println("Enter event date and time (yyyy-MM-dd HH:mm):");
        String dateStr = scanner.nextLine();
        
        System.out.println("Enter event description:");
        String description = scanner.nextLine();

        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            Event event = new Event(id, name, dateTime, description);
            eventList.add(event);
            eventBST.insert(event);
            eventQueue.add(event);
            eventPriorityQueue.add(event);
            System.out.println("Event added successfully!");
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm.");
        }
    }
    private static void displayEvent(Scanner scanner) {
        // System.out.println("Enter event ID:");
        // int id = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter event ID to display:");
        int id = Integer.parseInt(scanner.nextLine());

        Event event = eventBST.findById(id);
        if (event != null) {
            System.out.println(event);
            System.out.println("Event displayed successfully!");
        } else {
            System.out.println("Event not found.");
        }
    }
    private static void removeEvent(Scanner scanner) {
        System.out.println("Enter event ID to remove:");
        int id = Integer.parseInt(scanner.nextLine());

        boolean removed = eventList.removeIf(e -> e.getId() == id);
        if (removed) {
            eventBST.removeById(id);
            eventQueue.removeIf(e -> e.getId() == id);
            eventPriorityQueue.removeIf(e -> e.getId() == id);
            System.out.println("Event removed successfully!");
        } else {
            System.out.println("Event not found.");
        }
    }

    private static void updateEvent(Scanner scanner) {
        System.out.println("Enter event ID to update:");
        int id = Integer.parseInt(scanner.nextLine());

        Event event = eventList.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
        if (event != null) {
            System.out.println("Enter new event name:");
            String name = scanner.nextLine();
            System.out.println("Enter new event date and time (yyyy-MM-dd HH:mm):");
            String dateStr = scanner.nextLine();
            System.out.println("Enter new event description:");
            String description = scanner.nextLine();

            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                event.setName(name);
                event.setDateTime(dateTime);
                event.setDescription(description);
                System.out.println("Event updated successfully!");
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm.");
            }
        } else {
            System.out.println("Event not found.");
        }
    }

    private static void listAllEvents() {
        if (eventList.isEmpty()) {
            System.out.println("No events to list.");
        } else {
            eventList.forEach(System.out::println);
            System.out.println("All events listed successfully!");
        }
    }

    private static void findEventByDate(Scanner scanner) {
        System.out.println("Enter date to find events (yyyy-MM-dd):");
        String dateStr = scanner.nextLine();
        
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<Event> events = eventBST.findEventsByDate(date);
            if (events.isEmpty()) {
                System.out.println("No events found on this date.");
            } else {
                events.forEach(System.out::println);
                System.out.println("Events found successfully!");
            }
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }


    // Inner class Event
    static class Event implements Serializable {


       public Event(int id, String name, LocalDateTime dateTime, String description) {

        }

        public int getId() {
        
        }

        public void setName(String name) {
        
        }

        public void setDateTime(LocalDateTime dateTime) {
        
        }

        public void setDescription(String description) {
        
        }

    }

    // Binary Search Tree (BST) for Event
    static class BinarySearchTree {
        private class Node {
            Event event;
            Node left;
            Node right;

            Node(Event event) {
                this.event = event;
            }
        }

        private Node root;



 

        public boolean isEmpty() {
            return root == null;
        }

       public List<Event> findEventsByDate(LocalDate date) {

        }


   

        private Node removeRec(Node root, int id) {


            return root;
        }


    }
}

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
                // Scanner scanner = new Scanner(System.in);
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
                case "save":
                    saveEventsToFile(scanner);
                    break;
                case "load":
                    loadEventsFromFile(scanner);
                    break;
                case "checkconflicts":
                    checkEventConflicts();
                    break;
                case "sort":
                    sortEventsByDate();
                    break;
                
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

    private static void saveEventsToFile(Scanner scanner) {
        System.out.println("Enter file name to save events:");
        String fileName = scanner.nextLine();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(eventList);
            System.out.println("Events saved to file successfully!");
        } catch (IOException e) {
            System.out.println("Error saving events to file: " + e.getMessage());
        }
    }

    private static void loadEventsFromFile(Scanner scanner) {
        System.out.println("Enter file name to load events:");
        String fileName = scanner.nextLine();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            eventList = (LinkedList<Event>) ois.readObject();
            eventBST = new BinarySearchTree();
            eventQueue = new LinkedList<>();
            eventPriorityQueue = new PriorityQueue<>(Comparator.comparing(Event::getDateTime));
            for (Event event : eventList) {
                eventBST.insert(event);
                eventQueue.add(event);
                eventPriorityQueue.add(event);
            }
            System.out.println("Events loaded from file successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading events from file: " + e.getMessage());
        }
    }

    private static void checkEventConflicts() {
        Event previousEvent = null;
        Queue<Event> tempQueue = new LinkedList<>(eventQueue);
        while (!tempQueue.isEmpty()) {
            Event currentEvent = tempQueue.poll();
            if (previousEvent != null && currentEvent.getDateTime().isBefore(previousEvent.getDateTime().plusHours(1))) {
                System.out.println("Conflict found: " + previousEvent + " and " + currentEvent);
            }
            previousEvent = currentEvent;
        }
    }

    private static void sortEventsByDate() {
        PriorityQueue<Event> tempQueue = new PriorityQueue<>(eventPriorityQueue);
        while (!tempQueue.isEmpty()) {
            Event event = tempQueue.poll();
            System.out.println(event);
        }
    }

    private static void notifyUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        eventQueue.stream().filter(e -> e.getDateTime().isAfter(now) && e.getDateTime().isBefore(now.plusHours(1)))
                .forEach(e -> System.out.println("Upcoming event: " + e));
    }

    // Inner class Event
    static class Event implements Serializable {
        private int id;
        private String name;
        private LocalDateTime dateTime;
        private String description;

        public Event(int id, String name, LocalDateTime dateTime, String description) {
            this.id = id;
            this.name = name;
            this.dateTime = dateTime;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return "Event ID: " + id + ", Name: " + name + ", DateTime: " + dateTime.format(formatter) + ", Description: " + description;
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

        public void insert(Event event) {
            root = insertRec(root, event);
        }

        private Node insertRec(Node root, Event event) {
            if (root == null) {
                root = new Node(event);
                return root;
            }

            if (event.getDateTime().isBefore(root.event.getDateTime()))
            if (event.getDateTime().isBefore(root.event.getDateTime())) {
                root.left = insertRec(root.left, event);
            } else if (event.getDateTime().isAfter(root.event.getDateTime())) {
                root.right = insertRec(root.right, event);
            }

            return root;
        }

        public boolean isEmpty() {
            return root == null;
        }

        public List<Event> findEventsByDate(LocalDate date) {
            List<Event> events = new ArrayList<>();
            findByDateRec(root, date, events);
            return events;
        }

        private void findByDateRec(Node root, LocalDate date, List<Event> events) {
            if (root != null) {
                findByDateRec(root.left, date, events);
                if (root.event.getDateTime().toLocalDate().equals(date)) {
                    events.add(root.event);
                }
                findByDateRec(root.right, date, events);
            }
        }

        public void inOrderTraversal() {
            inOrderRec(root);
        }

        private void inOrderRec(Node root) {
            if (root != null) {
                inOrderRec(root.left);
                System.out.println(root.event);
                inOrderRec(root.right);
            }
        }

        public boolean removeById(int id) {
            return removeRec(root, id) != null;
        }

        private Node removeRec(Node root, int id) {
            if (root == null) {
                return null;
            }

            if (id < root.event.getId()) {
                root.left = removeRec(root.left, id);
            } else if (id > root.event.getId()) {
                root.right = removeRec(root.right, id);
            } else {
                if (root.left == null) {
                    return root.right;
                } else if (root.right == null) {
                    return root.left;
                }

                root.event = findMin(root.right).event;
                root.right = removeRec(root.right, root.event.getId());
            }

            return root;
        }

        private Node findMin(Node root) {
            while (root.left != null) {
                root = root.left;
            }
            return root;
        }

        public Event findById(int id) {
            return findRec(root, id);
        }

        private Event findRec(Node root, int id) {
            if (root == null || root.event.getId() == id) {
                return root != null ? root.event : null;
            }

            if (id < root.event.getId()) {
                return findRec(root.left, id);
            } else {
                return findRec(root.right, id);
            }
        }
    }
}

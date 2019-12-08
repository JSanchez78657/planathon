import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
public class Menu {

    public Menu(){
        Scanner input = new Scanner(System.in);

        int selection;
        do {
            System.out.println("Main Menu:");
            System.out.println("1) Add Event");
            System.out.println("2) Add Person");
            System.out.println("3) View Events");
            System.out.println("4) View People");
            System.out.println("0) Exit");
            selection = input.nextInt();
            switch(selection){
                case 1:
                    add_event_menu();
                    break;
                case 2:
                    add_person_menu();
                    break;
                case 3:
                    view_events_menu();
                    break;
                case 4:
                    view_people_menu();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("ERROR.");
            }
        }while(selection != 0);
        input.close();
    }

    public void add_event_menu() {
        Scanner input = new Scanner(System.in);
        int year, month, day, hour, minute;
        System.out.println("Event Name: ");
        String event_name = input.nextLine();
        System.out.println("Event Date and Time:");
        System.out.println("Year: ");
        year = input.nextInt();
        System.out.println("Month: ");
        month = input.nextInt();
        System.out.println("Day: ");
        day = input.nextInt();
        System.out.println("Hour: ");
        hour = input.nextInt();
        System.out.println("Minute: ");
        minute = input.nextInt();
        System.out.println();
        LocalDateTime date = LocalDateTime.of(year, month, day, hour, minute);
        System.out.println("Event Location: ");
        String location = input.nextLine();
        Database db = new Database("Cooper");
        int selection;
        do{
            System.out.println("Add Attendee");
            ResourceIterator<Node> people = db.getPeople();
            int i = 1;
            ArrayList<Node> people_table = new ArrayList<Node>();
            while(people.hasNext())
            {
                people_table.add(people.next());
                System.out.println(i + ") " + people.next().getProperty(Database.NAME));
                i++;
            }
            System.out.println("0) Exit");
            switch(selection) {
                case 0:
                    break;
                default:
                    //add people_table[selection] to party
            }
        }while(selection != 0);

    }

    public void add_person_menu() {

    }

    public void view_events_menu() {

    }

    public void view_people_menu() {

    }
}

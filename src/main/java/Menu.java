import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;
public class Menu {

    private Database db;

    public Menu(Database db)
    {
        this.db = db;
        main_menu();
    }

    public void main_menu()
    {
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

    public void add_event_menu()
    {
        Scanner input_string = new Scanner(System.in);
        Scanner input = new Scanner(System.in);
        int year, month, day, hour, minute;
        System.out.println("Event Name: ");
        String event_name = input_string.nextLine();
        System.out.println("Event Location");
        String event_location = input_string.nextLine();
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
        try
        {
            LocalDateTime date = LocalDateTime.of(year, month, day, hour, minute);
            db.addEvent(event_name, date, event_location);
        }
        catch (DateTimeException d) { System.out.println("Invalid date."); }

    }

    public void add_person_menu() {

    }

    public void view_events_menu() {

    }

    public void view_people_menu() {

    }
}

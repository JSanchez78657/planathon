import org.neo4j.graphdb.RelationshipType;

import java.util.*;
import java.time.ZonedDateTime;
public class Database {
    private enum Relationships implements RelationshipType {// implements RelationshipType {
        ATTENDING,
        NEEDED,
        BRINGING
    }

    public Dictionary data = new Hashtable();
    public void addEvent(String event_name, ZonedDateTime date, String Location){}
    public void deleteEvent(){}
    public Dictionary getEvent(){
        return data;
    }
    public void addPerson(){}
    public void deletePerson(){}
    public Dictionary getPerson(){
        return data;
    }
    public void addSupply(){}
    public void deleteSupply(){}
    public Dictionary getSupply(){
        return data;
    }
}

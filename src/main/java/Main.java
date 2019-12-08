import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.time.ZonedDateTime;

public class Main {

    public static void main(String[] args)
    {
        Database db = new Database("TestServer");
        Node kevin = db.addPerson("Kevin");
        Node nachos = db.addSupply("Nachos", 2);
        Node hackathon = db.addEvent("Hack a Thon", ZonedDateTime.now(), "ARC");
        db.addRelationship(kevin, nachos, Database.RelationshipTypes.BRINGING);
        db.addRelationship(kevin, hackathon, Database.RelationshipTypes.ATTENDING);
        db.getSupplies(kevin);
        db.shutdownServer();
    }
}

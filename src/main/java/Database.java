import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.Dictionary;
import java.util.Hashtable;
public class Database {

    private GraphDatabaseService db;
    private File dbFile;

    public final String NAME = "name";
    public final String DATE = "date";
    public final String LOCATION = "location";
    public final String QUANTITY = "quantity";

    private final Label EVENT = Label.label("event");
    private final Label PERSON = Label.label("person");
    private final Label SUPPLY = Label.label("supply");
    private final Label RELATIONSHIP = Label.label("relationship");

    public enum RelationshipTypes implements RelationshipType
    {
        ATTENDING,
        NEEDED,
        BRINGING;
    }

    public Database(String filename) {
        dbFile = new File("databases/" + filename);
        db = new GraphDatabaseFactory().newEmbeddedDatabase(dbFile);
    }

    public Dictionary data = new Hashtable();

    public Node addEvent(String event_name, ZonedDateTime date, String location)
    {
        Node node;
        try (Transaction tx = db.beginTx())
        {
            node = db.createNode(EVENT);
            node.setProperty(NAME, event_name);
            node.setProperty(DATE, date);
            node.setProperty(LOCATION, location);
            tx.success();
        }
        catch(TransactionFailureException t) { return null; }
        return node;
    }

    public ResourceIterator<Node> getEvent(String name) {
        ResourceIterator<Node> nodes;
        try (Transaction tx = db.beginTx())
        {
            nodes = db.findNodes(EVENT, NAME, name);
            tx.success();
        }
        catch(TransactionFailureException t) { return null; }
        return nodes;
    }

    public Dictionary getEvent()
    {
        return data;
    }

    public Node addPerson(String name)
    {
        Node node;
        try (Transaction tx = db.beginTx())
        {
            node = db.createNode(PERSON);
            node.setProperty(NAME, name);
            tx.success();
        }
        catch(TransactionFailureException t) { return null; }
        return node;
    }

    public ResourceIterator<Node> getPerson(String name)
    {
        ResourceIterator<Node> nodes;
        try (Transaction tx = db.beginTx())
        {
            nodes = db.findNodes(EVENT, NAME, name);
            tx.success();
        }
        catch(TransactionFailureException t) { return null; }
        return nodes;
    }

    public Node addSupply(String name, int quantity)
    {
        Node node;
        try (Transaction tx = db.beginTx())
        {
            node = db.createNode(SUPPLY);
            node.setProperty(NAME, name);
            node.setProperty(QUANTITY, quantity);
            tx.success();
        }
        catch(TransactionFailureException t) { return null; }
        return node;
    }

    public ResourceIterator<Node> getSupplies(Node node)
    {
        Node hold;
        try (Transaction tx = db.beginTx())
        {
            String label = getLabel(node);
            Result result = db.execute(
                    "WHERE " +
                    CypherQueries.LabelToNameRelationship(
                            label,
                            node.getProperty(NAME).toString(),
                            (label.equals(EVENT.toString())) ? EVENT.toString() : PERSON.toString()) +
                    "RETURN *");
            while(result.hasNext())
            {
                System.out.println(result.next());
            }
            tx.success();
        }
        catch(TransactionFailureException | QueryExecutionException t) { return null; }
        return null;
    }

    public Relationship addRelationship(Node a, Node b, RelationshipType type)
    {
        Relationship relationship;
        try (Transaction tx = db.beginTx())
        {
            relationship = a.createRelationshipTo(b, type);
            tx.success();
        }
        catch(TransactionFailureException t) { return null; }
        return relationship;
    }

    public void printNode(Node node)
    {
        System.out.println(node.getId() + ":");
        for (Label s : node.getLabels()) System.out.print(s + " ");
        System.out.println();
        for (String s : node.getPropertyKeys()) System.out.print(node.getProperty(s) + " ");
        System.out.println();
    }

    public String getLabel(Node node) {
        return node.getLabels().iterator().next().name();
    }

    public void printNodes()
    {
        ResourceIterable<Node> nodes;
        try (Transaction tx = db.beginTx())
        {
            nodes = db.getAllNodes();
            for(Node node : nodes)
            {
                printNode(node);
                System.out.println();
            }
            tx.success();
        }
        catch(TransactionFailureException t) { return; }
    }

    public void shutdownServer()
    {
        db.shutdown();
    }
}

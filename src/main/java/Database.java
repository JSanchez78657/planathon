import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.factory.GraphDatabaseFacade;

import javax.management.relation.Relation;
import javax.xml.crypto.Data;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.time.ZonedDateTime;
public class Database {

    private GraphDatabaseService db;
    private File dbFile;

    public static final String NAME = "name";
    public static final String DATE = "date";
    public static final String LOCATION = "location";
    public static final String QUANTITY = "quantity";

    public static final Label EVENT = Label.label("event");
    public static final Label PERSON = Label.label("person");
    public static final Label SUPPLY = Label.label("supply");
    public static final Label RELATIONSHIP = Label.label("relationship");

    public enum RelationshipTypes implements RelationshipType
    {
        ATTENDING,
        NEEDED,
        BRINGING
    }

    public Database(String filename) {
        dbFile = new File("databases/" + filename);
        db = new GraphDatabaseFactory().newEmbeddedDatabase(dbFile);
    }

    public Node addEvent(String event_name, LocalDateTime date, String location)
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
                            (label.equals(EVENT.toString())) ? RelationshipTypes.NEEDED.toString() : RelationshipTypes.BRINGING.toString()) +
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

    public ResourceIterator<Node> getEvents(Node node)
    {
        Node hold;
        try (Transaction tx = db.beginTx())
        {
            String label = getLabel(node);
            Result result = db.execute(
                    "MATCH (n:person)-[:ATTENDING]->(m:event)\n" +
                            "WHERE n.name = '" + node.getProperty(Database.NAME) + "'");
            while(result.hasNext())
            {
                System.out.println(result.next());
            }
            tx.success();
        }
        catch(TransactionFailureException | QueryExecutionException t) { return null; }
        return null;
    }

    public ResourceIterator<Node> getPeople(Node node)
    {
        Node hold;
        try (Transaction tx = db.beginTx())
        {
            String label = getLabel(node);
            Result result = db.execute(
                    "MATCH (n:event)-[:ATTENDING]->(m:person)\n" +
                            "WHERE n.name = '" + node.getProperty(Database.NAME) + "'");
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

    public Iterator<Node> getAllLabel(Label label)
    {
        Iterator<Node> nodes;
        try (Transaction tx = db.beginTx())
        {
            nodes = db.execute("MATCH (n:" + label + ") RETURN *").columnAs("n");
            tx.success();
        }
        catch(TransactionFailureException t) { return null; }
        return nodes;
    }



    public void printNode(Node node)
    {
        String label = getLabel(node);
        switch(getLabel(node))
        {
        }
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

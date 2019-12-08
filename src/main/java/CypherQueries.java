public class CypherQueries
{
    static String LabelToNameRelationship(String label, String name, String relationship)
    {
        return "(n" + label + ")-[:" + relationship + "]->(m {name: " + name + "})";
    }
}

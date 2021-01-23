import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;

public class OntologyDAO {

    public int getPlatAmount() {
        Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);
        String sQuery =
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "prefix menu: <http://www.univ-rouen.fr/ontologies/restaurant/carte/menu> " +
                        "SELECT ?label " +
                        "WHERE { VALUES ?famille { " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/plats> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/desserts> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/entrées> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/fromages> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/boissons> " +
                        "} " +
                        "?plat rdf:type ?famille. " +
                        "?plat rdfs:label ?label " +
                        "FILTER(LANGMATCHES(LANG(?label), \"fr\")) " +
                        "} ";
//        System.out.println(sQuery);
        Query query = QueryFactory.create(sQuery);
        QueryExecution qexec = QueryExecutionFactory.create(query, ontology);
        int toRet = 0;
        try {
            ResultSet rs = qexec.execSelect();

            while (rs.hasNext()){
                ++toRet;
                rs.next();
            }

        } finally {

            qexec.close();
        }

        return toRet;
    }
}

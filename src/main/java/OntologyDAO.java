import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

public class OntologyDAO {

    public int getPlatAmount() {
        Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);
        String sQuery =
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "prefix menu: <http://www.univ-rouen.fr/ontologies/restaurant/carte/menu> " +
                        "SELECT ?plat " +
                        "WHERE { VALUES ?famille { " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/plats> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/desserts> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/entrées> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/fromages> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/boissons> " +
                        "} " +
                        "?plat rdf:type ?famille. " +
                        "} ";
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

    public void getAllPlat() {
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
        Query query = QueryFactory.create(sQuery);
        QueryExecution qexec = QueryExecutionFactory.create(query, ontology);
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {

                QuerySolution solution = rs.next();
                String label = solution.getLiteral("label").getString();
                System.out.println(label);


            }
        } finally {
            qexec.close();
        }
        return;
    }

    public void updatePrice(int price, String label) {
        Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);
        String sQuery =
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "prefix carte: <http://www.univ-rouen.fr/ontologies/restaurant/carte> " +
                        "DELETE { ?plat  carte:prixDeVenteTtc ?prix } " +
                        "INSERT { ?plat carte:prixDeVenteTtc '" + price + "' } " +
                        "WHERE { VALUES ?famille { " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/plats> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/desserts> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/entrées> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/fromages> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/boissons> " +
                        "} " +
                        "?plat rdf:type ?famille. " +
                        "?plat rdfs:label '" + label + "' " +
                        "FILTER(LANGMATCHES(LANG(?label), \"fr\")) " +
                        "} ";
            UpdateRequest request = UpdateFactory.create(sQuery);

            UpdateAction.execute(request, ontology);

        String sQuery2 =
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "prefix menu: <http://www.univ-rouen.fr/ontologies/restaurant/carte/menu> " +
                        "prefix carte: <http://www.univ-rouen.fr/ontologies/restaurant/carte> " +
                        "SELECT ?price " +
                        "WHERE { " +
                        "?plat carte:prixDeVenteTtc ?price. " +
                        "?plat rdfs:label '" + label + "'. " +
                        "FILTER(LANGMATCHES(LANG(?label), \"fr\")) " +
                        "} ";
        System.out.println(sQuery2);
        Query query = QueryFactory.create(sQuery2);
        QueryExecution qexec = QueryExecutionFactory.create(query, ontology);
        try {
            ResultSet rs = qexec.execSelect();
            ResultSetFormatter.outputAsJSON(rs);
            while (rs.hasNext()) {
                System.out.println("while");
                QuerySolution solution = rs.next();
                String price_result = solution.getLiteral("price").getString();
                System.out.println(price_result);


            }
            System.out.println("no while");
        } finally {
            qexec.close();
        }
        return;
    }

    public void getAllVegetarianPlat() {
        Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);
        String sQuery =
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "prefix recette: <http://www.univ-rouen.fr/ontologies/restaurant/carte/recette> " +

        "INSERT DATA { " +
    "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/desserts/fondant-au-chocolat> recette:a-la-specificite <http://www.univ-rouen.fr/ontologies/restaurant/specificite/vegan>. " +
    "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/desserts/tarte-tatin> recette:a-la-specificite <http://www.univ-rouen.fr/ontologies/restaurant/specificite/vegan>. " +
    "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/desserts/cafe-gourmand> recette:a-la-specificite <http://www.univ-rouen.fr/ontologies/restaurant/specificite/vegan>. " +
    "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/desserts/sabayon> recette:a-la-specificite <http://www.univ-rouen.fr/ontologies/restaurant/specificite/vegan>. " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/desserts/sorbet-citron> recette:a-la-specificite <http://www.univ-rouen.fr/ontologies/restaurant/specificite/vegan> " +
                        "}";

        UpdateRequest request = UpdateFactory.create(sQuery);

        UpdateAction.execute(request, ontology);

        String sQuery2 =
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "prefix menu: <http://www.univ-rouen.fr/ontologies/restaurant/carte/menu> " +
                        "prefix recette: <http://www.univ-rouen.fr/ontologies/restaurant/carte/recette> " +
                        "SELECT ?label " +
                        "WHERE { VALUES ?famille { " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/plats> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/desserts> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/entrées> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/fromages> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/boissons> " +
                        "} " +
                        "?plat rdf:type ?famille. " +
                        "?plat rdfs:label ?label. " +
                        "?plat recette:a-la-specificite <http://www.univ-rouen.fr/ontologies/restaurant/specificite/vegan> " +
                        "FILTER(LANGMATCHES(LANG(?label), \"fr\")) " +
                        "} ";
        Query query = QueryFactory.create(sQuery2);
        QueryExecution qexec = QueryExecutionFactory.create(query, ontology);
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {

                QuerySolution solution = rs.next();
                String label = solution.getLiteral("label").getString();
                System.out.println(label);


            }
        } finally {
            qexec.close();
        }
        return;
    }

    public void getAllPlatOf(String label) {
        Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);
        String sQuery =
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "prefix menu: <http://www.univ-rouen.fr/ontologies/restaurant/carte/menu> " +
                        "prefix restaurant: <http://www.univ-rouen.fr/ontologies/restaurant> " +
                        "prefix carte: <http://www.univ-rouen.fr/ontologies/restaurant/carte> " +
                        "SELECT ?adresse " +
                        "WHERE { " +
//                        "VALUES ?famille { " +
//                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/plats> " +
//                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/desserts> " +
//                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/entrées> " +
//                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/fromages> " +
//                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/boissons> " +
//                        "} " +
                        "?restaurant rdf:type <http://www.univ-rouen.fr/ontologies/restaurant>. " +
                        "?restaurant rdfs:label \"" + label +"\". " +
//                        "?restaurant restaurant:propose ?carte. " +
//                        "?carte carte:contient ?plat. " +
//                        "?plat rdf:type ?famille. " +
//                        "?plat rdfs:label ?label " +
//                        "FILTER(LANGMATCHES(LANG(?label), \"fr\")) " +
                        "} ";
        System.out.println(sQuery);
        Query query = QueryFactory.create(sQuery);
        QueryExecution qexec = QueryExecutionFactory.create(query, ontology);
        try {
            ResultSet rs = qexec.execSelect();
            while (rs.hasNext()) {

                QuerySolution solution = rs.next();
                String label_result = solution.getLiteral("label").getString();
                System.out.println(label_result);

            }
        } finally {
            qexec.close();
        }
        return;
    }
}

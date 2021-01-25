import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class OntologyDAO {

    public int getPlatAmount(RDFConnection conn) {
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
        AtomicInteger toRet = new AtomicInteger();
        if (conn == null) {
            Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);
            Query query = QueryFactory.create(sQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, ontology);
            try {
                ResultSet rs = qexec.execSelect();
                while (rs.hasNext()){
                    toRet.incrementAndGet();
                    rs.next();
                }
            } finally {
                qexec.close();
            }
        } else {
            conn.querySelect(sQuery, qs -> {
                toRet.incrementAndGet();
            });
        }


        return toRet.get();
    }

    public List<String> getAllPlat(RDFConnection conn) {
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
        List<String> res = new ArrayList<String>();
        if (conn == null) {
            Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);
            Query query = QueryFactory.create(sQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, ontology);
            try {
                ResultSet rs = qexec.execSelect();
                while (rs.hasNext()) {
                    QuerySolution solution = rs.next();
                    String label = solution.getLiteral("label").getString();
                    res.add(label);
                }
            } finally {
                qexec.close();
            }
        } else {
            conn.querySelect(sQuery, qs -> {
                res.add(qs.getLiteral("label").getString());
            });
        }

        return res;
    }

    public void updatePrice(int price, String label) {
        String sQuery =
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "prefix carte: <http://www.univ-rouen.fr/ontologies/restaurant/carte> " +
                        "DELETE { ?plat carte:prixDeVenteTtc ?price } " +
                        "INSERT { ?plat carte:prixDeVenteTtc '" + price + "' } " +
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
                        "FILTER(regex(?label, \"" + label + "\"))  " +
                        "} ";
        Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);
        UpdateRequest request = UpdateFactory.create(sQuery);

        UpdateAction.execute(request, ontology);

//--------TEST----------------------------------------------------------------------------------------------------------
//        String sQuery2 =
//                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
//                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
//                        "prefix menu: <http://www.univ-rouen.fr/ontologies/restaurant/carte/menu> " +
//                        "prefix carte: <http://www.univ-rouen.fr/ontologies/restaurant/carte> " +
//                        "SELECT ?price " +
//                        "WHERE { " +
//                        "?plat rdfs:label ?label. " +
//                        "?plat carte:prixDeVenteTtc ?price. " +
////                        "FILTER(LANGMATCHES(LANG(?label), \"fr\")) " +
//                        "FILTER(regex(?label, \"Fondant au chocolat, meringue et glace Vanille\"))  " +
//                        "} ";
//        System.out.println(sQuery2);
//        Query query = QueryFactory.create(sQuery2);
//        QueryExecution qexec = QueryExecutionFactory.create(query, ontology);
//        try {
//            ResultSet rs = qexec.execSelect();
//            ResultSetFormatter.outputAsJSON(rs);
//            while (rs.hasNext()) {
//                System.out.println("while");
//                QuerySolution solution = rs.next();
//                String price_result = solution.getLiteral("price").getString();
//                System.out.println(price_result);
//
//
//            }
//            System.out.println("no while");
//        } finally {
//            qexec.close();
//        }
        return;
    }

    public List<String> getAllVegetarianPlat(RDFConnection conn) {
        String sQuery =
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
        List<String> res = new ArrayList<>();

        if (conn == null) {
            Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);
            Query query = QueryFactory.create(sQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, ontology);
            try {
                ResultSet rs = qexec.execSelect();
                while (rs.hasNext()) {

                    QuerySolution solution = rs.next();
                    String label = solution.getLiteral("label").getString();
                    res.add(label);
                }
            } finally {
                qexec.close();
            }
        } else {
            conn.querySelect(sQuery, qs -> {
                res.add(qs.getLiteral("label").getString());
            });
        }


        return res;
    }

    public List<String> getAllPlatOf(String label, RDFConnection conn) {
        String sQuery =
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "prefix menu: <http://www.univ-rouen.fr/ontologies/restaurant/carte/menu> " +
                        "prefix restaurant: <http://www.univ-rouen.fr/ontologies/restaurant> " +
                        "prefix carte: <http://www.univ-rouen.fr/ontologies/restaurant/carte> " +
                        "SELECT DISTINCT ?label " +
                        "WHERE { " +
                        "VALUES ?famille { " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/plats> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/desserts> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/entrées> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/fromages> " +
                        "<http://www.univ-rouen.fr/ontologies/restaurant/carte/recette/boissons> " +
                        "} " +
                        "?restaurant rdf:type <http://www.univ-rouen.fr/ontologies/restaurant>. " +
                        "?restaurant rdfs:label ?labelRestau. " +
                        "?restaurant restaurant:propose ?carte. " +
                        "?carte carte:contient ?plat. " +
                        "?plat rdf:type ?famille. " +
                        "?plat rdfs:label ?label " +
                        "FILTER(LANGMATCHES(LANG(?label), \"fr\")) " +
                        "FILTER(regex(?labelRestau, \"" + label + "\"))  " +
                        "} ";
        List<String> res = new ArrayList<>();
        if (conn == null) {
            Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);
            Query query = QueryFactory.create(sQuery);
            QueryExecution qexec = QueryExecutionFactory.create(query, ontology);
            try {
                ResultSet rs = qexec.execSelect();
                while (rs.hasNext()) {

                    QuerySolution solution = rs.next();
                    String label_result = solution.getLiteral("label").getString();
                    res.add(label_result);
                }

            } finally {
                qexec.close();
            }
        } else {
            conn.querySelect(sQuery, qs -> {
                res.add(qs.getLiteral("label").getString());
            });
        }

        return res;
    }
}

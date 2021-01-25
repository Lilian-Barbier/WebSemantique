import org.apache.commons.lang3.Validate;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.riot.RDFDataMgr;

import java.io.InputStream;

@SuppressWarnings("*")
public class WebSemantiqueApplication {

    public static boolean IS_LOCAL = false;
    public static String JENA_URL = "http://localhost:3030/";
    public static String DATASET_NAME = "tpwebsem";

    public static void main(String[] args) {
        OntologyDAO dao = new OntologyDAO();
        if (IS_LOCAL) {
            System.out.println("---------------- Local ----------------");

            Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);

            OntModel schema = OntologyFactory.getSchema(OntologyFactory.SCHEMA);

            System.out.println("--------------Nombre de plats : " + dao.getPlatAmount(null));
            System.out.println("--------------Liste des plats :");
            dao.getAllPlat(null);
            System.out.println("--------------Mise à jour du prix :");
            dao.updatePrice(35,"Fondant au chocolat, meringue et glace Vanille");
            System.out.println("--------------Liste des plats végétariens :");
            dao.getAllVegetarianPlat(null);
            System.out.println("--------------Liste des plats d'un restaurant particulier :");
            dao.getAllPlatOf("La Brasserie des 2 rois", null);
        } else {
            System.out.println("---------------- Remote ----------------");
            try (RDFConnection conn = RDFConnectionFactory.connect(JENA_URL + DATASET_NAME)) {
                System.out.println(dao.getPlatAmount(conn));

            } catch (Exception e) {
                System.out.println("Une erreur a eu lieu avec la base distante.");
                System.out.println(e);
            }
        }
        //        System.out.println("----------------  Liste des sujets ---------------- ");
//        ResIterator iteratorSubject = ontology.listSubjects();
//        while (iteratorSubject.hasNext()) {
//            Resource r = iteratorSubject.next();
//            System.out.println(r.getURI());
//        }
//
//        System.out.println("---------------- Listes des prédicats ---------------- ");
//        StmtIterator iteratorStmt = ontology.listStatements();
//        while (iteratorStmt.hasNext()) {
//            Statement r = iteratorStmt.next();
//            System.out.println(r.getSubject().toString());
//            System.out.println(r.getPredicate().toString());
//            System.out.println(r.getObject().toString());
//            System.out.println();
//        }
//        System.out.println("---------------- Listes des objets ---------------- ");
//        NodeIterator iteratorObject = ontology.listObjects();
//        while (iteratorObject.hasNext()) {
//            RDFNode r = iteratorObject.next();
//            System.out.println(r.toString());
//        }

//        System.out.println("---------------- tous ---------------- ");
//
//        StmtIterator it = ontology.listStatements(
//                new SimpleSelector(ontology.createResource("http://www.univ-rouen.fr/ontologies/restaurant/carte/recette"),
//                        ontology.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), (RDFNode) null) {
//                    public boolean selects(Statement s){
//                        return true;
//                    }
//                });
//        while (it.hasNext()) {
//            Statement s = it.next();
//            Statement r = it.next();
//            System.out.println(r.getObject().toString());
//            System.out.println();
//        }
//        System.out.println("Program ended");
    }


    private void validateOntology(Model schema, Model ontology) {
        ValidityReport report = ModelFactory.createRDFSModel(schema, ontology).validate();
        System.out.println("isClean : " + report.isClean());
        System.out.println("isValid : " + report.isValid());

    }
}
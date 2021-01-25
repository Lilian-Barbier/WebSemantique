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

    public static boolean IS_LOCAL = true;
    public static String JENA_URL = "http://localhost:3030/";
    public static String DATASET_NAME = "tpwebsem";

    public static void main(String[] args) {
        OntologyDAO dao = new OntologyDAO();
        if (IS_LOCAL) {
            System.out.println("---------------- Local ----------------");

            Model ontology = OntologyFactory.getOntology(OntologyFactory.ONTOLOGY);

            OntModel schema = OntologyFactory.getSchema(OntologyFactory.SCHEMA);

            System.out.println("-----Nombre de plats : " + dao.getPlatAmount(null));
            System.out.println("-----Liste des plats :");
            for (String s : dao.getAllPlat(null)) {
                System.out.println("- " + s);
            }
            System.out.println("-----Mise à jour du prix :");
            dao.updatePrice(35,"Fondant au chocolat, meringue et glace Vanille");
            System.out.println("-----Liste des plats végétariens :");
            for (String s : dao.getAllVegetarianPlat(null)) {
                System.out.println("- " + s);
            }
            System.out.println("-----Liste des plats d'un restaurant particulier :");
            for (String s : dao.getAllPlatOf("La Brasserie des 2 rois",null)) {
                System.out.println("- " + s);
            }
        } else {
            System.out.println("---------------- Remote ----------------");
            try (RDFConnection conn = RDFConnectionFactory.connect(JENA_URL + DATASET_NAME)) {
                System.out.println("-----Nombre de plats : "  + dao.getPlatAmount(conn));
                System.out.println("-----Liste des plats :");
                for (String s : dao.getAllPlat(conn)) {
                    System.out.println("- " + s);
                }
                System.out.println("-----Liste des plats végétariens :");
                for (String s : dao.getAllVegetarianPlat(conn)) {
                    System.out.println("- " + s);
                }
                System.out.println("-----Liste des plats d'un restaurant particulier :");
                for (String s : dao.getAllPlatOf("La Brasserie des 2 rois",conn)) {
                    System.out.println("- " + s);
                }

            } catch (Exception e) {
                System.out.println("Une erreur a eu lieu avec la base distante.");
                System.out.println(e);
            }

        }
    }


    private void validateOntology(Model schema, Model ontology) {
        ValidityReport report = ModelFactory.createRDFSModel(schema, ontology).validate();
        System.out.println("isClean : " + report.isClean());
        System.out.println("isValid : " + report.isValid());

    }
}
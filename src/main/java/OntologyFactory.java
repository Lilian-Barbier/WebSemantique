import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

import java.io.InputStream;

public class OntologyFactory {

    public static final String  MENU_ONTOLOGY = "menu.rdf";
    public static final String ONTOLOGY = "menu.rdf";


    public static Model getOntology(String ontologyFileName) {
        Model model = ModelFactory.createDefaultModel();
        // use the RDFDataMgr to find the input file
        InputStream in = RDFDataMgr.open( ontologyFileName );
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: " + ontologyFileName + " not found");
        }

        model.read(in, null);

        return model;
    }
}

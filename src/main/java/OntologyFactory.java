import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;

import java.io.InputStream;

public class OntologyFactory {

    public static final String ONTOLOGY = "ontologie.rdf";
    public static final String SCHEMA = "schema.rdf";

    public static Model getOntology(String ontologyFileName) {

        Model model = ModelFactory.createDefaultModel();

        // use the RDFDataMgr to find the input file
        InputStream in = RDFDataMgr.open( ontologyFileName );
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: " + ontologyFileName + " not found");
        }

        model.read(in, null);

        //Model ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);


        return model;
    }

    public static OntModel getSchema(String ontologyFileName) {

        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
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

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;

import java.io.InputStream;

public class WebSemantiqueApplication {

    public static void main(String[] args) {
        System.out.println("Program started");
        String inputFileName = "src\\main\\resources\\menu.rdf";

        Model model = ModelFactory.createDefaultModel();

        // use the RDFDataMgr to find the input file
        InputStream in = RDFDataMgr.open( inputFileName );
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: " + inputFileName + " not found");
        }

        model.read(in, null);
        model.listObjects();

        System.out.println("----------------  Liste des sujets ---------------- ");
        ResIterator iteratorSubject = model.listSubjects();
        while (iteratorSubject.hasNext()) {
            Resource r = iteratorSubject.next();
            System.out.println(r.getURI());
        }

        System.out.println("---------------- Listes des prédicats ---------------- ");
        StmtIterator iteratorStmt = model.listStatements();
        while (iteratorStmt.hasNext()) {
            Statement r = iteratorStmt.next();
            System.out.println(r.getSubject().toString());
            System.out.println(r.getPredicate().toString());
            System.out.println(r.getObject().toString());
            System.out.println();
        }
        System.out.println("---------------- Listes des objets ---------------- ");
        NodeIterator iteratorObject = model.listObjects();
        while (iteratorObject.hasNext()) {
            RDFNode r = iteratorObject.next();
            System.out.println(r.toString());
        }

        System.out.println("---------------- tous ---------------- ");

        StmtIterator it = model.listStatements(
                new SimpleSelector(model.createResource("http://www.univ-rouen.fr/ontologies/restaurant/carte/menu/menu-enfant/menu-des-petits-rois"),
                        model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), (RDFNode) null) {
                    public boolean selects(Statement s){
                        return true;
                    }
                });
        while (it.hasNext()) {
            Statement s = it.next();
            Statement r = it.next();
            System.out.println(r.getObject().toString());
            System.out.println();
        }
        System.out.println("Program ended");
    }
}
package org.ld4l.bib2lod;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

class RDFProcessor {

    protected OntModel processInputs(List<InputStream> streams) {
        OntModel unionModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);      
        for (InputStream in : streams) {
            try {
                OntModel processedModel = processInput(in);
                unionModel.add(processedModel);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return unionModel;
    }
    
    // TODO Not sure why the FileNotFoundException would be thrown here...
    protected OntModel processInput(InputStream in) throws FileNotFoundException {
        ModelPostProcessor p = ModelPostProcessorFactory.createModelPostProcessor(in); 
        return p.process();              
    }


}

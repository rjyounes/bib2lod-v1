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
            OntModel processedModel = processInput(in);
            unionModel.add(processedModel);
        }
        return unionModel;
    }
    
    protected OntModel processInput(InputStream in)  {
        ModelPostProcessor p = ModelPostProcessorFactory.createModelPostProcessor(in); 
        return p.process();              
    }

}

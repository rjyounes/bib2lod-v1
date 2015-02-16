package org.ld4l.bib2lod;

import java.io.InputStream;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

class RDFPostProcessor {
    
    protected Model processRecords(List<InputStream> records) {
        
        // This might need to be an OntModel...
        Model allRecords = 
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM); 
        
        for (InputStream record : records) {
            OntModel processedRecordModel = processRecord(record);
            // OK to call add if processedRecordModel is null, or test for
            // non-null value first?
            allRecords.add(processedRecordModel);
        }
        
        return allRecords;
    }
    
    protected OntModel processRecord(InputStream record)  {
        
        OntModel recordModel = 
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        
        // TODO Take advantage of parameters 2 (base) and 3 (lang) to 
        // recordModel.read() by allowing optional specification of base URI 
        // and serialization format as program arguments. Currently relying on 
        // Model defaults. Null base means there are no relative URIs in the 
        // input. Could we handle blank nodes if we specify a base??
        // Default serialization = RDF/XML.
        recordModel.read(record, null, null);
        
        ModelPostProcessor p = 
                ModelPostProcessorFactory.createModelPostProcessor(recordModel);
        
        return (p != null) ? p.process() : null;
    }

}

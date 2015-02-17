package org.ld4l.bib2lod;

import java.io.InputStream;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

class RDFPostProcessor {
    
    protected OntModel processRecords(List<InputStream> records, String localNamespace) {
        
        OntModel allRecords = 
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM); 
        
        for (InputStream record : records) {
            OntModel processedRecordModel = processRecord(record, localNamespace);
            // OK to call add if processedRecordModel is null, or test for
            // non-null value first?
            allRecords.add(processedRecordModel);
        }
        
        return allRecords;
    }
    
    protected OntModel processRecord(InputStream record, String baseUri)  {
        
        OntModel recordModel = 
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        
        // Third parameter = serialization. Currently using default RDF/XML.
        // In future may want to specify as commandline option.
        // Default serialization = RDF/XML.
        recordModel.read(record, baseUri, null);
        
        ModelPostProcessor p = 
                ModelPostProcessorFactory.createModelPostProcessor(
                        recordModel, baseUri); 
                               
        return (p != null) ? p.process() : null;
    }

}

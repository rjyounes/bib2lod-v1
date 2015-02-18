package org.ld4l.bib2lod;

import java.io.InputStream;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

class RDFPostProcessor {
    
    protected OntModel processRecords(List<InputStream> records, 
            List<InputStream> ontologies, String localNamespace) {
        
        OntModel ontologyModel = 
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM); 
        
        /* 
         * Consider making this or recordModel an inferencing model, so we don't, 
         * e.g., have to assert inverse property relations. Makes operations on
         * the model more expensive, however. Check: when the ontologyModel is
         * removed, does that remove all the inferences? Javadoc states that
         * it is optional to "rebind any associated inferencing engine to the 
         * new data (which may be an expensive operation)," (if rebind is set
         * to true or we use removeSubModel(Model). Without rebinding inferences
         * to recordModel, it won't work anyway.
         */
        OntModel allRecords = 
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM); 
        
        for (InputStream record : records) {
            OntModel processedRecordModel = processRecord(record, ontologyModel, localNamespace);
            // OK to call add if processedRecordModel is null, or test for
            // non-null value first?
            allRecords.add(processedRecordModel);
        }
        
        return allRecords;
    }
    
    protected OntModel processRecord(InputStream record, OntModel ontologyModel, String baseUri)  {

        /* 
         * Consider making this or allRecords an inferencing model, so we don't, 
         * e.g., have to assert inverse property relations. Makes operations on
         * the model more expensive, however. Check: when the ontologyModel is
         * removed, does that remove all the inferences? Javadoc states that
         * it is optional to "rebind any associated inferencing engine to the 
         * new data (which may be an expensive operation)," (if rebind is set
         * to true or we use removeSubModel(Model). Without rebinding inferences
         * to recordModel, it won't work anyway.
         */
        OntModel recordModel = 
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        // TODO Remove the parts of the ontologies (except Bibframe and LD4L)
        // that we're not using, so there are fewer statements to add to and 
        // remove from recordModel.
        recordModel.addSubModel(ontologyModel);
        
        // Third parameter = serialization. Currently using default RDF/XML.
        // In future may want to specify as commandline option.
        // Default serialization = RDF/XML.
        recordModel.read(record, baseUri, null);
        
        ModelPostProcessor p = 
                ModelPostProcessorFactory.createModelPostProcessor(
                        recordModel, baseUri); 

        if (p == null) {
            return null;
        }
        OntModel processedRecordModel = p.process();
        // Here is where rebinding inferences would take place, if recordModel
        // were an inferencing model.
        processedRecordModel.remove(ontologyModel);
        return processedRecordModel;
    }

}

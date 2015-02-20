package org.ld4l.bib2lod.postprocessor;

import java.io.InputStream;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RDFPostProcessor {
    
    public static String LOCAL_NAMESPACE;
    
    public RDFPostProcessor(String localNamespace) {
        LOCAL_NAMESPACE = localNamespace;
    }
    
    public OntModel processRecords(List<InputStream> records) { 
        
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
            OntModel processedRecordModel = processRecord(record);
            // OK to call add if processedRecordModel is null, or test for
            // non-null value first?
            allRecords.add(processedRecordModel);
        }
        
        return allRecords;
    }
    
    protected OntModel processRecord(InputStream record)  {

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

        // Third parameter = serialization. Currently using default RDF/XML.
        // In future may want to specify as commandline option.
        // Default serialization = RDF/XML.
        recordModel.read(record, LOCAL_NAMESPACE, null);
        
        ModelPostProcessor p = 
                ModelPostProcessorFactory.createModelPostProcessor(recordModel);                       

        if (p == null) {
            return null;
        }
        
        OntModel processedRecordModel = p.process();
        return processedRecordModel;
    }

}

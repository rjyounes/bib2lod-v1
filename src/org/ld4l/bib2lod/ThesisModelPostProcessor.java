/**
 * 
 */
package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.BF_CREATOR_PROPERTY;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author rjy7
 *
 */
class ThesisModelPostProcessor extends ModelPostProcessor {

    protected ThesisModelPostProcessor(
            OntModel recordModel, Individual bfWork, String baseUri) {
        super(recordModel, bfWork, baseUri);
    }    
      
    protected void processRecord() {

        assertionsModel.add(createFoafPersonCreator());

    }
    

    protected OntModel createFoafPersonCreator() {

        // Find the bf:Person creator of the bf:Work.
        Resource bfCreator = 
                bfWork.getPropertyResourceValue(BF_CREATOR_PROPERTY);
        
        // Get the bf:Person individual from the model.
        Individual bfPerson = 
                recordModel.getIndividual(bfCreator.getURI());
        
        // Create a corresponding foaf:Person.
        return createFoafPerson(bfPerson);
    }

}

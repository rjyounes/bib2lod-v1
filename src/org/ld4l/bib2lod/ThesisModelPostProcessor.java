/**
 * 
 */
package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.BF_CREATOR_PROPERTY;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

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
        //createFoafPersonCreator();
    }
    

    protected Individual createFoafPersonCreator() {
 
        // Get the bfPerson creator of this.bfWork.
        BfPerson bfPerson = 
                new BfPerson(bfWork, BF_CREATOR_PROPERTY, localNamespace);
        
        // Create a corresponding foaf:Person.
        return bfPerson.createFoafPerson();
        
    }

}

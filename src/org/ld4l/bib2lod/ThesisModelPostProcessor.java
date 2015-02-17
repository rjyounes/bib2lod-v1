/**
 * 
 */
package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.*;

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
        createFoafPersonCreator();
        createFoafPersonAdvisor();
    }   

    protected Individual createFoafPersonCreator() {
 
        // Get the bfPerson creator of this.bfWork.
        BfPerson bfPerson = 
                new BfPerson(bfWork, BF_CREATOR_PROPERTY, localNamespace);
        
        // Create a corresponding foaf:Person.
        return bfPerson.createFoafPerson();
        
    }
    
    protected Individual createFoafPersonAdvisor() {
        
        // Get the bfPerson advisor of this.bfWork.
        BfPerson bfPerson = 
                new BfPerson(bfWork, RELATORS_THS_PROPERTY, localNamespace); 

        Individual foafPerson = bfPerson.createFoafPerson();

        // TODO Check that this retraction works.
        // <bf:hasAuthority rdf:resource="http://vivo.cornell.edu/individual/individual23258"/>
        // The vivo individual is NOT an authority       
        // CAUTION: SIDE EFFECT
        bfPerson.getBaseIndividual().removeProperty(BF_HAS_AUTHORITY_PROPERTY, foafPerson);
        
        // TODO Do we need anything else here?
        
        return foafPerson;
    }

}

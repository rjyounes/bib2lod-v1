/**
 * 
 */
package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.BF_CREATOR_PROPERTY;
import static org.ld4l.bib2lod.Constants.LD4L_THESIS_CLASS;
import static org.ld4l.bib2lod.Constants.RELATORS_THS_PROPERTY;

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
        assignThesisType();
        createFoafPersonCreator();
        createFoafPersonAdvisor();
    }   
    
    private void assignThesisType() {
        // Apparently no difference between these two, other than addOntClass() 
        // is a method on Individual while addRDFType() is a method on 
        // OntResource.
        // bfWork.addRDFType(LD4L_THESIS_CLASS);
        bfWork.addOntClass(LD4L_THESIS_CLASS);
    }

    private Individual createFoafPersonCreator() {
 
        // Get the bfPerson creator of this.bfWork.
        BfPerson bfPerson = 
                new BfPerson(bfWork, BF_CREATOR_PROPERTY, localNamespace);
        
        // Create a corresponding foaf:Person.
        return bfPerson.createFoafPerson();
        
    }
    
    private Individual createFoafPersonAdvisor() {
        
        // Get the bfPerson advisor of this.bfWork.
        BfPerson bfPerson = 
                new BfPerson(bfWork, RELATORS_THS_PROPERTY, localNamespace); 

        Individual foafPerson = bfPerson.createFoafPerson();

        return foafPerson;
    }

}

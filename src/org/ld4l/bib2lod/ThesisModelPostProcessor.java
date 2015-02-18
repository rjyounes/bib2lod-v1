/**
 * 
 */
package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.BF_CREATOR_URI;
import static org.ld4l.bib2lod.Constants.LD4L_THESIS_URI;
import static org.ld4l.bib2lod.Constants.RELATORS_THS_URI;

import org.ld4l.bib2lod.bfindividual.BfIndividual;
import org.ld4l.bib2lod.bfindividual.BfIndividualFactory;
import org.ld4l.bib2lod.bfindividual.BfPerson;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

/**
 * @author rjy7
 *
 */
class ThesisModelPostProcessor extends ModelPostProcessor {

    protected ThesisModelPostProcessor(
            OntModel recordModel, Individual bfWork) {
        super(recordModel, bfWork);
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
        bfWork.addOntClass(recordModel.getProperty(LD4L_THESIS_URI));
    }

    private Individual createFoafPersonCreator() {
 
        // Get the bfPerson creator of this.bfWork.
        BfPerson bfPerson = (BfPerson) 
                BfIndividualFactory.createBfObjectIndividual(bfWork, 
                        recordModel.getProperty(BF_CREATOR_URI));
                       
        // Create a corresponding foaf:Person.
        return bfPerson.createFoafPerson();
        
    }
    
    private Individual createFoafPersonAdvisor() {
        
        // Get the bfPerson advisor of this.bfWork.
        BfPerson bfPerson = (BfPerson) 
                BfIndividualFactory.createBfObjectIndividual(bfWork, 
                        recordModel.getProperty(RELATORS_THS_URI));

        Individual foafPerson = bfPerson.createFoafPerson();

        return foafPerson;
    }

}

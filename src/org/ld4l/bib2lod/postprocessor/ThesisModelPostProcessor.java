/**
 * 
 */
package org.ld4l.bib2lod.postprocessor;

import static org.ld4l.bib2lod.postprocessor.Constants.BF_CREATOR_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.LD4L_AUTHOR_OF_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.LD4L_THESIS_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.PAV_AUTHORED_BY_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.RELATORS_THS_URI;

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
        // bfWork.addRDFType(recordModel.getProperty(LD4L_THESIS_URI));
        bfWork.addOntClass(recordModel.getProperty(LD4L_THESIS_URI));
    }

    private Individual createFoafPersonCreator() {
 
        // Get the bfPerson creator of this.bfWork.
        BfPerson bfPerson = (BfPerson) 
                BfIndividualFactory.createBfObjectIndividual(bfWork, 
                        recordModel.getProperty(BF_CREATOR_URI));
                       
        // Create a corresponding foaf:Person.
        Individual foafPerson = bfPerson.createFoafPerson();
        
        // Create the relationships between the bfWork and the foafPerson.
        bfWork.addProperty(recordModel.getProperty(PAV_AUTHORED_BY_URI), foafPerson);
        
        // Make the inverse assertion for ingest into systems that don't do
        // inverse inferencing. 
        /* NB if recordModel (or allRecords) were an inferencing OntModel, we
         * wouldn't need to make the inverse assertion. See notes in 
         * ModelPostProcessor.processRecords() and 
         * ModelPostProcessor.processRecord().
         */
        foafPerson.addProperty(recordModel.getProperty(
                LD4L_AUTHOR_OF_URI), bfWork);

        return foafPerson;
        
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

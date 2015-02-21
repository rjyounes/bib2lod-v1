/**
 * 
 */
package org.ld4l.bib2lod.postprocessor;

import static org.ld4l.bib2lod.postprocessor.Constants.BF_CREATOR_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_DISSERTATION_INSTITUTION_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_HAS_AUTHORITY_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_LABEL_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_ORGANIZATION_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.LD4L_AUTHOR_OF_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.LD4L_THESIS_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.PAV_AUTHORED_BY_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.RELATORS_THS_URI;

import org.ld4l.bib2lod.bfindividual.BfIndividualFactory;
import org.ld4l.bib2lod.bfindividual.BfOrganization;
import org.ld4l.bib2lod.bfindividual.BfPerson;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * @author rjy7
 *
 */
class ThesisModelPostProcessor extends ModelPostProcessor {

    protected ThesisModelPostProcessor(
            Individual bfWork, OntModel recordModel, OntModel allRecords) {
        super(bfWork, recordModel, allRecords);
    }    
      
    protected void processRecord() {
        assignThesisType();
        linkToFoafOrganization();
        createFoafPersonCreator();
        linkToFoafPersonAdvisor();
    }   
    
    private void assignThesisType() {
        // Apparently no difference between these two, other than addOntClass() 
        // is a method on Individual while addRDFType() is a method on 
        // OntResource.
        // bfWork.addRDFType(recordModel.getProperty(LD4L_THESIS_URI));
        bfWork.addOntClass(recordModel.getProperty(LD4L_THESIS_URI));
    }
    
    /** 
     * Dedupe the dissertaton institution. If a previous institution of the same 
     * name is found, remove this one, and relink the work to the existing one.
     * Otherwise, create a foaf:Organization as the related RWO. For now, we
     * only have the bf:label to use for deduping, which is sufficient for the
     * thesis data set, but it needs to be more sophisticated.
     */
    private Individual linkToFoafOrganization() {

        // Get the bf:dissertationInstitution property that links the bfWork
        // to the institution.
        Property dissertationInstitutionProperty = recordModel.getProperty(
                BF_DISSERTATION_INSTITUTION_URI);
        
        // Create a bfOrganization wrapper to handle the linking between
        // bfWork and the dissertation institution.
        BfOrganization bfInstitution = (BfOrganization) BfIndividualFactory.
                createBfObjectIndividual(bfWork, 
                        dissertationInstitutionProperty);
                
        return bfInstitution.createFoafOrganization(bfWork,
                dissertationInstitutionProperty, allRecords);        
    }

    private void createFoafPersonCreator() {
 
        // Get the bfPerson creator of this.bfWork.
        BfPerson bfPerson = (BfPerson) 
                BfIndividualFactory.createBfObjectIndividual(bfWork, 
                        recordModel.getProperty(BF_CREATOR_URI));
                       
        // Create a corresponding foaf:Person.
        Individual foafPerson = bfPerson.createFoafPerson();
        
        // Create the relationships between the bfWork and the foafPerson.
        bfWork.addProperty(recordModel.getProperty(
                PAV_AUTHORED_BY_URI), foafPerson);
        
        // Make the inverse assertion, for ingest into systems that don't do
        // inverse inferencing. 
        /* NB if recordModel (or allRecords) were an inferencing OntModel, we
         * wouldn't need to make the inverse assertion. See notes in 
         * ModelPostProcessor.processRecords() and 
         * ModelPostProcessor.processRecord().
         */
        foafPerson.addProperty(recordModel.getProperty(
                LD4L_AUTHOR_OF_URI), bfWork);       
    }
    
    private Individual linkToFoafPersonAdvisor() {

        // Get the relators:ths property that links the bfWork to the advisor.
        Property thesisAdvisorProperty = recordModel.getProperty(
                RELATORS_THS_URI);
 
        // Create a bfPerson wrapper to handle the linking between the bfWork
        // bfWork and the thesis advisor.
        BfPerson bfPerson = (BfPerson) BfIndividualFactory.
                createBfObjectIndividual(bfWork, 
                        thesisAdvisorProperty);
        
        return bfPerson.createFoafPerson(bfWork,
                thesisAdvisorProperty, allRecords);       

    }

}

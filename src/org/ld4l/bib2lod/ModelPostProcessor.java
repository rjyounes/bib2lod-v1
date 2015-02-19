/**
 * 
 */
package org.ld4l.bib2lod;

import org.ld4l.bib2lod.bfindividual.BfIndividual;
import org.ld4l.bib2lod.bfindividual.BfIndividualFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * @author rjy7
 *
 */
abstract class ModelPostProcessor {
    
    protected Individual bfWork;
    protected OntModel recordModel;
    
    // TBD if these are needed
    protected OntModel assertionsModel;        
    protected OntModel retractionsModel;
    
    protected ModelPostProcessor(
            OntModel recordModel, Individual bfWork) {
        
        this.recordModel = recordModel;
        this.bfWork = bfWork;
        
        // Are we going to need this? Perhaps all statements are added via
        // the Individual.
        assertionsModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        // Are we going to need this? There may not be any statements to retract.
        // If there are, they may all be retracted via the Individual.
        retractionsModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
    }
    
    protected OntModel getOntModel() {
        return recordModel;
    }

    /**
     * Controls overall flow common to all types of post-processors.
     */
    protected OntModel process() {
        processRecord(); 
        addRdfsLabels();
        // applyModelChanges();
        return recordModel;
    }
    
    // Each subclass must define its own processRecord method.
    protected abstract void processRecord();
    
    /**
     * Add rdfs:label to any instances in the model that don't have one.
     * 
     * Needed for Vitro. Should probably be done during Vitro ingest, but it's
     * not clear at the moment how to hook into the Vitro RDF ingest code.
     * 
     * Some of these will have been added during post-processing. Add the
     * labels to individuals without an rdfs:label.
     * 
     * We'll just keep the intact bf:label as the rdfs:label. Bibframe 
     * individuals are authorities rather than RWOs, so it's appropriate, e.g., 
     * to keep the birth and death dates in the label. The problem will be if
     * there is no bf:label. Then we have to find it from the title or other
     * property. However, don't all individuals created by the converter get
     * assigned a bf:label? So then only the primary work could be missing a 
     * label. CHECK THIS OUT.    
     */
    private void addRdfsLabels() {
        // Seems to be the best way to get only instances. It happens that
        // every object instance in Bibframe RDF is also a subject. 
        // Doesn't include blank nodes. Should they have an rdfs:label?
        ResIterator subjects = recordModel.listSubjects();
        while (subjects.hasNext()) {
            Resource subject = subjects.next();
            String uri = subject.getURI();
            if (uri != null) {
                Individual individual = recordModel.getIndividual(subject.getURI());
                BfIndividual bfIndividual = 
                        BfIndividualFactory.createBfIndividual(individual);
                bfIndividual.addRdfsLabel();  
            }
        }       
    }

    
    /** 
     * Apply assertions and retractions to recordModel.
     * 
     * TODO May not need this if we're adding individuals to recordModel.
     * May still need retractions? Probably not - we're not REMOVING anything
     * from the original record.
     * 
     */
    protected void applyModelChanges() {
        recordModel.add(assertionsModel);
    }
    

    

}

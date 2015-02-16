/**
 * 
 */
package org.ld4l.bib2lod;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * @author rjy7
 *
 */
abstract class ModelPostProcessor {
    
    // Not sure yet whether we want these to be instance variables, or pass
    // them between methods.
    protected Resource bfWork;
    protected OntModel recordModel;
    protected OntModel assertionsModel = 
            ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
    protected OntModel retractionsModel = 
            ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
    
    protected ModelPostProcessor(OntModel recordModel, Resource bfWork) {
        this.recordModel = recordModel;
        this.bfWork = bfWork;
    }
    
    protected OntModel getOntModel() {
        return recordModel;
    }

    /**
     * Controls overall flow common to all types of post-processors.
     */
    protected OntModel process() {
        processWork(); 
        addRdfsLabels();
        applyModelChanges();
        return recordModel;
    }
    

    // Each subclass must define its own processWork method.
    protected abstract void processWork();
    
    /**
     * Add rdfs:label to any resources in the model that don't have one.
     * 
     * Needed for Vitro. Should probably be done during Vitro ingest, but it's
     * not clear at the moment how to hook into the Vitro RDF ingest code.
     * 
     */
    protected void addRdfsLabels() {
        ExtendedIterator<Individual> individuals = recordModel.listIndividuals();
        while (individuals.hasNext()) {
            Resource individual = individuals.next();
            //if (! individual.hasProperty(RDFS.LABEL) {

            //}
        }
    }
    
    /** 
     * Apply assertions and retractions to this.recordModel.
     */
    protected void applyModelChanges() {
        // Apply assertions and retractions to this.recordModel.
    }

}

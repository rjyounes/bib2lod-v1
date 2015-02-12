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
    
    protected static final String BIBFRAME_NS = "http://bibframe.org/vocab/";
    protected static final String BFWORK_URI = BIBFRAME_NS + "Work";
    
    
    // Not sure yet whether we want these to be instance variables, or pass
    // them between methods.
    protected OntModel ontModel;
    protected OntModel assertionsModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
    protected OntModel retractionsModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);;
    
    protected ModelPostProcessor(String infileName) {
        this.ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM); 
        ontModel.read(infileName);
    }
    
    protected ModelPostProcessor(OntModel inputModel) {
        this.ontModel = inputModel;      
    }
    
    protected OntModel getOntModel() {
        return ontModel;
    }

    /**
     * Controls overall post-processing flow.
     */
    protected OntModel process() {
        doProcessing();
        addRdfsLabels();
        applyModelChanges();
        return ontModel;
    }
    
    protected abstract void doProcessing();
    
    protected void addRdfsLabels() {
        // Add rdfs:label to any resources that don't have one.
        ExtendedIterator<Individual> individuals = ontModel.listIndividuals();
        while (individuals.hasNext()) {
            Resource individual = individuals.next();
            //if (! individual.hasProperty(RDFS.LABEL) {

            //}
        }
    }
    
    protected void applyModelChanges() {
        // Apply assertions and retractions to model.

    }



    


}

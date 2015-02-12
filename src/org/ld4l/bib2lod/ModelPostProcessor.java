/**
 * 
 */
package org.ld4l.bib2lod;

import java.io.InputStream;

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
    protected OntModel retractionsModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
    
    protected ModelPostProcessor(InputStream in) {
        this.ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM); 
        // TODO Take advantage of args 2 and 3 to read(InputStream, base, lang)
        // by allowing optional specification of base URI and serialization
        // format as program arguments. Currently relying on defaults: 
        // Null base means there are no relative URIs in the input. Could we 
        // handle blank nodes if we specify a base??
        // Default serialization = RDF/XML.
        ontModel.read(in, null, null);
    }
    
    protected OntModel getOntModel() {
        return ontModel;
    }

    /**
     * Controls overall flow common to all types of post-processors.
     */
    protected OntModel process() {
        // Do processing for the specific type of processor
        doProcessing(); 
        addRdfsLabels();
        applyModelChanges();
        return ontModel;
    }
    
    /**
     * Perform processing specific to this ModelPostProcessor.
     */
    protected abstract void doProcessing();
    
    /**
     * Add rdfs:label to any resources in the model that don't have one.
     * 
     * Needed for Vitro. Should probably be done during Vitro ingest, but it's
     * not clear at the moment how to hook into the Vitro RDF ingest code.
     * 
     */
    protected void addRdfsLabels() {
        ExtendedIterator<Individual> individuals = ontModel.listIndividuals();
        while (individuals.hasNext()) {
            Resource individual = individuals.next();
            //if (! individual.hasProperty(RDFS.LABEL) {

            //}
        }
    }
    
    /** 
     * Apply assertions and retractions to this.ontModel.
     */
    protected void applyModelChanges() {
        // Apply assertions and retractions to this.ontModel.
    }

}

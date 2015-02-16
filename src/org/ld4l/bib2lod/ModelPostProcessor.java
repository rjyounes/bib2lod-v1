/**
 * 
 */
package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.*;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * @author rjy7
 *
 */
abstract class ModelPostProcessor {
    
    // Not sure yet whether we want these to be instance variables, or pass
    // them between methods.
    protected String baseUri;
    protected Individual bfWork;
    protected OntModel recordModel;
    protected OntModel assertionsModel;        
    protected OntModel retractionsModel;
    
    protected ModelPostProcessor(
            OntModel recordModel, Individual bfWork, String baseUri) {
        this.recordModel = recordModel;
        this.bfWork = bfWork;
        this.baseUri = baseUri;
        this.assertionsModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        // Are we going to need this? There may not be any statements to retract.
        this.retractionsModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
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
        applyModelChanges();
        return recordModel;
    }
    

    // Each subclass must define its own processRecord method.
    protected abstract void processRecord();
    
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
            Individual individual = individuals.next();
            //if (! individual.hasProperty(RDFS.LABEL) {

            //}
        }
    }
    
    /** 
     * Apply assertions and retractions to this.recordModel.
     */
    protected void applyModelChanges() {
        recordModel.add(assertionsModel);
    }

    protected String mintUri(String localName) {
        return baseUri + localName;
    }
    
    /**
     * Create an OntModel of a foaf:Person from a bf:Person 
     * @param Individual bfPerson
     * @return 
     */
    protected OntModel createFoafPerson (Individual bfPerson) {

        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        
        // *** Doesn't work because local name loses the digits at the front.
        // need to add them back to the front of the new local name.
        // What's going on with this??
        Individual foafPerson = 
                ontModel.createIndividual(mintUri(
                        bfPerson.getLocalName() + "foaf"), FOAF_PERSON_CLASS);
        
        Literal bfCreatorLabel = 
                (Literal) bfPerson.getPropertyValue(BF_LABEL_PROPERTY);

        foafPerson.addLiteral(FOAF_NAME_PROPERTY, bfCreatorLabel);

        return ontModel;
    }
}

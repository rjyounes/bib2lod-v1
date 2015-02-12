/**
 * 
 */
package org.ld4l.bib2lod;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.ResIterator;

/**
 * @author rjy7
 *
 */
abstract class ModelPostProcessor {
    
    protected static final String BIBFRAME_NS = "http://bibframe.org/vocab/";
    protected static final String BFWORK_URI = BIBFRAME_NS + "Work";
    
    // Not sure yet whether we want these to be instance variables, or pass
    // them between methods.
    protected Model model;
    protected Model assertionsModel;
    protected Model retractionsModel;
    
    protected ModelPostProcessor(Model model) {
        this.model = model;
        assertionsModel = ModelFactory.createDefaultModel(); 
        retractionsModel = ModelFactory.createDefaultModel(); 
        
        
    }

    /**
     * Controls overall post-processing flow.
     */
    protected void process() {
        doProcessing();
        addRdfsLabels();
        applyModelChanges();
    }
    
    protected abstract void doProcessing();
    
    protected void addRdfsLabels() {
        // Add rdfs:label to any resources that don't have one.

    }
    
    protected void applyModelChanges() {
        // Apply assertions and retractions to model.

    }

    


}

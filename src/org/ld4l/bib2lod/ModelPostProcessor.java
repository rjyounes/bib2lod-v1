/**
 * 
 */
package org.ld4l.bib2lod;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * @author rjy7
 *
 */
abstract class ModelPostProcessor {
    

    protected static final String BIBFRAME_NS = "http://bibframe.org/vocab/";
    protected static final String BFWORK_URI = BIBFRAME_NS + "Work";
    
    protected Model model;
    protected Model assertionsModel;
    protected Model retractionsModel;
    
    protected ModelPostProcessor(Model model) {
        this.model = model;
        assertionsModel = ModelFactory.createDefaultModel(); 
        retractionsModel = ModelFactory.createDefaultModel();     
    }

    protected abstract Model process();
    
    protected Model applyModelChanges() {
        // Apply assertions and retractions to model, and return the resulting
        // model.
        return model;
    }
    


    


}

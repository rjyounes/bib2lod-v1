/**
 * 
 */
package org.ld4l.bib2lod;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * @author rjy7
 *
 */
class BibframePostProcessor implements PostProcessor {
    
    private Model inputModel;
    private Model outputModel;
    private Model assertionsModel;
    private Model retractionsModel;
    
    protected BibframePostProcessor(Model model) {
        inputModel = ModelFactory.createDefaultModel();
        assertionsModel = ModelFactory.createDefaultModel(); 
        retractionsModel = ModelFactory.createDefaultModel();        
    }
    
    protected Model process() {
        
        
        return outputModel;
    }

}

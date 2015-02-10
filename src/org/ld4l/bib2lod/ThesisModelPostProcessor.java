/**
 * 
 */
package org.ld4l.bib2lod;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * @author rjy7
 *
 */
class ThesisModelPostProcessor extends ModelPostProcessor {
    
    protected ThesisModelPostProcessor(Model model) {
        super(model);
    }
      
    // TODO Will probably need to subdivide into smaller methods.
    protected Model process() {
        Resource resourceBfWork = model.createResource(BFWORK_URI);
        ResIterator bfWorks = model.listResourcesWithProperty(RDF.type, resourceBfWork); 
        while (bfWorks.hasNext()) {
            Resource bfWork = bfWorks.next();
            Property bfCreatorProperty = model.createProperty(BIBFRAME_NS, "creator");
            Resource bfCreator = bfWork.getPropertyResourceValue(bfCreatorProperty);
            Property bfLabelProperty = model.createProperty(BIBFRAME_NS, "label");
            String bfLabelValue = bfCreator.getProperty(bfLabelProperty).getString();
            // Create a foaf:Person with the name of the label
            
            
        }
        
        return model;
    }
    



}

/**
 * 
 */
package org.ld4l.bib2lod;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * @author rjy7
 *
 */
class ThesisModelPostProcessor extends ModelPostProcessor {

    protected ThesisModelPostProcessor(InputStream in) {
        super(in);
    }    
      
    // TODO Will probably need to subdivide into smaller methods.
    @Override
    protected void doProcessing() {
        Resource resourceBfWork = ontModel.createResource(BFWORK_URI);
        ResIterator bfWorks = ontModel.listResourcesWithProperty(RDF.type, resourceBfWork); 
        while (bfWorks.hasNext()) {
            Resource bfWork = bfWorks.next();
            Property bfCreatorProperty = ontModel.createProperty(BIBFRAME_NS, "creator");
            Resource bfCreator = bfWork.getPropertyResourceValue(bfCreatorProperty);
            Property bfLabelProperty = ontModel.createProperty(BIBFRAME_NS, "label");
            String bfLabelValue = bfCreator.getProperty(bfLabelProperty).getString();
            // Create a foaf:Person with the name of the label           
        }
        
        //return ontmodel;

    }


}

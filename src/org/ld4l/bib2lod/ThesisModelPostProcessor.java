/**
 * 
 */
package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.BFCREATOR_PROPERTY;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author rjy7
 *
 */
class ThesisModelPostProcessor extends ModelPostProcessor {

    protected ThesisModelPostProcessor(OntModel recordModel, Resource bfWork) {
        super(recordModel, bfWork);
    }    
      
    // TODO Will probably need to subdivide into smaller methods.
    protected void processWork() {

        Resource bfCreator = bfWork.getPropertyResourceValue(BFCREATOR_PROPERTY);    
        // Get the literal value of the bfLabel. Then get the String labelValue (getLexicalForm())
        // How do you get a literal, similar to getPropertyResourceValue()?
        //Literal bfLabel = bfCreator.getPropertyResourceValue(BFLABEL_PROPERTY); //.getString();
        // String labelValue = bfLabel.getLexicalString();
        // Create a foaf:Person with the name of the label           
        
    }

}

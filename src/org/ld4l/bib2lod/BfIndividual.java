package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.*;

import org.apache.commons.lang3.StringUtils;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/** Decorator of class Individual to provide post-processor specific 
 * functionality.
 * @author rjy7
 *
 */

public class BfIndividual {

    protected Individual baseIndividual;
    protected OntModel ontModel;
    protected String bfTypeUri = BF_RESOURCE_URI;
    protected Resource bfType; // = BF_RESOURCE_CLASS;
    protected String baseUri;
      
    protected BfIndividual(Individual baseIndividual) {
        this.baseIndividual = baseIndividual; 
        setOntModelAndType(baseIndividual);              
    }
    
    protected BfIndividual(
            Individual relatedIndividual, Property property, String localNamespace) {
         
        setOntModelAndType(relatedIndividual);
        
        Resource bfResource = 
                relatedIndividual.getPropertyResourceValue(property);  
        
        String resourceUri = bfResource.getURI();

        // Get the base Individual from the model.
        baseIndividual = ontModel.getIndividual(resourceUri);
 
        // The baseUri to be used for minting URIs for new Individuals.
        baseUri = computeBaseUri(localNamespace);
    }
    
    private void setOntModelAndType(Individual individual) {
        ontModel = individual.getOntModel();
        bfType = ontModel.getOntProperty(bfTypeUri);
    }
    
    // TODO  Duplicates code in ModelPostProcessor.addRdfsLabels(). Need to 
    // combine.
    protected void addRdfsLabel() {
        
        Literal rdfsLabel = null;
        Literal bfLabel = null;
        
        Property bfLabelProperty = ontModel.getOntProperty(BF_LABEL_URI);
        RDFNode bfLabelNode = 
                baseIndividual.getPropertyValue(bfLabelProperty);
        if (bfLabelNode != null) {
            bfLabel = bfLabelNode.asLiteral();
            rdfsLabel = bfLabel;
        } else {
            // TODO fill in - 
        }
        
        if (bfLabel != null) {
            rdfsLabel = bfLabel;
        } else {
            // TODO Fill in
        }
        
        if (rdfsLabel == null) {
            OntModel ontModel = baseIndividual.getOntModel();
            rdfsLabel = ontModel.createLiteral(baseIndividual.getURI());
        }
        
        baseIndividual.addProperty(RDFS.label, rdfsLabel);
        
    }
    
    protected Individual getBaseIndividual() {
        return baseIndividual;
    }
    
    private String computeBaseUri(String localNamespace) {
        String baseIndividualUri = baseIndividual.getURI();
        /*
        * Jena strips off initial digits of the local name, so we can't use
        * Jena baseIndividual.getLocalName(). Need to parse the URI to
        * get the full local name.
        * Another way to do the same thing: use the bfWork URI as the base
        * URI for any entity created while processing that work. Not sure if
        * that's as reliable, so using the following.
        * Another option: add an initial non-numeric character to the baseUri
        * passed to the Bibframe converter. E.g., 
        * http://localhost:8080/ld4l-vitro/individual/_
        * Then we shouldn't call it localNamespace, but rather baseUri.
        * For now, the BF conversion isn't linked to the post-processor, so 
        * use the current approach. If/when we link them together into a
        * single process, that would make more sense.
        * */
        String localName = 
                StringUtils.substringAfterLast(baseIndividualUri, "/");
        return localNamespace + localName;
    }
    
    protected String getAuthorityResourceUri() {
        String authorityResourceUri = null;
        Property bfHasAuthorityProperty = 
                ontModel.getOntProperty(BF_HAS_AUTHORITY_URI);
        if (baseIndividual.hasProperty(bfHasAuthorityProperty)) {
            Resource authorityResource = 
                    baseIndividual.getPropertyResourceValue(bfHasAuthorityProperty);
            // TODO Should this be limited to external URIs, or should we 
            // include internal ones as well? Seems like the latter.
            authorityResourceUri = authorityResource.getURI();
        }
        return authorityResourceUri;
    } 
    


}

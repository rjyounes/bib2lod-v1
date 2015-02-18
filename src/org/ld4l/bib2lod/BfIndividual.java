package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.BF_HAS_AUTHORITY_PROPERTY;
import static org.ld4l.bib2lod.Constants.BF_LABEL_PROPERTY;
import static org.ld4l.bib2lod.Constants.BF_RESOURCE_CLASS;

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
    protected Resource bfType = BF_RESOURCE_CLASS;
    protected String baseUri = null;
      
    protected BfIndividual(Individual baseIndividual) {
        this.baseIndividual = baseIndividual;
    }
    
    protected BfIndividual(
            Individual relatedIndividual, Property property, String localNamespace) {
         
        OntModel ontModel = relatedIndividual.getOntModel(); 
        
        Resource bfResource = 
                relatedIndividual.getPropertyResourceValue(property);  
        
        String resourceUri = bfResource.getURI();

        // Get the base Individual from the model.
        this.baseIndividual = 
                ontModel.getIndividual(resourceUri);
        
        // The baseUri to be used for minting URIs for new Individuals.
        this.baseUri = computeBaseUri(localNamespace);

    }
    
    // TODO  Duplicates code in ModelPostProcessor.addRdfsLabels(). Need to 
    // combine.
    protected void addRdfsLabel() {
        Individual baseIndividual = this.baseIndividual;
        
        Literal rdfsLabel = null;
        Literal bfLabel = null;
        
        RDFNode bfLabelNode = 
                baseIndividual.getPropertyValue(BF_LABEL_PROPERTY);
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
        return this.baseIndividual;
    }
    
    private String computeBaseUri(String localNamespace) {
        String baseIndividualUri = this.baseIndividual.getURI();
        /*
        * Jena strips off initial digits of the local name, so we can't use
        * Jena this.baseIndividual.getLocalName(). Need to parse the URI to
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
        if (baseIndividual.hasProperty(BF_HAS_AUTHORITY_PROPERTY)) {
            Resource authorityResource = 
                    baseIndividual.getPropertyResourceValue(BF_HAS_AUTHORITY_PROPERTY);
            // TODO Should this be limited to external URIs, or should we 
            // include internal ones as well? Seems like the latter.
            authorityResourceUri = authorityResource.getURI();
        }
        return authorityResourceUri;
    } 
    


}

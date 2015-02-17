package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.*;

import org.apache.commons.lang3.StringUtils;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * The BfIndividual has a pointer to a Jena Individual in order to reference
 * its functionality, while adding additional functionality of its own.
 * 
 * By using a BfIndividual, we can modify it and those statements are 
 * automatically added to the model (MAKE SURE THIS IS TRUE!!). We don't need
 * to create a new model to manually add to the assertions model.
 * 
 * Should this be an abstract class? Wait and see whether we ever need to
 * instantiate a BfIndividual that has no specific Bibframe type. Seems
 * unlikely.
 * 
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
    
    protected void addRdfsLabel() {
        Individual baseIndividual = this.baseIndividual;
        
        String rdfsLabel = null;
        
        String bfLabel = 
                baseIndividual.getPropertyValue(BF_LABEL_PROPERTY).asLiteral().getLexicalForm();
        
        if (bfLabel != null) {
            rdfsLabel = bfLabel;
        } else {
            // TODO Fill in
        }
        
        if (rdfsLabel == null) {
            rdfsLabel = baseIndividual.getURI();
        }
        
        baseIndividual.addProperty(RDFS.label, rdfsLabel);
        
    }
    
    protected Individual getBaseIndividual() {
        return this.baseIndividual;
    }
    
    private String computeBaseUri(String localNamespace) {
        String baseIndividualUri = this.baseIndividual.getURI();
        // Jena strips off initial digits of the local name, so we can't use
        // Jena this.baseIndividual.getLocalName(). Need to parse the URI to
        // get the full local name.
        // Another option: add an initial non-numeric character to the baseUri
        // passed to the Bibframe converter. E.g., 
        // http://localhost:8080/ld4l-vitro/individual/_
        // Then we can't call it localNamespace, but rather baseUri.
        // For now, the two processes aren't linked, so use the current
        // approach.
        String localName = 
                StringUtils.substringAfterLast(baseIndividualUri, "/");
        return localNamespace + localName;
    }
    
    protected String getAuthorityResourceUri() {
        String authorityResourceUri = null;
        if (baseIndividual.hasProperty(BF_HAS_AUTHORITY_PROPERTY)) {
            Resource authorityResource = 
                    baseIndividual.getPropertyResourceValue(BF_HAS_AUTHORITY_PROPERTY);
            // TODO Do we need to ascertain that this is an external URI, or
            // doesn't it matter?
            authorityResourceUri = authorityResource.getURI();
        }
        return authorityResourceUri;
    } 
    


}

package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.postprocessor.Constants.BF_HAS_AUTHORITY_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_LABEL_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_TITLE_PROPERTY_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_TITLE_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_TITLE_VALUE_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.FOAF_NAME_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.MADSRDF_IDENTIFIES_RWO_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URI;
import static org.ld4l.bib2lod.postprocessor.RDFPostProcessor.LOCAL_NAMESPACE;

import org.apache.commons.lang3.StringUtils;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/** Decorator of class Individual The BfIndividual wraps the Jena Individual to 
 * provide functionality specific to the post-processor and the type of the
 * Individual. The Decorator pattern is used because neither extending 
 * IndividualImpl or implementing Individual is easy.
 * @author rjy7
 *
 */
public class BfIndividual {

 
    protected Individual baseIndividual;
    protected OntModel recordModel;
    protected String baseUri;
      
    protected BfIndividual(Individual baseIndividual) {
        this.baseIndividual = baseIndividual; 
        // Store OntModel for convenience.
        recordModel = baseIndividual.getOntModel();
        // The baseUri to be used for minting URIs for new Individuals.
        baseUri = computeBaseUri();
    }    
    
    public Individual getBaseIndividual() {
        return baseIndividual;
    }
    
    private String computeBaseUri() {
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
        return LOCAL_NAMESPACE + localName;
    }
    
    /**
     * Return the URI of the authority linked to this.baseIndividual by
     * the bf:hasAuthority property. For example, a bf:Person bf:hasAuthority
     * bf:Authority - return the URI of the bf:Authority individual.
     * @return
     */
    protected String getAuthorityResourceUri() {
        String authorityResourceUri = null;
        Property bfHasAuthorityProperty = 
                recordModel.getOntProperty(BF_HAS_AUTHORITY_URI);
        if (baseIndividual.hasProperty(bfHasAuthorityProperty)) {
            Resource authorityResource = baseIndividual.
                    getPropertyResourceValue(bfHasAuthorityProperty);
                    
            // TODO Should this be limited to external URIs, or should we 
            // include internal ones as well? Seems like the latter.
            authorityResourceUri = authorityResource.getURI();
        }
        return authorityResourceUri;
    }
    
    public void addRdfsLabel() {
        
        /* Setting the stage for subtypes to override the default assignment
         * or a previous rdfs:label value, by trying to compute a new rdfs:label
         * first, before assigning the default. In the case of an instance with
         * rdfs:label "Electronic Resource", we want to change the rdfs:label
         * to the title + " - Electronic Resource".
         */  
        assignDefaultRdfsLabel();
        if (! baseIndividual.hasProperty(RDFS.label)) {
            baseIndividual.addLiteral(RDFS.label, baseIndividual.getURI());
        }
    }
    
    protected void assignDefaultRdfsLabel() {
        
        if (! baseIndividual.hasProperty(RDFS.label)) {
            Property property = recordModel.getProperty(BF_LABEL_URI);
            RDFNode bfLabelNode = baseIndividual.getPropertyValue(property);
            if (bfLabelNode != null) {
                baseIndividual.addLiteral(RDFS.label, bfLabelNode.asLiteral());                 
            } 
        }
    }

    protected Literal getNewLiteral(Literal original, String newLexicalForm) {
        Literal newLiteral = null;
        String lang = original.getLanguage();
        if (lang == null) {
            newLiteral = recordModel.createLiteral(newLexicalForm);
        } else {
            newLiteral = recordModel.createLiteral(newLexicalForm, lang);
        }
        
        return newLiteral;
        
    }    
    
    /**
     * Link the baseIndividual (a bf:Authority, such as bf:Person, 
     * bf:Organization, etc. to a real world object.
     *
     * @param individual
     * @param RWO
     */
    // Note: if we need to link an Authority other than baseIndividual to an
    // RWO, pass in Individual authority as well.
    protected void linkAuthorityToRwo(Individual RWO) {
        
        Property authorityToRwoProperty = recordModel.getProperty(
                MADSRDF_IDENTIFIES_RWO_URI);
        
        Property RwoToAuthorityProperty = recordModel.getProperty(
                MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URI);
                
        RWO.addProperty(RwoToAuthorityProperty, baseIndividual);
        
        // Make the inverse assertion for ingest into systems that don't do
        // inverse inferencing. 
        /* NB if recordModel (or allRecords) were an inferencing OntModel, we
         * wouldn't need to make the inverse assertion. See notes in 
         * ModelPostProcessor.processRecords() and 
         * ModelPostProcessor.processRecord().
         */
         baseIndividual.addProperty(authorityToRwoProperty, RWO);

    }
    
    // Mint or create a foafUri for the foaf Individual related to the
    // baseIndividual.
    protected String getFoafUri() {
        return getFoafUri(baseUri);
    }
    
    // Mint or create a foafUri based on the given URI.
    protected String getFoafUri(String uri) {
        return uri + "foaf";
    }
    
    /** 
     * Create a foaf Jena Individual (not a foaf:Individual) of type foafClass
     * and related to the bf:Authority authority.
     * @param relatedIndividual
     * @param foafClass
     * @return
     */
//    protected Individual createFoafIndividual(Individual authority, 
//            Class foafClass) {
//        
//        // Mint a URI for the new foaf:Organization.
//        String foafOrganizationUri = getFoafUri();
//
//        // Create a foaf:Organization from the newly-
//        // minted URI. It will get added to allRecords at the
//        // end of processing the record.
//        Individual foafOrganization = recordModel.createIndividual(
//                foafOrganizationUri, foafOrganizationClass);
//        
//        // Add an rdfs:label to the foaf:Organization.
//        foafOrganization.addProperty(RDFS.label, objectLabel);
//        foafOrganization.addProperty(recordModel.getProperty(
//                FOAF_NAME_URI), objectLabel);
//
//        // Link the bf:Organization to the foaf:Organization.
//        linkAuthorityToRwo(foafOrganization);
//        
//        // Add an rdfs:label to the bf:Organization, while we're
//        // here and we have it.
//        objectIndividual.addProperty(RDFS.label, objectLabel);
//    }
    
    
}

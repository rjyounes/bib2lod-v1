package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.BF_HAS_AUTHORITY_PROPERTY;
import static org.ld4l.bib2lod.Constants.BF_LABEL_PROPERTY;
import static org.ld4l.bib2lod.Constants.BF_PERSON_CLASS;
import static org.ld4l.bib2lod.Constants.FOAF_NAME_PROPERTY;
import static org.ld4l.bib2lod.Constants.FOAF_PERSON_CLASS;
import static org.ld4l.bib2lod.Constants.MADSRDF_IDENTIFIES_RWO_PROPERTY;
import static org.ld4l.bib2lod.Constants.MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_PROPERTY;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Subclasses BfIndividual, a Decorator class on Jena Individual, to provide
 * functionality specific to dealing with bf:Person individuals.
 * @author rjy7
 *
 */

public class BfPerson extends BfIndividual  {
    
    protected Resource bfType = BF_PERSON_CLASS;    

    protected BfPerson(
            Individual relatedIndividual, Property property, String baseUri) {
        super(relatedIndividual, property, baseUri);
    }
    
    protected Literal cleanLabel(Literal bfPersonLabel) {
        // TODO Add transformations
        // Remove dates from label
        // E.g., <bf:label>Prokofiev, Sergey, 1891-1953.</bf:label>
        return bfPersonLabel; // TEMPORARY
    }    
    
    protected Individual createFoafPerson() {

        OntModel ontModel = baseIndividual.getOntModel();

        /* 
         * If the baseIndividual hasAuthority to an external URI, use that
         * instead of minting a URI.
         * If the authority is structured as follows:
         *
         *   <bf:hasAuthority>
         *       <madsrdf:Authority>
         *          <madsrdf:authoritativeLabel>
         *              Royer, Caisa Elizabeth.
         *          </madsrdf:authoritativeLabel>
         *       </madsrdf:Authority>
         *    </bf:hasAuthority>
         *   
         * instead of like this: 
         * 
         * <bf:hasAuthority 
         * rdf:resource="http://vivo.cornell.edu/individual/individual23258"/>
         * 
         * then the getURI() method returns null, which is what we want. That is 
         * case where we'll mint a new URI ending in "foaf".
         */
        String foafPersonUri = getAuthorityResourceUri();
        if (foafPersonUri == null) {
            foafPersonUri = baseUri + "foaf";
        }
        
        Individual foafPerson = 
                ontModel.createIndividual(foafPersonUri, FOAF_PERSON_CLASS);
        
        Literal bfCreatorLabel = 
                baseIndividual.getPropertyValue(BF_LABEL_PROPERTY).asLiteral();
        
        // TODO Remove dates from label
        Literal foafPersonName = cleanLabel(bfCreatorLabel);

        foafPerson.addProperty(FOAF_NAME_PROPERTY, foafPersonName);
        foafPerson.addProperty(RDFS.label, foafPersonName);
        
        foafPerson.addProperty(
                MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_PROPERTY, baseIndividual);
        
        // Make the inverse assertion for ingest into systems that don't do
        // inverse inferencing.
        baseIndividual.addProperty(MADSRDF_IDENTIFIES_RWO_PROPERTY, foafPerson);
        
        // Remove the bf:hasAuthority relationship to the foaf:Person, because
        // a foaf:Person is not an Authority.  We've just added the appropriate
        // relationship MADSRDF_IDENTIFIES_RWO_PROPERTY.
        Resource authority = baseIndividual.getPropertyResourceValue(
                BF_HAS_AUTHORITY_PROPERTY);
        if (authority != null && authority.getURI() == foafPerson.getURI()) {
            baseIndividual.removeProperty(BF_HAS_AUTHORITY_PROPERTY, authority);
        }
        
        
        return foafPerson;
    }

}

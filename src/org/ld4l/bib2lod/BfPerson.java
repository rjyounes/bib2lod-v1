package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.BF_LABEL_PROPERTY;
import static org.ld4l.bib2lod.Constants.BF_PERSON_CLASS;
import static org.ld4l.bib2lod.Constants.FOAF_NAME_PROPERTY;
import static org.ld4l.bib2lod.Constants.FOAF_PERSON_CLASS;
import static org.ld4l.bib2lod.Constants.MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_PROPERTY;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

public class BfPerson extends BfIndividual  {
    
    protected final Resource bfType = BF_PERSON_CLASS;    

    protected BfPerson(
            Individual relatedIndividual, Property property, String baseUri) {
        super(relatedIndividual, property, baseUri);
    }
    
    protected Literal cleanLabel(Literal bfPersonLabel) {
        // Remove dates from label
        // E.g., <bf:label>Prokofiev, Sergey, 1891-1953.</bf:label>
        return bfPersonLabel;
    }    
    
    protected Individual createFoafPerson() {

        OntModel ontModel = baseIndividual.getOntModel();
        
        // *** Doesn't work because local name loses the digits at the front.
        // need to add them back to the front of the new local name.
        // What's going on with this??
        
        // If the baseIndividual hasAuthority to an external URI, use
        // that instead of minting a URI.
        /* 
         * TODO Make sure that if the authority is:
        
        <bf:hasAuthority>
        <madsrdf:Authority>
           <madsrdf:authoritativeLabel>Royer, Caisa Elizabeth.</madsrdf:authoritativeLabel>
        </madsrdf:Authority>
     </bf:hasAuthority>
        instead of like this: 
        <bf:hasAuthority rdf:resource="http://vivo.cornell.edu/individual/individual23258"/>
        then the getURI() method returns null.
        */
        String foafPersonUri = getAuthorityResourceUri();
        if (foafPersonUri == null) {
            foafPersonUri = baseUri + "foaf";
        }
        
        Individual foafPerson = 
                ontModel.createIndividual(foafPersonUri, FOAF_PERSON_CLASS);
        
        Literal bfCreatorLabel = 
                (Literal) baseIndividual.getPropertyValue(BF_LABEL_PROPERTY);
        
        // TODO Remove dates from label
        Literal foafPersonName = cleanLabel(bfCreatorLabel);

        foafPerson.addLiteral(FOAF_NAME_PROPERTY, foafPersonName);
        foafPerson.addLiteral(RDFS.label, foafPersonName);
        
        // Add relation to bf and foaf persons: madsrdf:identifiesRWO.
        // TODO Check that the inverse is also put into Vitro.
        foafPerson.addProperty(
                MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_PROPERTY, baseIndividual);
        
        return foafPerson;
    }

}

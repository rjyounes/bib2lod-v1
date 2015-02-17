package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.*;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

public class BfPerson extends BfIndividual  {
    
    protected Resource bfType = BF_PERSON_CLASS;    

    protected BfPerson(
            Individual relatedIndividual, Property property, String baseUri) {
        super(relatedIndividual, property, baseUri);
    }
    
    protected String cleanLabel(String bfPersonLabel) {
        // TODO Add transformations
        // Remove dates from label
        // E.g., <bf:label>Prokofiev, Sergey, 1891-1953.</bf:label>
        return bfPersonLabel; // TEMPORARY
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
        
        String bfCreatorLabel = 
                //((Literal) baseIndividual.getPropertyValue(BF_LABEL_PROPERTY)).getLexicalForm();
                baseIndividual.getPropertyValue(BF_LABEL_PROPERTY).asLiteral().getLexicalForm();
        
        // TODO Remove dates from label
        String foafPersonName = cleanLabel(bfCreatorLabel);

        foafPerson.addProperty(FOAF_NAME_PROPERTY, foafPersonName);
        foafPerson.addProperty(RDFS.label, foafPersonName);
        
        foafPerson.addProperty(
                MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_PROPERTY, baseIndividual);
        
        // TODO Decide on whether to make the inverse assertion or not.
        // We shouldn't need this because it would come from inferencing. But 
        // we can add it explicitly anyway. What are the pros/cons? 
        // If we don't assert it, check that Vitro makes the inference.
        baseIndividual.addProperty(MADSRDF_IDENTIFIES_RWO_PROPERTY, foafPerson);
        
        return foafPerson;
    }

}

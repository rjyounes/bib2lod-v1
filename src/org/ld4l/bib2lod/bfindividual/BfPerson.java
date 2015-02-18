package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.Constants.*;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Subclasses BfIndividual, a Decorator class on Jena Individual, to provide
 * functionality specific to dealing with bf:Person individuals.
 * @author rjy7
 *
 */

public class BfPerson extends BfIndividual  { 

    protected BfPerson(Individual baseIndividual) {
        super(baseIndividual);
    }

    protected Literal cleanLabel(Literal bfPersonLabel) {
        // TODO Add transformations
        // Remove dates from label
        // E.g., <bf:label>Prokofiev, Sergey, 1891-1953.</bf:label>
        return bfPersonLabel; // TEMPORARY
    }    
    
    public Individual createFoafPerson() {

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
                ontModel.createIndividual(
                        foafPersonUri, ontModel.getProperty(FOAF_PERSON_URI));
        

        RDFNode bfCreatorNode = baseIndividual.getPropertyValue(
                ontModel.getProperty(BF_LABEL_URI));

        if (bfCreatorNode != null) {
            Literal bfCreatorLabel = bfCreatorNode.asLiteral();
            // Remove dates from label
            // TODO If the foaf:Person has an external namespace, we should get
            // this data from that resource. Requires a call out to that
            // namespace (e.g., to the SPARQL endpoint).
            Literal foafPersonName = cleanLabel(bfCreatorLabel);
            foafPerson.addProperty(ontModel.getProperty(FOAF_NAME_URI), foafPersonName);
            foafPerson.addProperty(RDFS.label, foafPersonName);
        }

        foafPerson.addProperty(ontModel.getProperty(
                MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URI), baseIndividual);
        
        // Make the inverse assertion for ingest into systems that don't do
        // inverse inferencing. 
        /* NB if recordModel (or allRecords) were an inferencing OntModel, we
         * wouldn't need to make the inverse assertion. See notes in 
         * ModelPostProcessor.processRecords() and 
         * ModelPostProcessor.processRecord().
         */
         baseIndividual.addProperty(ontModel.getProperty(
                 MADSRDF_IDENTIFIES_RWO_URI), foafPerson);
        
        // Remove the bf:hasAuthority relationship to the foaf:Person, because
        // a foaf:Person is not an Authority.  We've just added the appropriate
        // relationship MADSRDF_IDENTIFIES_RWO_PROPERTY.
        /* Do we also want to delete the bf:hasAuthority relation to a blank
         * node, and the blank node, since we now have a relation to a RWO? For 
         * example:
         *    <rdf:Description rdf:about="http://localhost:8080/vitro/individual/8793268person8">
         *        <rdf:type rdf:resource="http://bibframe.org/vocab/Person"/>
         *        <bf:label>Royer, Caisa Elizabeth.</bf:label>
         *        <bf:authorizedAccessPoint>Royer, Caisa Elizabeth.</bf:authorizedAccessPoint>
         *        <bf:hasAuthority rdf:nodeID="A0"/>
         *        <madsrdf:identifiesRWO rdf:resource="http://localhost:8080/vitro/individual/8793268person8foaf"/>
         *        <rdfs:label>Royer, Caisa Elizabeth.</rdfs:label>
         *    </rdf:Description>
         *    <rdf:Description rdf:nodeID="A0">
         *        <rdf:type rdf:resource="http://www.loc.gov/mads/rdf/v1#Authority"/>
         *        <madsrdf:authoritativeLabel>Royer, Caisa Elizabeth.</madsrdf:authoritativeLabel>
         *    </rdf:Description>
         */
        Property bfHasAuthorityProperty = 
                ontModel.getProperty(BF_HAS_AUTHORITY_URI);
        Resource authority = baseIndividual.getPropertyResourceValue(
                bfHasAuthorityProperty);
        if (authority != null && authority.getURI() == foafPerson.getURI()) {
            baseIndividual.removeProperty(bfHasAuthorityProperty, authority);
        }        
        
        return foafPerson;
    }

}

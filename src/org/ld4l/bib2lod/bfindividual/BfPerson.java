package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.postprocessor.Constants.BF_AUTHORIZED_ACCESS_POINT_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_HAS_AUTHORITY_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_LABEL_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.FOAF_NAME_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.FOAF_PERSON_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.MADSRDF_IDENTIFIES_RWO_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern FINAL_PERIOD_PATTERN = Pattern.compile("(?<!\\s[A-Z])\\.$");
    private static final Pattern DATE_PATTERN = Pattern.compile(",\\s[0-9].+$");
    
    protected BfPerson(Individual baseIndividual) {
        super(baseIndividual);
    }

    protected Literal cleanLabel(Literal bfPersonLabel) {

        String label = bfPersonLabel.getLexicalForm();
        String originalLabel = label;
        String lang = bfPersonLabel.getLanguage();

        Matcher matcher = FINAL_PERIOD_PATTERN.matcher(label);
        
        if (matcher.find()) {
            //Prokofiev, Sergey, 1891-1953.
            // Syrgkanis, Vasileios, 1986-
            label = matcher.replaceFirst("");
        }
        
        matcher = matcher.reset();
        matcher = DATE_PATTERN.matcher(label);
        if (matcher.find()) {
            // Meisburger, Stephen Paul.
            label = matcher.replaceFirst("");
        }

        if (label.equals(originalLabel)) {
            return bfPersonLabel;
        }
        
        Literal newLabel;
        if (lang != null) {
            newLabel = recordModel.createLiteral(label, lang);
        } else {
            newLabel = recordModel.createLiteral(label);
        }
        return newLabel; 
    }    
    /**
     * Create a foaf:Person from the bf:Authority, and link the two together.
     * @return
     */
    public Individual createFoafPerson() {

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
         * the case where we'll mint a new URI ending in "foaf".
         */
        // If the bf:Person has an authority resource with a URI, use that 
        // as the foaf:Person URI. Otherwise, create a new URI for the 
        // foaf:Person.
        String foafPersonUri = getAuthorityResourceUri();
        if (foafPersonUri == null) {
            foafPersonUri = getFoafUri();            
        }
        
        Individual foafPerson = 
                recordModel.createIndividual(
                        foafPersonUri, recordModel.getResource(FOAF_PERSON_URI));
        
        // TODO IMPORTANT need to pass in the property. Call it linkingProperty
        // here
        
        RDFNode bfCreatorNode = baseIndividual.getPropertyValue(
                recordModel.getProperty(BF_LABEL_URI));

          // All following should only be done if bfCreatorNode is not null
        if (bfCreatorNode != null) {
            Literal bfCreatorLabel = bfCreatorNode.asLiteral();
            // Remove dates from label
            // TODO If the foaf:Person has an external namespace, we should get
            // this data from that resource. Requires a call out to that
            // namespace (e.g., to the SPARQL endpoint).
            Literal foafPersonName = cleanLabel(bfCreatorLabel);
            foafPerson.addProperty(recordModel.getProperty(
                    FOAF_NAME_URI), foafPersonName);
            foafPerson.addProperty(RDFS.label, foafPersonName);
        }

        // Link the bf:Person to the foaf:Person.
        linkAuthorityToRwo(foafPerson);
        
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
        // TODO It's possible that we ALWAYS want to do this after we link the
        // foaf:Person to the baseIndividual (Authority). In that case, move to
        // linkAuthorityToRwo(). In the current thesis data, it only applies
        // to Persons.
        Property bfHasAuthorityProperty = 
                recordModel.getProperty(BF_HAS_AUTHORITY_URI);
        Resource authority = baseIndividual.getPropertyResourceValue(
                bfHasAuthorityProperty);
        if (authority != null && authority.getURI() == foafPerson.getURI()) {
            baseIndividual.removeProperty(bfHasAuthorityProperty, authority);
        }  
        
        return foafPerson;

    }
    

    public void addRdfsLabel() {
        
        assignDefaultRdfsLabel();

        // If no label was assigned in assignDefaultLabel()
        if (! baseIndividual.hasProperty(RDFS.label)) { 
            
            RDFNode authorizedAccessPoint = baseIndividual.getPropertyValue(
                    recordModel.getProperty(BF_AUTHORIZED_ACCESS_POINT_URI));
            if (authorizedAccessPoint != null) {
                baseIndividual.addLiteral(RDFS.label, authorizedAccessPoint.asLiteral());
            } else {
                super.addRdfsLabel();
            }
        }
    }
}

package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.postprocessor.Constants.BF_AUTHORIZED_ACCESS_POINT_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_HAS_AUTHORITY_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_LABEL_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_ORGANIZATION_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_PERSON_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.FOAF_NAME_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.FOAF_ORGANIZATION_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.FOAF_PERSON_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.MADSRDF_IDENTIFIES_RWO_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
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
         * then the getURI() method returns null. That is the case where we'll 
         * mint a new URI ending in "foaf".
         * 
         */
        
        /*
         * If the bf:Person has an authority resource with a URI, use that 
         * as the foaf:Person URI. Otherwise, create a new URI for the 
         * foaf:Person. This preserves URIs in external namespaces, such as 
         * VIVO.
         */
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
        
        /* Remove any bf:hasAuthority relationships this bf:Person has, since
         * we've just linked it to a foaf:Person and no longer need the 
         * authority. Do this whether the authority resource has a URI or is a
         * blank node. See the two possibilities in the Bibframe RDF:
         * 
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
         *    
         *    <bf:Person rdf:about="http://localhost:8080/ld4l-vitro/individual/8793268person9">
         *        <bf:label>Ceci, Stephen John</bf:label>
         *        <bf:authorizedAccessPoint>Ceci, Stephen John</bf:authorizedAccessPoint>
         *        <bf:hasAuthority rdf:resource="http://vivo.cornell.edu/individual/individual23258"/>
         *    </bf:Person>
         *    
         * TODO In the current thesis data, this only applies to Persons. It's
         * possible that we should ALWAYS to this after we link a foaf 
         * Individual to the baseIndividual (Authority). In that case, move to
         * linkAuthorityToRWO().
         */

        Property bfHasAuthorityProperty = 
                recordModel.getProperty(BF_HAS_AUTHORITY_URI);
        Resource authority = baseIndividual.getPropertyResourceValue(
                bfHasAuthorityProperty);
        // if (authority.getURI().equals(foafPersonUri) {
        //     baseIndividual.removeProperty(bfHasAuthorityProperty, authority);
        // }
        // If authority is a blank node, we should remove it, but it's not
        // clear how. Is this going to be a problem?
        baseIndividual.removeProperty(bfHasAuthorityProperty, authority);
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

    /** Create or re-use a foaf:Person based on the workToPerson property.
     * 
     * For example, create a foaf:Person based on the bf:Person that is linked
     * to the bfWork via the workToPerson property. 
     * property.
     * 
     * Requires deduping of persons in the union Model. We do this by finding
     * the object of the baseIndividual's hasAuthority property, then 
     * looking for this individual in the union Model.
     * TODO We generalize this by parameterizing the deduping property. Is that
     * useful?
     * 
     * TODO To further generalize with the BfOrganization dissertation
     * institution deduping, which dedupes on bf:label, need to handle both
     * datatype and object properties as the deduping criterion.
     */
    public Individual createFoafPerson(Individual bfWork, Property workToPerson,
            OntModel allRecords) {

        Individual foafPerson = null;
        
        // Get the bf:hasAuthority property, which will be used for deduping
        // the bf:Persons.
        Property bfHasAuthorityProperty = 
                recordModel.getProperty(BF_HAS_AUTHORITY_URI);

        // Get the foaf:Person class
        Resource foafPersonClass = 
                recordModel.getResource(FOAF_PERSON_URI);  
        
        // Get the baseIndividual's value of the bf:hasAuthority property. This
        // will be used to dedupe bf:Persons.
        Resource foafPersonResource =
                baseIndividual.getPropertyResourceValue(bfHasAuthorityProperty);       
        String foafPersonUri = foafPersonResource.getURI();

        // Determine whether another Individual with this URI already exists
        // in the union Model.
        foafPerson = allRecords.getIndividual(foafPersonUri);
        
        if (foafPerson != null) {
            
            // Find the bf:Person that is linked to this foaf:Person.
            Property isIdentifiedByAuthorityProperty = allRecords.getProperty(
                    MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URI);

            Resource bfPersonResource = foafPerson.getPropertyResourceValue(
                    isIdentifiedByAuthorityProperty);
            String bfPersonUri = bfPersonResource.getURI();
            Individual bfPersonIndividual = 
                    allRecords.getIndividual(bfPersonUri);
            
            // Link the bfWork to this bf:Person via the workToPerson property.
            bfWork.addProperty(workToPerson, bfPersonResource);
            
            // Remove the baseIndividual from the recordModel.
            baseIndividual.remove();
            baseIndividual = bfPersonIndividual;
            
        } else {
            foafPerson = createFoafIndividual(foafPersonClass, foafPersonUri);
            // Remove the hasAuthority relation between the bf:Person and the
            // foaf:Person, since they are now linked by 
            // madsrdf:identifiesRWO and madsrdf:isIdentifiedByAuthority.
            baseIndividual.removeProperty(bfHasAuthorityProperty, foafPerson);           
        }

        return foafPerson;
        
    }
}

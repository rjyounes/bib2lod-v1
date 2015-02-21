package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.postprocessor.Constants.BF_LABEL_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.BF_ORGANIZATION_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.FOAF_NAME_URI;
import static org.ld4l.bib2lod.postprocessor.Constants.FOAF_ORGANIZATION_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class BfOrganization extends BfIndividual {

    protected BfOrganization(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    // public void addRdfsLabel() {
    //     Uses bf:label, so just use superclass method.
    // }
    
    /** Create or re-use a foaf:Organization based on the linkingProperty.
     * 
     * For example, create a foaf:Organization based on the bf:Organization
     * that is linked to the bfWork via the bf:dissertationInstitution
     * property.
     * 
     * Requires deduping of organizations in the union Model.
     */
    public Individual createFoafOrganization(Individual bfWork, Property 
            linkingProperty, OntModel allRecords) {
        
        Individual foafOrganization = null;

        // Get the bf:label property
        Property bfLabelProperty = recordModel.getProperty(BF_LABEL_URI); 
        
        // Get the bf:Organization class
        Resource bfOrganizationClass = 
                recordModel.getResource(BF_ORGANIZATION_URI);  
        
        // Get the foaf:Organization class
        Resource foafOrganizationClass = 
                recordModel.getResource(FOAF_ORGANIZATION_URI);
        
        // Get the bfWork's object of the linkingProperty
        Resource objectResource = bfWork.
                getPropertyResourceValue(linkingProperty);
        
        // If there's no object Resource (shouldn't happen) we'll return null.
        if (objectResource != null) {
       
            // Create an Individual from this Resource.
            String resourceUri = objectResource.getURI();
            Individual objectIndividual = 
                    recordModel.getIndividual(resourceUri);

            // Get the bf:label of the object
            RDFNode objectLabelNode = 
                    objectIndividual.getPropertyValue(bfLabelProperty); 

            // Get the object's label.
            if (objectLabelNode != null) {

                String objectLabel = 
                        objectLabelNode.asLiteral().getLexicalForm();
                // Determine whether this institution has been seen before,
                // based on bf:label matching.
                // NB This is a vastly simplified version of deduping 
                // institutions that works for the thesis data at hand.
                
                // Iterate through the bf:Organizations.
                ExtendedIterator<Individual> bfOrganizations = 
                        allRecords.listIndividuals(bfOrganizationClass);
                
                // Iterate through organizations already in the union Model,
                // and compare their labels to the current organization's
                // label.
                while (bfOrganizations.hasNext()) {

                    Individual bfOrganization = bfOrganizations.next();

                    RDFNode organizationLabelNode = 
                            bfOrganization.getPropertyValue(bfLabelProperty);

                    if (organizationLabelNode != null) {
                        String organizationLabel = organizationLabelNode.
                                asLiteral().getLexicalForm();
                              
                        // If the current organization has the same label as
                        // the existing organization, assume they are the same
                        // organization.
                        if (objectLabel.equals(organizationLabel)) {

                            // Find the related foaf:Organization to return.
                            String foafOrganizationUri = 
                                    getFoafUri(bfOrganization.getURI());
                            foafOrganization = allRecords.getIndividual(
                                    foafOrganizationUri);
                            
                            // Remove the current organization from the 
                            // recordModel.
                            objectIndividual.remove();

                            // Link the bfWork to the existing organization 
                            // instead.
                            bfWork.addProperty(linkingProperty, bfOrganization);
                            
                            // No need to continue iterating through 
                            // organizations. Return the existing 
                            // foaf:Organization already linked to the 
                            // bf:Organization.
                            // TODO Should probably check to make sure there is
                            // one, though we definitely should have created it
                            // the first time we saw this bf:Organization.
                            break;
                        } 
                    }                   
                } // end while loop

                // TODO Put this part in separate function; pass in 
                // objectIndividual.
                // If no foafOrganization, create a new one and link it to the
                // current bf:Organization as well as to the bfWork. Two cases: 
                // (1) No match to an existing organization was found.
                // (2) There are no existing organizations.
                if (foafOrganization == null) {
                    // Mint a URI for the new foaf:Organization.
                    String foafOrganizationUri = getFoafUri();

                    // Create a foaf:Organization from the newly-
                    // minted URI. It will get added to allRecords at the
                    // end of processing the record.
                    foafOrganization = recordModel.createIndividual(
                            foafOrganizationUri, foafOrganizationClass);
                    
                    // Add an rdfs:label to the foaf:Organization.
                    foafOrganization.addProperty(RDFS.label, objectLabel);
                    foafOrganization.addProperty(recordModel.getProperty(
                            FOAF_NAME_URI), objectLabel);
         
                    // Link the bf:Organization to the foaf:Organization.
                    linkAuthorityToRwo(foafOrganization);
                    
                    // Add an rdfs:label to the bf:Organization, while we're
                    // here and we have it.
                    objectIndividual.addProperty(RDFS.label, objectLabel);
                    
                } // end creating new foaf:Organization

            } // end non-null object label
            
        } // end no object Resource
        
        return foafOrganization;
    }  
    
//    protected Individual createFoafOrganization(Individual bfIndividual) {
//        
//    }

}

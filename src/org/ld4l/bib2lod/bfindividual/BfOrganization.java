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
        
        // Get the bf:label of the object
        RDFNode baseIndividualLabelNode = 
                baseIndividual.getPropertyValue(bfLabelProperty); 

        // Get the object's label.
        if (baseIndividualLabelNode != null) {

            String baseIndividualLabel = 
                    baseIndividualLabelNode.asLiteral().getLexicalForm();
            
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
                    if (baseIndividualLabel.equals(organizationLabel)) {

                        // Find the related foaf:Organization to return.
                        String foafOrganizationUri = 
                                getFoafUri(bfOrganization.getURI());
                        foafOrganization = allRecords.getIndividual(
                                foafOrganizationUri);
                        
                        // Remove the current organization from the 
                        // recordModel.
                        // NB It's odd to remove or change the baseIndividual 
                        // of this BfIndividual. However, if we instead remove 
                        // the bfOrganization in allRecords, there's a lot more
                        // manipulation involved: remove original foaf: and
                        // bf:Organizations from recordModel and allRecords,
                        // link other works to the new organization, etc. 
                        baseIndividual.remove();  
                        // Not really needed, but seems better to have a 
                        // baseIndividual.
                        baseIndividual = bfOrganization;

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
            // (2) There are no prior existing organizations in the data.
            if (foafOrganization == null) {
                
                foafOrganization = createFoafIndividual(foafOrganizationClass);                      
                
            } // end creating new foaf:Organization

        } // end non-null object label
       
        return foafOrganization;
    }  
    


}

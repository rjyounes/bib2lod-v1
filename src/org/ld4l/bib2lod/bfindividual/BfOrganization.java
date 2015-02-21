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
    
    /** 
     * Create or re-use a foaf:Organization based on the linkingProperty. The
     * baseIndividual is linked to the bfWork via this property.
     * 
     * For example, create or re-use a foaf:Organization based on the 
     * bf:Organization linked to the bfWork via the bf:dissertationInstitution
     * property.
     * 
     * Requires deduping of organizations in the union Model. For now the 
     * deduping is vastly oversimplified and based on the limited thesis data
     * set. Needs to made much more sophisticated.
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
        
        // Get the bf:label of the baseIndividual, which is used to dedupe
        // the bf:Organizations.
        RDFNode baseIndividualLabelNode = 
                baseIndividual.getPropertyValue(bfLabelProperty); 

        if (baseIndividualLabelNode != null) {

            String baseIndividualLabel = 
                    baseIndividualLabelNode.asLiteral().getLexicalForm();
            
            // Determine whether this institution already exists in the data 
            // set, based on bf:label matching.
            // NB This is a vastly simplified version of deduping 
            // institutions that works for the thesis data at hand. Also, in
            // the real world we'd need to check against existing data that's
            // already been read into a designated triplestore, if one exists.
            // TODO Move to separate method BfOrganization.dedupe().
            // TODO Could also try combining with BfPerson deduping, the
            // matching criteria work differently.
            
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

                        // Find the related foaf:Organization to return, based
                        // on the URI that would have been minted when it was
                        // created.
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

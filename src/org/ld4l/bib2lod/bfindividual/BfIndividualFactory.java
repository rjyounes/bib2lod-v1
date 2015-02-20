package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.Constants.BF_IDENTIFIER_URI;
import static org.ld4l.bib2lod.Constants.BF_INSTANCE_URI;
import static org.ld4l.bib2lod.Constants.BF_PERSON_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_URI;
import static org.ld4l.bib2lod.Constants.BF_WORK_URI;
import static org.ld4l.bib2lod.Constants.MADSRDF_AUTHORITY_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class BfIndividualFactory {
    
    public static BfIndividual createBfIndividual(Individual baseIndividual) {       

        BfIndividual bfIndividual;
        
        if (baseIndividual.hasRDFType(BF_PERSON_URI)) {
            bfIndividual = new BfPerson(baseIndividual);
        } else if (baseIndividual.hasRDFType(BF_INSTANCE_URI)) {
            bfIndividual = new BfInstance(baseIndividual); 
        } else if (baseIndividual.hasRDFType(BF_WORK_URI)) {
            bfIndividual = new BfWork(baseIndividual);     
        } else if (baseIndividual.hasRDFType(BF_TITLE_URI)) {
            bfIndividual = new BfTitle(baseIndividual);
        } else if (baseIndividual.hasRDFType(BF_IDENTIFIER_URI)) {
            bfIndividual = new BfIdentifier(baseIndividual);
        } else if (baseIndividual.hasRDFType(MADSRDF_AUTHORITY_URI)) {
            bfIndividual = new MadsRdfAuthority(baseIndividual);
        } else { 
            bfIndividual = new BfIndividual(baseIndividual);
        }
    
        return bfIndividual;        
    }
        
    /**
     * Return a BfIndividual based on the baseIndividual which is the object of
     * the specified subject and property.
     * TODO Do we need to deal with the case where there's more than one such 
     * object, or will that not arise in this context?
     * @param subject
     * @param property
     * @return
     */
    public static BfIndividual createBfObjectIndividual(Individual subject, Property property) {
        
        OntModel ontModel = subject.getOntModel();
        
        // Get the base Individual from the model, based on the subject and 
        // property.
        Resource objectResource = 
                subject.getPropertyResourceValue(property);          
        String resourceUri = objectResource.getURI();
        Individual object = ontModel.getIndividual(resourceUri);
        
        return createBfIndividual(object);
    }

}

package org.ld4l.bib2lod;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 * Define constants for the package
 * 
 * TODO Get rid of this. Store the ontology files in the application (or point 
 * to an external directory where the ontology files reside). Read in the 
 * ontology files on application startup and store them in the main OntModel. 
 * This would be much cleaner, clearer, and more manageable than this list of 
 * constants.
 */
public class Constants {
    
    /* LD4L */
    protected static final String LD4L_CORE_NS = "http://ld4l.org/ontology/core#";
    

    /* BIBFRAME */  
    protected static final String BIBFRAME_NS = "http://bibframe.org/vocab/";
    
    /* BIBFRAME classes */    
    protected static final String BF_WORK_URI = BIBFRAME_NS + "Work";
    protected static final Resource BF_WORK_CLASS = 
            ResourceFactory.createResource(BF_WORK_URI);
    

    protected static final String BF_PERSON_URI = BIBFRAME_NS + "Person";
    protected static final Resource BF_PERSON_CLASS = 
            ResourceFactory.createResource(BF_PERSON_URI);
    
    /* BIBFRAME object properties */    
    protected static final String BF_CREATOR_URI  = BIBFRAME_NS + "creator";
    protected static final Property BF_CREATOR_PROPERTY = 
            ResourceFactory.createProperty(BF_CREATOR_URI);

    protected static final String BF_HAS_AUTHORITY_URI  = BIBFRAME_NS + "hasAuthority";
    protected static final Property BF_HAS_AUTHORITY_PROPERTY = 
            ResourceFactory.createProperty(BF_HAS_AUTHORITY_URI);
            
    
    /* BIBFRAME datatype properties */   
    protected static final String BF_LABEL_URI = BIBFRAME_NS + "label";
    protected static final Property BF_LABEL_PROPERTY = 
            ResourceFactory.createProperty(BF_LABEL_URI);
    
    
    
    /* FOAF */
    protected static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
    
    /* FOAF classes */
    protected static final String FOAF_PERSON_URI = FOAF_NS + "Person";
    protected static final Resource FOAF_PERSON_CLASS = 
            ResourceFactory.createResource(FOAF_PERSON_URI);
    
    /* FOAF datatype properties */
    protected static final String FOAF_NAME_URI = FOAF_NS + "name";
    protected static final Property FOAF_NAME_PROPERTY = 
            ResourceFactory.createProperty(FOAF_NAME_URI);
    
    
    
    /* MADSRDF */
    protected static final String MADSRDF_NS = 
            "http://www.loc.gov/mads/rdf/v1#";
    
    /* MADSRDF object properties */
    protected static final String MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URL = 
            MADSRDF_NS + "isIdentifiedByAuthority";
    protected static final Property MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_PROPERTY = 
            ResourceFactory.createProperty(MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URL);
    
    protected static final String MADSRDF_IDENTIFIES_RWO_URI = 
            MADSRDF_NS + "identifiesRWO";
    protected static final Property MADSRDF_IDENTIFIES_RWO_PROPERTY = 
            ResourceFactory.createProperty(MADSRDF_IDENTIFIES_RWO_URI);
    
}
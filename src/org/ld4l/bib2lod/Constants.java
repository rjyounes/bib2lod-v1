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
    
    protected static final String LD4L_THESIS_URI = LD4L_CORE_NS + "Thesis";
    protected static final Resource LD4L_THESIS_CLASS = 
            ResourceFactory.createResource(LD4L_THESIS_URI);
    
    /* BIBFRAME */  
    protected static final String BIBFRAME_NS = "http://bibframe.org/vocab/";
    
    /* BIBFRAME classes */    
    protected static final String BF_RESOURCE_URI = BIBFRAME_NS + "Resource";
    protected static final Resource BF_RESOURCE_CLASS = 
            ResourceFactory.createResource(BF_RESOURCE_URI);
    
    protected static final String BF_WORK_URI = BIBFRAME_NS + "Work";
    protected static final Resource BF_WORK_CLASS = 
            ResourceFactory.createResource(BF_WORK_URI);
    
    protected static final String BF_INSTANCE_URI = BIBFRAME_NS + "Instance";
    protected static final Resource BF_INSTANCE_CLASS = 
            ResourceFactory.createResource(BF_INSTANCE_URI);

    protected static final String BF_TITLE_URI = BIBFRAME_NS + "Title";
    protected static final Resource BF_TITLE_CLASS = 
            ResourceFactory.createResource(BF_TITLE_URI);
    
    protected static final String BF_IDENTIFIER_URI = 
            BIBFRAME_NS + "Identifier";
    protected static final Resource BF_IDENTIFIER_CLASS = 
            ResourceFactory.createResource(BF_IDENTIFIER_URI);    
    
    protected static final String BF_PERSON_URI = BIBFRAME_NS + "Person";
    protected static final Resource BF_PERSON_CLASS = 
            ResourceFactory.createResource(BF_PERSON_URI);
    
    /* BIBFRAME object properties */    
    protected static final String BF_CREATOR_URI  = BIBFRAME_NS + "creator";
    protected static final Property BF_CREATOR_PROPERTY = 
            ResourceFactory.createProperty(BF_CREATOR_URI);
    
    protected static final String BF_WORK_TITLE_URI  = 
            BIBFRAME_NS + "workTitle";
    protected static final Property BF_WORK_TITLE_PROPERTY = 
            ResourceFactory.createProperty(BF_WORK_TITLE_URI);
    
    protected static final String BF_INSTANCE_TITLE_URI  = 
            BIBFRAME_NS + "instanceTitle";
    protected static final Property BF_INSTANCE_TITLE_PROPERTY = 
            ResourceFactory.createProperty(BF_INSTANCE_TITLE_URI);

    protected static final String BF_HAS_AUTHORITY_URI = 
            BIBFRAME_NS + "hasAuthority";
    protected static final Property BF_HAS_AUTHORITY_PROPERTY = 
            ResourceFactory.createProperty(BF_HAS_AUTHORITY_URI);
            
    
    /* BIBFRAME datatype properties */   
    protected static final String BF_LABEL_URI = BIBFRAME_NS + "label";
    protected static final Property BF_LABEL_PROPERTY = 
            ResourceFactory.createProperty(BF_LABEL_URI);

    protected static final String BF_TITLE_PROPERTY_URI = 
            BIBFRAME_NS + "title";
    protected static final Property BF_TITLE_PROPERTY = 
            ResourceFactory.createProperty(BF_TITLE_PROPERTY_URI);
    
    protected static final String BF_TITLE_VALUE_URI = 
            BIBFRAME_NS + "titleValue";
    protected static final Property BF_TITLE_VALUE_PROPERTY = 
            ResourceFactory.createProperty(BF_TITLE_VALUE_URI);  
    
    protected static final String BF_TITLE_STATEMENT_URI = 
            BIBFRAME_NS + "titleStatement";
    protected static final Property BF_TITLE_STATEMENT_PROPERTY = 
            ResourceFactory.createProperty(BF_TITLE_STATEMENT_URI); 

    protected static final String BF_SUBTITLE_URI = 
            BIBFRAME_NS + "subtitle";
    protected static final Property BF_SUBTITLE_PROPERTY = 
            ResourceFactory.createProperty(BF_SUBTITLE_URI); 
    
    protected static final String BF_IDENTIFIER_VALUE_URI = 
            BIBFRAME_NS + "identifierValue";
    protected static final Property BF_IDENTIFIER_VALUE_PROPERTY = 
            ResourceFactory.createProperty(BF_IDENTIFIER_VALUE_URI);    
    
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
    
    /* MADSRDF classes */
    protected static final String MADSRDFS_AUTHORITY_URI = MADSRDF_NS + "Authority";
    protected static final Resource MADSRDFS_AUTHORITY_CLASS = 
            ResourceFactory.createResource(MADSRDFS_AUTHORITY_URI);
    
    /* MADSRDF object properties */
    protected static final String MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URL = 
            MADSRDF_NS + "isIdentifiedByAuthority";
    protected static final Property MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_PROPERTY = 
            ResourceFactory.createProperty(MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URL);
    
    protected static final String MADSRDF_IDENTIFIES_RWO_URI = 
            MADSRDF_NS + "identifiesRWO";
    protected static final Property MADSRDF_IDENTIFIES_RWO_PROPERTY = 
            ResourceFactory.createProperty(MADSRDF_IDENTIFIES_RWO_URI);
    
    protected static final String MADSRDF_AUTHORITATIVE_LABEL_URI = 
            MADSRDF_NS + "authoritativeLabel";
    protected static final Property MADSRDF_AUTHORITATIVE_LABEL_PROPERTY = 
            ResourceFactory.createProperty(MADSRDF_AUTHORITATIVE_LABEL_URI);
    
    
    /* RELATORS */
    protected static final String RELATORS_NS = 
            "http://id.loc.gov/vocabulary/relators/";
    
    /* Relators object properties */
    protected static final String RELATORS_THS_URI =
            "http://id.loc.gov/vocabulary/relators/ths";
    protected static final Property RELATORS_THS_PROPERTY = 
            ResourceFactory.createProperty(RELATORS_THS_URI);
    
}

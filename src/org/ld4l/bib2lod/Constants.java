package org.ld4l.bib2lod;

/**
 * Define constants for the package; mostly URIs for ontology resources.
 */
public class Constants {
    
    /* LD4L */
    protected static final String LD4L_CORE_NS = "http://ld4l.org/ontology/core#";
    
    protected static final String LD4L_THESIS_URI = LD4L_CORE_NS + "Thesis";
    
    /* BIBFRAME */  
    protected static final String BIBFRAME_NS = "http://bibframe.org/vocab/";
    
    /* BIBFRAME classes */    
    protected static final String BF_RESOURCE_URI = BIBFRAME_NS + "Resource";    
    protected static final String BF_WORK_URI = BIBFRAME_NS + "Work";    
    protected static final String BF_INSTANCE_URI = BIBFRAME_NS + "Instance";
    protected static final String BF_TITLE_URI = BIBFRAME_NS + "Title";    
    protected static final String BF_IDENTIFIER_URI = 
            BIBFRAME_NS + "Identifier";
    protected static final String BF_PERSON_URI = BIBFRAME_NS + "Person";

    
    /* BIBFRAME object properties */    
    protected static final String BF_CREATOR_URI  = BIBFRAME_NS + "creator"; 
    protected static final String BF_WORK_TITLE_URI  = 
            BIBFRAME_NS + "workTitle";   
    protected static final String BF_INSTANCE_TITLE_URI  = 
            BIBFRAME_NS + "instanceTitle";
    protected static final String BF_HAS_AUTHORITY_URI = 
            BIBFRAME_NS + "hasAuthority";

                
    /* BIBFRAME datatype properties */   
    protected static final String BF_LABEL_URI = BIBFRAME_NS + "label";
    protected static final String BF_TITLE_PROPERTY_URI = BIBFRAME_NS + "title";                
    protected static final String BF_TITLE_VALUE_URI = 
            BIBFRAME_NS + "titleValue";               
    protected static final String BF_TITLE_STATEMENT_URI = 
            BIBFRAME_NS + "titleStatement";
    protected static final String BF_SUBTITLE_URI = BIBFRAME_NS + "subtitle"; 
    protected static final String BF_IDENTIFIER_VALUE_URI = 
            BIBFRAME_NS + "identifierValue";
   
    
    /* FOAF */
    protected static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
    
    /* FOAF classes */
    protected static final String FOAF_PERSON_URI = FOAF_NS + "Person";

    
    /* FOAF datatype properties */
    protected static final String FOAF_NAME_URI = FOAF_NS + "name";

    
 
    /* MADSRDF */
    protected static final String MADSRDF_NS = 
            "http://www.loc.gov/mads/rdf/v1#";
    
    /* MADSRDF classes */
    protected static final String MADSRDFS_AUTHORITY_URI = MADSRDF_NS + "Authority";

    
    /* MADSRDF object properties */
    protected static final String MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URI = 
            MADSRDF_NS + "isIdentifiedByAuthority";    
    protected static final String MADSRDF_IDENTIFIES_RWO_URI = 
            MADSRDF_NS + "identifiesRWO";   
    protected static final String MADSRDF_AUTHORITATIVE_LABEL_URI = 
            MADSRDF_NS + "authoritativeLabel";

    
    
    /* RELATORS */
    protected static final String RELATORS_NS = 
            "http://id.loc.gov/vocabulary/relators/";
    
    /* Relators object properties */
    protected static final String RELATORS_THS_URI =
            "http://id.loc.gov/vocabulary/relators/ths";

    
}

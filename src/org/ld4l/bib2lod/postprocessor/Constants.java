package org.ld4l.bib2lod.postprocessor;

/**
 * Define constants for the package; mostly URIs for ontology resources.
 */
public class Constants {
    
    /* LD4L */
    public static final String LD4L_CORE_NS = "http://ld4l.org/ontology/core#";
    
    public static final String LD4L_THESIS_URI = LD4L_CORE_NS + "Thesis";
    
    public static final String LD4L_EXT_NS = "http://ld4l.org/ontology/ext#";
    public static final String LD4L_AUTHOR_OF_URI = LD4L_EXT_NS + "authorOf";

    
    /* BIBFRAME */  
    public static final String BIBFRAME_NS = "http://bibframe.org/vocab/";
    
    /* BIBFRAME classes */    
    public static final String BF_RESOURCE_URI = BIBFRAME_NS + "Resource";    
    public static final String BF_WORK_URI = BIBFRAME_NS + "Work";    
    public static final String BF_INSTANCE_URI = BIBFRAME_NS + "Instance";
    public static final String BF_TITLE_URI = BIBFRAME_NS + "Title";    
    public static final String BF_IDENTIFIER_URI = 
            BIBFRAME_NS + "Identifier";
    public static final String BF_PERSON_URI = BIBFRAME_NS + "Person";
    public static final String BF_ELECTRONIC_URI = BIBFRAME_NS + "Electronic";

    
    /* BIBFRAME object properties */    
    public static final String BF_CREATOR_URI  = BIBFRAME_NS + "creator"; 
    public static final String BF_WORK_TITLE_URI  = 
            BIBFRAME_NS + "workTitle";   
    public static final String BF_INSTANCE_TITLE_URI  = 
            BIBFRAME_NS + "instanceTitle";
    public static final String BF_HAS_AUTHORITY_URI = 
            BIBFRAME_NS + "hasAuthority";
    public static final String BF_HAS_INSTANCE_URI = 
            BIBFRAME_NS + "hasInstance";
    public static final String BF_INSTANCE_OF_URI = 
            BIBFRAME_NS + "instanceOf";
    
                
    /* BIBFRAME datatype properties */   
    public static final String BF_LABEL_URI = BIBFRAME_NS + "label";
    public static final String BF_TITLE_PROPERTY_URI = BIBFRAME_NS + "title";                
    public static final String BF_TITLE_VALUE_URI = 
            BIBFRAME_NS + "titleValue";               
    public static final String BF_TITLE_STATEMENT_URI = 
            BIBFRAME_NS + "titleStatement";
    public static final String BF_SUBTITLE_URI = BIBFRAME_NS + "subtitle"; 
    public static final String BF_IDENTIFIER_VALUE_URI = 
            BIBFRAME_NS + "identifierValue";
    public static final String BF_AUTHORIZED_ACCESS_POINT_URI = 
            BIBFRAME_NS + "authorizedAccessPoint";
    
    
    /* FOAF */
    public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
    
    /* FOAF classes */
    public static final String FOAF_PERSON_URI = FOAF_NS + "Person";

    
    /* FOAF datatype properties */
    public static final String FOAF_NAME_URI = FOAF_NS + "name";

    
 
    /* MADSRDF */
    public static final String MADSRDF_NS = 
            "http://www.loc.gov/mads/rdf/v1#";
    
    /* MADSRDF classes */
    public static final String MADSRDF_AUTHORITY_URI = MADSRDF_NS + "Authority";

    
    /* MADSRDF object properties */
    public static final String MADSRDF_IS_IDENTIFIED_BY_AUTHORITY_URI = 
            MADSRDF_NS + "isIdentifiedByAuthority";    
    public static final String MADSRDF_IDENTIFIES_RWO_URI = 
            MADSRDF_NS + "identifiesRWO";   
    public static final String MADSRDF_AUTHORITATIVE_LABEL_URI = 
            MADSRDF_NS + "authoritativeLabel";

    
    
    /* RELATORS */
    public static final String RELATORS_NS = 
            "http://id.loc.gov/vocabulary/relators/";
    
    /* Relators object properties */
    public static final String RELATORS_THS_URI =
            "http://id.loc.gov/vocabulary/relators/ths";

    
    /* PAV */
    public static final String PAV_NS = "http://purl.org/pav/";
    
    public static final String PAV_AUTHORED_BY_URI = PAV_NS + "authoredBy";
            
    
    
}

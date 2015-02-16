package org.ld4l.bib2lod;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 * Define constants for the package
 * 
 * TODO Consider whether we should store the ontology files in the application
 * (or point to an external directory as either a command line or config option, 
 * and read in the actual ontologies rather than manipulating their string 
 * values. It's not clear now whether this would facilitate referencing classes 
 * and properties within those ontologies or not. We still have to locate the 
 * classes and properties by URI, so we'd still have to construct the URIs here.
 */
public class Constants {
    
    /* LD4L */
    protected static final String LD4L_CORE_NS = "http://ld4l.org/ontology/core";

    /* BIBFRAME */  
    protected static final String BIBFRAME_NS = "http://bibframe.org/vocab/";
    
    /* BIBFRAME classes */    
    protected static final String BF_WORK_URI = BIBFRAME_NS + "Work";
    protected static final Resource BF_WORK_CLASS = 
            ResourceFactory.createResource(BF_WORK_URI);
    
    
    /* BIBFRAME object properties */    
    protected static final String BF_CREATOR_URI  = BIBFRAME_NS + "creator";
    protected static final Property BF_CREATOR_PROPERTY = 
            ResourceFactory.createProperty(BF_CREATOR_URI);
    
    
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
    
}

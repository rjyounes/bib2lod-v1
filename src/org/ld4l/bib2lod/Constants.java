package org.ld4l.bib2lod;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 * Define constants for the package
 */
public class Constants {

    protected static final String BIBFRAME_NS = "http://bibframe.org/vocab/";
    
    /* BIBFRAME classes */
    
    protected static final String BFWORK_URI = BIBFRAME_NS + "Work";
    protected static final Resource BFWORK_TYPE = 
            ResourceFactory.createResource(BFWORK_URI);
    
    
    /* BIBFRAME object properties */
    
    protected static final String BFCREATOR_URI  = BIBFRAME_NS + "creator";
    protected static final Property BFCREATOR_PROPERTY = 
            ResourceFactory.createProperty(BFCREATOR_URI);
    
    
    /* BIBFRAME datatype properties */
    
    protected static final String BFLABEL_URI = BIBFRAME_NS + "label";
    protected static final Property BFLABEL_PROPERTY = 
            ResourceFactory.createProperty(BFLABEL_URI);
    
}

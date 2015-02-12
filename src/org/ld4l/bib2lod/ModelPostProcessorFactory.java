package org.ld4l.bib2lod;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * Creates a ModelPostProcessor.
 * @author rjy7
 *
 */
class ModelPostProcessorFactory  {
    
    /**
     * Read an RDF file into a new OntModel and return the OntModel.
     * @param infileName
     * @return
     */
    protected static ModelPostProcessor createModelPostProcessor(String infileName) {       
        // TODO We're side-stepping the question of how to know what type of
        // preprocessor to create. For now, just define thesis post-processing,
        // since the current data consists only of thesis records. Later must
        // expand to other types of records. When we have mixed data, we'll need 
        // to test for things like: the bf:Work has a relators:ths, 
        // bf:dissertationYear, bf:dissertationDegree, 
        // bf:dissertationInstitution. The bf:Instance 
        // has a system number prefixed with "CUThesis."
        // *** Does this mean it would be better to process one record at a 
        // time, so we can figure out its type? Then pass a directory of
        // records rather than a single file into the application.
        return new ThesisModelPostProcessor(infileName);
    }
       
    protected static ModelPostProcessor createModelPostProcessor(OntModel ontModel) {        
        // TODO We're side-stepping the question of how to know what type of
        // preprocessor to create. For now, just define thesis post-processing,
        // since the current data consists only of thesis records. Later must
        // expand to other types of records. When we have mixed data, we'll need 
        // to test for things like: the bf:Work has a relators:ths, 
        // bf:dissertationYear, bf:dissertationDegree, 
        // bf:dissertationInstitution. The bf:Instance 
        // has a system number prefixed with "CUThesis."
        // *** Does this mean it would be better to process one record at a 
        // time, so we can figure out its type?
        return new ThesisModelPostProcessor(ontModel);

    }

}

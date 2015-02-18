package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.*;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * Creates a ModelPostProcessor.
 * @author rjy7
 *
 */
class ModelPostProcessorFactory  {
   
    protected static ModelPostProcessor createModelPostProcessor(
            OntModel recordModel, String localNamespace) {
        
        // A single record may include multiple bf:Works: the primary Work may
        // be related to other Works.  
        ExtendedIterator<Individual> bfWorks = recordModel.listIndividuals(
                recordModel.getProperty(BF_WORK_URI));         
        
        while (bfWorks.hasNext()) {
            // We're relying on the converter always putting the primary Work
            // first. However, we could also test local name of the URI, since
            // for the primary Work it's the bibid, whereas for other Works it's
            // the primary Work's bibid + "work" + id number (e.g., 
            // 7852272work35).
            // TODO For now, we're side-stepping the question of how to know 
            // what type of record we're post-processing. We will need to 
            // inspect bfWork to see what kind of work it is, in order to
            // create the appropriate ModelPostProcessor. For now, just define
            // thesis post-processing, since the current data set consists only 
            // of thesis records. Later must expand to other types of records. 
            // When we have mixed data, we'll need to test for things like: the 
            // bf:Work has relators:ths, bf:dissertationYear, 
            // bf:dissertationDegree, bf:dissertationInstitution properties. 
            // The bf:Instance has a system number prefixed with "CUThesis."
            Individual bfWork = bfWorks.next();

            // Additional Works included in the record are processed with the
            // primary Work, so don't continue iterating through Works.
            return new ThesisModelPostProcessor(recordModel, bfWork, localNamespace);
        }
        return null;
    }
}

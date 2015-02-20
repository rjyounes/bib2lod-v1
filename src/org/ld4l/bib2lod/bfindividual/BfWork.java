package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.postprocessor.Constants.BF_WORK_TITLE_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/* TODO Create common superclass for BfWork and BfInstance, as a subclass of
 * BfIndividual. This would allow code-sharing between the two, without putting
 * common code into BfIndividual, which doesn't make sense. They could share
 * addRdfsLabel() and getTitleDatatypePropertyValue(), while each
 * having their own getWorkTitle() and getInstanceTitle(). Even parts of the
 * latter could be shared by using callouts to superclass methods and/or 
 * defining a common TITLE_OBJECT_PROPERTY with different values in the two 
 * classes.
 */
public class BfWork extends BfWorkObject {

    protected BfWork(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    public void addRdfsLabel() {

        assignDefaultRdfsLabel();
        
        // If no label was assigned in assignDefaultLabel()
        if (! baseIndividual.hasProperty(RDFS.label)) { 
            // Use the title value as the rdfs:label, if present.
            Literal rdfsLabel = getWorkTitle();
    
            if (rdfsLabel != null) {
                baseIndividual.addProperty(RDFS.label, rdfsLabel);
            } else {
                // Otherwise use the generic value
                super.addRdfsLabel();
            }
        }
    }
        
    
    protected Literal getWorkTitle() {
     
        Literal titleLiteral = null;

        // First look for the Title object. Seems to be more reliably well-
        // formed, and we also get the subtitle.
        Resource workTitle = baseIndividual.getPropertyResourceValue(
                recordModel.getProperty(BF_WORK_TITLE_URI));
        if (workTitle != null) {
            Individual workTitleIndividual = recordModel.getIndividual(workTitle.getURI());
            BfTitle bfWorkTitle = new BfTitle(workTitleIndividual);
            titleLiteral = bfWorkTitle.getRdfsLabel();            
        } else {
            // Then look for the title property
            titleLiteral = getTitleDatatypePropertyValue();
        } 
        
        return titleLiteral;
        
    }


}

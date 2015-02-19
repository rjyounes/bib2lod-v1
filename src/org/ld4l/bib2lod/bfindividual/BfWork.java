package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.Constants.BF_WORK_TITLE_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

public class BfWork extends BfIndividual {

    protected BfWork(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    protected void addRdfsLabelByType() {

        Literal rdfsLabel = null;
        
        // First look for the Title object. Seems to be more reliably well-
        // formed, and we also get the subtitle.
        Resource workTitle = baseIndividual.getPropertyResourceValue(
                ontModel.getProperty(BF_WORK_TITLE_URI));
        if (workTitle != null) {
            Individual workTitleIndividual = ontModel.getIndividual(workTitle.getURI());
            BfTitle bfWorkTitle = new BfTitle(workTitleIndividual);
            rdfsLabel = bfWorkTitle.getRdfsLabel();            
        } else {
            // Then look for the title property
            rdfsLabel = getRdfsLabelFromTitleDatatypeProperty();
        } 
        
        if (rdfsLabel != null) {
            baseIndividual.addProperty(RDFS.label, rdfsLabel);
        } else {
            // Otherwise use the generic value
            super.addRdfsLabelByType();
        }
    }
}

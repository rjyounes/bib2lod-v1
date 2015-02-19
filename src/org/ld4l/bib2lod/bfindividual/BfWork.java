package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.Constants.BF_TITLE_PROPERTY_URI;
import static org.ld4l.bib2lod.Constants.BF_WORK_TITLE_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

public class BfWork extends BfIndividual {

    protected BfWork(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    protected void addRdfsLabelByType() {
        
        addRdfsLabelFromTitleDatatypeProperty();
        
        if (! baseIndividual.hasProperty(RDFS.label)) {
            String rdfsLabel = null;
            Resource workTitle = baseIndividual.getPropertyResourceValue(
                    ontModel.getProperty(BF_WORK_TITLE_URI));
            if (workTitle != null) {
                Individual workTitleIndividual = ontModel.getIndividual(workTitle.getURI());
                BfTitle bfWorkTitle = new BfTitle(workTitleIndividual);
                rdfsLabel = bfWorkTitle.getRdfsLabel();
                baseIndividual.addProperty(RDFS.label, rdfsLabel);
            } else {
                super.addRdfsLabelByType();
            }
        }
    }
}

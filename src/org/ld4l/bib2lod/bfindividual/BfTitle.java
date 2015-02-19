package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.Constants.BF_SUBTITLE_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_VALUE_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.RDFS;

public class BfTitle extends BfIndividual {
    
    protected BfTitle(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    protected void addRdfsLabelByType() {
        
        String rdfsLabel = getRdfsLabel();
        if (rdfsLabel != null) {
            baseIndividual.addProperty(RDFS.label, rdfsLabel);
        } else {
            super.addRdfsLabelByType();
        }            
    }
    
    protected String getRdfsLabel() {
        
        String rdfsLabel = null;
        RDFNode titleValue = baseIndividual.getPropertyValue(
                ontModel.getProperty(BF_TITLE_VALUE_URI));
        if (titleValue != null) {
            String titleString = titleValue.asLiteral().getLexicalForm();
            RDFNode subtitle = baseIndividual.getPropertyValue(
                    ontModel.getProperty(BF_SUBTITLE_URI));
            if (subtitle != null) {
                String subtitleString = subtitle.asLiteral().getLexicalForm();
                rdfsLabel = titleString + ": " + subtitleString;
            } else {
                rdfsLabel = titleString;
            }
        }
        return rdfsLabel;
    }
}

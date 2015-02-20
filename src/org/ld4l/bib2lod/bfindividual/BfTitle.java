package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.Constants.BF_SUBTITLE_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_VALUE_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.RDFS;

public class BfTitle extends BfIndividual {
    
    protected BfTitle(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    protected void addRdfsLabelByType() {
        
        Literal rdfsLabel = getRdfsLabel();
        if (rdfsLabel != null) {
            baseIndividual.addProperty(RDFS.label, rdfsLabel);
        } else {
            super.addRdfsLabelByType();
        }            
    }
    
    protected Literal getRdfsLabel() {
        
        Literal rdfsLabelLiteral = null;
        RDFNode titleValue = baseIndividual.getPropertyValue(
                recordModel.getProperty(BF_TITLE_VALUE_URI));
        if (titleValue != null) {
            Literal titleValueLiteral = titleValue.asLiteral();
            String titleString = titleValueLiteral.getLexicalForm();
            RDFNode subtitle = baseIndividual.getPropertyValue(
                    recordModel.getProperty(BF_SUBTITLE_URI));
            String rdfsLabelString;
            if (subtitle != null) {
                String subtitleString = subtitle.asLiteral().getLexicalForm();
                rdfsLabelString = titleString + ": " + subtitleString;
            } else {
                rdfsLabelString = titleString;
            }
            String lang = titleValueLiteral.getLanguage();
            if (lang.isEmpty()) {
                rdfsLabelLiteral = recordModel.createLiteral(rdfsLabelString);
            } else {
                rdfsLabelLiteral = recordModel.createLiteral(rdfsLabelString, lang);
            }
        }
        return rdfsLabelLiteral;
    }
}

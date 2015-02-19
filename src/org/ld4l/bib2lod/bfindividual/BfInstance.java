package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.Constants.BF_INSTANCE_TITLE_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_PROPERTY_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_STATEMENT_URI;
import static org.ld4l.bib2lod.Constants.BF_WORK_TITLE_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

public class BfInstance extends BfIndividual {
    
    protected BfInstance(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    protected void addRdfsLabelByType() {
        
        Literal rdfsLabel = null;
        Resource instanceTitle = baseIndividual.getPropertyResourceValue(
                ontModel.getProperty(BF_INSTANCE_TITLE_URI));
        if (instanceTitle != null) {
            Individual workTitleIndividual = ontModel.getIndividual(instanceTitle.getURI());
            BfTitle bfWorkTitle = new BfTitle(workTitleIndividual);
            rdfsLabel = bfWorkTitle.getRdfsLabel();            
        } else {
            rdfsLabel = getRdfsLabelFromTitleDatatypeProperty();
        } 
        
        if (rdfsLabel == null) {
            RDFNode titleStatement = baseIndividual.getPropertyValue(
                    ontModel.getProperty(BF_TITLE_STATEMENT_URI));
            if (titleStatement != null) {
                rdfsLabel = titleStatement.asLiteral();              
            }
        } 
        
        if (rdfsLabel != null) {
            baseIndividual.addProperty(RDFS.label, rdfsLabel);
        } else {
            super.addRdfsLabelByType();
        }
    }
}

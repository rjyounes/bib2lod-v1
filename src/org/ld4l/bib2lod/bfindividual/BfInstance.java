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
        
        addRdfsLabelFromTitleDatatypeProperty();

        if (! baseIndividual.hasProperty(RDFS.label)) {
            RDFNode titleStatement = baseIndividual.getPropertyValue(
                    ontModel.getProperty(BF_TITLE_STATEMENT_URI));
            if (titleStatement != null) {
                baseIndividual.addLiteral(RDFS.label, titleStatement.asLiteral());
            } else {               
                String rdfsLabel = null;
                Resource instanceTitle = baseIndividual.getPropertyResourceValue(
                        ontModel.getProperty(BF_INSTANCE_TITLE_URI));
                if (instanceTitle != null) {
                    Individual instanceTitleIndividual = ontModel.getIndividual(instanceTitle.getURI());
                    BfTitle bfWorkTitle = new BfTitle(instanceTitleIndividual);
                    rdfsLabel = bfWorkTitle.getRdfsLabel();
                    baseIndividual.addProperty(RDFS.label, rdfsLabel);
                } else {
                    super.addRdfsLabelByType();
                } 
            }
        }
    }
}

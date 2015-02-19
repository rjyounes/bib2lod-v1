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

/* TODO Create common superclass for BfWork and BfInstance, as a subclass of
 * BfIndividual. This would allow code-sharing between the two, without putting
 * common code into BfIndividual, which doesn't make sense. They could share
 * addRdfsLabelByType() and getTitleDatatypePropertyValue(), while each
 * having their own getWorkTitle() and getInstanceTitle(). Even parts of the
 * latter could be shared by using callouts to superclass methods and/or 
 * defining a common TITLE_OBJECT_PROPERTY with different values in the two 
 * classes.
 */
public class BfInstance extends BfIndividual {
    
    protected BfInstance(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    protected void addRdfsLabelByType() {
        
        Literal rdfsLabel = getInstanceTitle();
        
        if (rdfsLabel != null) {
            baseIndividual.addProperty(RDFS.label, rdfsLabel);
        } else {
            // Otherwise use the generic label.
            super.addRdfsLabelByType();
        }
    }
    
    protected Literal getInstanceTitle() {
        
        Literal titleLiteral = null;
        
        // First look for the Title individual. Seems to be more reliably well-
        // formed, and we also get the subtitle.
        Resource instanceTitle = baseIndividual.getPropertyResourceValue(
                ontModel.getProperty(BF_INSTANCE_TITLE_URI));
        if (instanceTitle != null) {
            Individual instanceTitleIndividual = ontModel.getIndividual(instanceTitle.getURI());
            BfTitle bfInstanceTitle = new BfTitle(instanceTitleIndividual);
            titleLiteral = bfInstanceTitle.getRdfsLabel();            
        } else {
            // Then look for the title datatype property.
            titleLiteral = getTitleDatatypePropertyValue();
        } 
        
        if (titleLiteral == null) {
            // Then look for the titleStatement datatype property.
            RDFNode titleStatement = baseIndividual.getPropertyValue(
                    ontModel.getProperty(BF_TITLE_STATEMENT_URI));
            if (titleStatement != null) {
                titleLiteral = titleStatement.asLiteral();              
            }
        }
        
        return titleLiteral;        
    }
}

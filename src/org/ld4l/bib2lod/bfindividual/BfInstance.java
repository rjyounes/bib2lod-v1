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
 * addRdfsLabel() and getTitleDatatypePropertyValue(), while each
 * having their own getWorkTitle() and getInstanceTitle(). Even parts of the
 * latter could be shared by using callouts to superclass methods and/or 
 * defining a common TITLE_OBJECT_PROPERTY with different values in the two 
 * classes.
 */
public class BfInstance extends BfIndividual {
    
    protected BfInstance(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    public void addRdfsLabel() {

        /* TODO First check if type is bf:Electronic and rdfs:label is
         * "Electronic Resource." Then change rdfs:label (or assign new one) to
         * work title + " - Electronic Resource." Can also add this as title.
         * For other instances, go to the default first, then to instance title. 
         * NB Electronic resources have no instance title.
         * Another option: if we have a pre-processing step, the title and 
         * bf:label could be added/modified there.
         */       
        assignDefaultRdfsLabel();
        
        // If no label was assigned in assignDefaultLabel()
        if (! baseIndividual.hasProperty(RDFS.label)) { 
            Literal rdfsLabel = getInstanceTitle();
            
            if (rdfsLabel != null) {
                baseIndividual.addProperty(RDFS.label, rdfsLabel);
            } else {
                // Otherwise use the generic label.
                super.addRdfsLabel();
            }
        }
    }
    
    protected Literal getInstanceTitle() {
        
        Literal titleLiteral = null;
        
        // First look for the Title individual. Seems to be more reliably well-
        // formed, and we also get the subtitle.
        Resource instanceTitle = baseIndividual.getPropertyResourceValue(
                recordModel.getProperty(BF_INSTANCE_TITLE_URI));
        if (instanceTitle != null) {
            Individual instanceTitleIndividual = recordModel.getIndividual(instanceTitle.getURI());
            BfTitle bfInstanceTitle = new BfTitle(instanceTitleIndividual);
            titleLiteral = bfInstanceTitle.getRdfsLabel();            
        } else {
            // Then look for the title datatype property.
            titleLiteral = getTitleDatatypePropertyValue();
        } 
        
        if (titleLiteral == null) {
            // Then look for the titleStatement datatype property.
            RDFNode titleStatement = baseIndividual.getPropertyValue(
                    recordModel.getProperty(BF_TITLE_STATEMENT_URI));
            if (titleStatement != null) {
                titleLiteral = titleStatement.asLiteral();              
            }
        }
        
        return titleLiteral;        
    }
}

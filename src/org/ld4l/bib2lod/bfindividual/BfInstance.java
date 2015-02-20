package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.Constants.BF_ELECTRONIC_URI;
import static org.ld4l.bib2lod.Constants.BF_HAS_INSTANCE_URI;
import static org.ld4l.bib2lod.Constants.BF_INSTANCE_OF_URI;
import static org.ld4l.bib2lod.Constants.BF_INSTANCE_TITLE_URI;
import static org.ld4l.bib2lod.Constants.BF_LABEL_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_PROPERTY_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_STATEMENT_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_VALUE_URI;
import static org.ld4l.bib2lod.Constants.BF_WORK_TITLE_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
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
   
    private static String ELECTRONIC_RESOURCE = "Electronic Resource";
    
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
        if (baseIndividual.hasRDFType(BF_ELECTRONIC_URI)) {
            RDFNode bfLabelNode = 
                    baseIndividual.getPropertyValue(
                            recordModel.getProperty(BF_LABEL_URI));
            if (bfLabelNode != null) {
                Literal bfLabelLiteral = bfLabelNode.asLiteral();
                String bfLabelString = bfLabelLiteral.getLexicalForm();
                if (bfLabelString.equals(ELECTRONIC_RESOURCE)) {
                    Literal instanceTitleLiteral = getInstanceTitle();
                    if (instanceTitleLiteral == null) {
                        Property instanceOfProperty = 
                                recordModel.getProperty(BF_INSTANCE_OF_URI);
                        BfWork relatedWork = (BfWork) BfIndividualFactory.
                               createBfObjectIndividual(
                                       baseIndividual, instanceOfProperty);                               
                        Literal titleLiteral = relatedWork.getWorkTitle();
                        if (titleLiteral  != null) {
                            String title = titleLiteral.getLexicalForm() + 
                                    " (" + ELECTRONIC_RESOURCE + ")";
                            Literal newLiteral = getNewLiteral(
                                    titleLiteral, title);
                            baseIndividual.removeProperty(
                                    RDFS.label, bfLabelLiteral);
                            baseIndividual.addProperty(RDFS.label, newLiteral);
                            // Also assign this value to the Instance's title.
                            String uri = baseIndividual.getURI() + "title";
                            Individual titleIndividual = 
                                    createNewTitle(uri, title); 
                            baseIndividual.addProperty(recordModel.getProperty(
                                    BF_INSTANCE_TITLE_URI), titleIndividual);
                        }
                        
                    }
                }
            }
        }

        if (! baseIndividual.hasProperty(RDFS.label)) {
            assignDefaultRdfsLabel();
            
            // ***Could change Electronic Resource value here - test for it, 
            // then get the title and apply it
            // If electronic type & label, or no rdfs:label:
            // But in electronic resource case, don't do super.addRdfsLabel
            
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
    }
    
    protected Literal getInstanceTitle() {
        
        Literal titleLiteral = null;
        
        // First look for the Title individual. Seems to be more reliably well-
        // formed, and we also get the subtitle.
        Resource instanceTitle = baseIndividual.getPropertyResourceValue(
                recordModel.getProperty(BF_INSTANCE_TITLE_URI));
        if (instanceTitle != null) {
            Individual instanceTitleIndividual = 
                    recordModel.getIndividual(instanceTitle.getURI());
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

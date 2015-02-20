package org.ld4l.bib2lod;

import static org.ld4l.bib2lod.Constants.BF_TITLE_PROPERTY_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_VALUE_URI;

import org.ld4l.bib2lod.bfindividual.BfIndividual;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Abstract class containing methods used by both BfWork and BfInstance. 
 * @author rjy7
 *
 */
public abstract class BfWorkObject extends BfIndividual {

    protected BfWorkObject(Individual baseIndividual) {
        super(baseIndividual);
    }

    protected Individual createNewTitle(String uri, String titleValue) {
        
        Resource titleClass = recordModel.getResource(BF_TITLE_URI);
        Property titleValueProperty = recordModel.getProperty(BF_TITLE_VALUE_URI);
        Individual titleIndividual = recordModel.createIndividual(uri, titleClass);
        titleIndividual.addProperty(titleValueProperty, titleValue);
        titleIndividual.addProperty(RDFS.label, titleValue);
        return titleIndividual;        
    }
    
    protected Literal getTitleDatatypePropertyValue() {
        
        RDFNode title = baseIndividual.getPropertyValue(
                recordModel.getProperty(BF_TITLE_PROPERTY_URI));
        Literal titleLiteral = null;
        if (title != null) {
            titleLiteral = title.asLiteral();
            String lang = titleLiteral.getLanguage();
            // Exclude these: used for sorting and hashing.
            if (lang != null && (lang.equals("x-bf-hash") || lang.equals("x-bf-sort"))) {
                titleLiteral = null;
            } 
        }
        
        return titleLiteral;
    } 

}

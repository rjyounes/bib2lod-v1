package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.Constants.BF_IDENTIFIER_VALUE_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.RDFS;

public class BfIdentifier extends BfIndividual {
    
    protected BfIdentifier(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    public void addRdfsLabel() {
        
        assignDefaultRdfsLabel();
        
        if (! baseIndividual.hasProperty(RDFS.label)) {           
            // So far Identifiers are blank nodes, so this won't apply.
            RDFNode identifierValue = baseIndividual.getPropertyValue(
                    recordModel.getProperty(BF_IDENTIFIER_VALUE_URI));
            if (identifierValue != null) {      
                baseIndividual.addLiteral(RDFS.label, identifierValue.asLiteral());
            } else {
                super.addRdfsLabel();
            }
        }
    }
}

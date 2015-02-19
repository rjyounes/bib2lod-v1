package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.Constants.BF_IDENTIFIER_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.RDFS;

public class BfIdentifier extends BfIndividual {
    
    protected BfIdentifier(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    protected void addRdfsLabelByType() {
        RDFNode identifierValue = baseIndividual.getPropertyValue(
                ontModel.getProperty(BF_IDENTIFIER_URI));
        if (identifierValue != null) {
            baseIndividual.addLiteral(RDFS.label, identifierValue.asLiteral());
        } else {
            super.addRdfsLabelByType();
        }
    }
}

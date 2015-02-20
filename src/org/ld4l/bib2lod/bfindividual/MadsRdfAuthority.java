package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.postprocessor.Constants.MADSRDF_AUTHORITATIVE_LABEL_URI;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.RDFS;

public class MadsRdfAuthority extends BfIndividual {

    protected MadsRdfAuthority(Individual baseIndividual) {
        super(baseIndividual);
    }
    
    public void addRdfsLabel() {
        
        assignDefaultRdfsLabel();
        
        if (! baseIndividual.hasProperty(RDFS.label)) {   
            RDFNode identifierValue = baseIndividual.getPropertyValue(
                    recordModel.getProperty(MADSRDF_AUTHORITATIVE_LABEL_URI));
            if (identifierValue != null) {
                baseIndividual.addLiteral(RDFS.label, identifierValue.asLiteral());
            } else {
                super.addRdfsLabel();
            }
        }
    }

}

package org.ld4l.bib2lod.bfindividual;

import static org.ld4l.bib2lod.Constants.BF_HAS_AUTHORITY_URI;
import static org.ld4l.bib2lod.Constants.BF_LABEL_URI;
import static org.ld4l.bib2lod.Constants.BF_TITLE_PROPERTY_URI;
import static org.ld4l.bib2lod.RDFPostProcessor.LOCAL_NAMESPACE;

import org.apache.commons.lang3.StringUtils;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/** Decorator of class Individual The BfIndividual wraps the Jena Individual to 
 * provide functionality specific to the post-processor and the type of the
 * Individual. The Decorator pattern is used because neither extending 
 * IndividualImpl or implementing Individual is easy.
 * @author rjy7
 *
 */
public class BfIndividual {

    protected Individual baseIndividual;
    protected OntModel ontModel;
    protected String baseUri;
      
    protected BfIndividual(Individual baseIndividual) {
        this.baseIndividual = baseIndividual; 
        // Store OntModel for convenience.
        ontModel = baseIndividual.getOntModel();
        // setOntModelAndType(baseIndividual);
        // The baseUri to be used for minting URIs for new Individuals.
        baseUri = computeBaseUri();
    }    
    
    protected Individual getBaseIndividual() {
        return baseIndividual;
    }
    
    private String computeBaseUri() {
        String baseIndividualUri = baseIndividual.getURI();
        /*
        * Jena strips off initial digits of the local name, so we can't use
        * Jena baseIndividual.getLocalName(). Need to parse the URI to
        * get the full local name.
        * Another way to do the same thing: use the bfWork URI as the base
        * URI for any entity created while processing that work. Not sure if
        * that's as reliable, so using the following.
        * Another option: add an initial non-numeric character to the baseUri
        * passed to the Bibframe converter. E.g., 
        * http://localhost:8080/ld4l-vitro/individual/_
        * Then we shouldn't call it localNamespace, but rather baseUri.
        * For now, the BF conversion isn't linked to the post-processor, so 
        * use the current approach. If/when we link them together into a
        * single process, that would make more sense.
        * */
        String localName = 
                StringUtils.substringAfterLast(baseIndividualUri, "/");
        return LOCAL_NAMESPACE + localName;
    }
    
    protected String getAuthorityResourceUri() {
        String authorityResourceUri = null;
        Property bfHasAuthorityProperty = 
                ontModel.getOntProperty(BF_HAS_AUTHORITY_URI);
        if (baseIndividual.hasProperty(bfHasAuthorityProperty)) {
            Resource authorityResource = 
                    baseIndividual.getPropertyResourceValue(bfHasAuthorityProperty);
            // TODO Should this be limited to external URIs, or should we 
            // include internal ones as well? Seems like the latter.
            authorityResourceUri = authorityResource.getURI();
        }
        return authorityResourceUri;
    }

    public void addRdfsLabel() {
        
        if (! baseIndividual.hasProperty(RDFS.label)) {
            Property property = ontModel.getProperty(BF_LABEL_URI);
            RDFNode bfLabelNode = baseIndividual.getPropertyValue(property);
            if (bfLabelNode != null) {
                Literal bfLabel = bfLabelNode.asLiteral();
                baseIndividual.addLiteral(RDFS.label, bfLabel);                 
            } else {                    
                addRdfsLabelByType();
            }
        }
    }
    
    protected void addRdfsLabelByType() {
        baseIndividual.addLiteral(RDFS.label, baseIndividual.getURI());
    }
    
    // Shared by bfWork and bfInstance
    protected Literal getRdfsLabelFromTitleDatatypeProperty() {
        RDFNode title = baseIndividual.getPropertyValue(
                ontModel.getProperty(BF_TITLE_PROPERTY_URI));
        Literal titleLiteral = null;
        if (title != null) {
            titleLiteral = title.asLiteral();
            String lang = titleLiteral.getLanguage();
            // Exclude these: used for sorting and hashing.
            if (lang != null&& (lang.equals("x-bf-hash") || lang.equals("x-bf-sort"))) {
                titleLiteral = null;
            } 
        }
        return titleLiteral;
    } 
    
}

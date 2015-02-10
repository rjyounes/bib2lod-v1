package org.ld4l.bib2lod;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class Bib2Lod {

    // TODO Break up into classes and methods. Just getting an overview of the
    // workflow for now.
    public static void main(String[] args) {
        
        // TODO Don't hardcode the path
        // Doing as a convenience for now, so don't have to provide full
        // path in arg
        // TODO Use options parser 
        String path = "/Users/rjy7/Workspace/bib2lod/rdf/";        
        String infileName = path + args[0];
        
        // Subclass Model to Bib2LodModel, which includes the post-processing
        // methods?
        Model model = ModelFactory.createDefaultModel(); 
        model.read(infileName);


        
        //////////// POST-PROCESSING GOES HERE ///////////////
        
        // TODO For now just define thesis post-processing. Later must expand 
        // to other types of works. We know that our data consists only of 
        // thesis records. When we have mixed data, will need to test: the 
        // bf:Work has a relators:ths, bf:dissertationYear, 
        // bf:dissertationDegree, bf:dissertationInstitution. The bf:Instance 
        // has a system number prefixed with "CUThesis."
        ModelPostProcessor p = new ThesisModelPostProcessor(model);
        p.process();
        
        // *** rdfs.label ***
        // Check how hard it will be to add rdfs:label during Vitro ingest.
        // If difficult, just do it here for now. Can move to ingest later.
       
        
        // Write out the post-processed rdf
        String outfileName = path + args[1];
        OutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(outfileName);
            model.write(fileOutputStream);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    
    }
    
    
}

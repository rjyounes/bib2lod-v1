package org.ld4l.bib2lod;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.hp.hpl.jena.ontology.OntModel;


public class Bib2Lod {

    /**
     * Handles file IO and passes off to ModelPostProcessor for processing.
     * @param args
     */
    public static void main(String[] args) {
        
        // TODO Don't hardcode the path
        // Doing as a convenience for now, so don't have to provide full
        // path in arg
        // TODO Use options parser 
        String path = "/Users/rjy7/Workspace/bib2lod/rdf/";        
        String infileName = path + args[0];
        
        ModelPostProcessor p = ModelPostProcessorFactory.createModelPostProcessor(infileName);

        //////////// POST-PROCESSING ///////////////
        
        OntModel ontModel = p.process();
        
        // Write out the post-processed rdf
        String outfileName = path + args[1];
        OutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(outfileName);
            ontModel.write(fileOutputStream);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        // System.out.println("Done!");

    
    }
    
    
}

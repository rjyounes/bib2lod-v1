package org.ld4l.bib2lod;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

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

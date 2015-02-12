package org.ld4l.bib2lod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

public class Bib2Lod {

    /**
     * Read in program arguments, pass off for processing, and write out
     * resulting RDF.
     * @param args
     */
    public static void main(String[] args) {
        
        // Read in program arguments
        // TODO Use options parser instead of ordered arguments
        String readdir = args[0]; 
        String outfile = args[1];

        // Convert the directory of RDF files into a list of input streams
        // for processing.
        File directory = new File(readdir);
        File[] items = directory.listFiles();
        List<InputStream> streams = new ArrayList<InputStream>();

        for (File file : items) {
            if (file.isFile()) {
                try {
                    streams.add(new FileInputStream(file));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        // Process the RDF input files and receive a model for printing.
        RDFProcessor p = new RDFProcessor();
        Model unionModel = p.processInputs(streams);
        
        // Write out the cumulative model to a file.
        try {
            OutputStream out = new FileOutputStream(outfile);
            // null = RDF serialization. We may want to be able to specify this
            // on the commandline.
            unionModel.write(out, null);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }    
        
       System.out.println("Done!");

    }        
        
}

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
     * 
     * Note: we may want to process a single file of multiple records (or the
     * ability to do either, depending on whether arg[0] is a file or a 
     * directory. For now we are just handling a directory containing one
     * record per file.
     * 
     * @param args
     * args[0]: the directory of input RDF files
     * args[1]: the path to the file for writing output RDF
     */
    public static void main(String[] args) {
        
        // Read in program arguments
        // TODO Use options parser instead of ordered arguments
        String readdir = args[0]; 
        String outfile = args[1];
        String baseUri = args[2];
         
        // Convert the directory of RDF files into a list of input streams
        // for processing.
        File directory = new File(readdir);
        // Despite the name, lists both files and directories.
        File[] items = directory.listFiles();
        List<InputStream> records = new ArrayList<InputStream>();

        for (File file : items) {
            if (file.isFile()) {
                try {
                    records.add(new FileInputStream(file));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        // Process the RDF input files and receive a union model for printing.
        RDFPostProcessor p = new RDFPostProcessor();
        Model allRecords = p.processRecords(records, baseUri);
        
        // Write out the union model to a file.
        try {
            OutputStream out = new FileOutputStream(outfile);
            // Second param = RDF serialization. We may want to be able to 
            // specify this on the commandline. For now we use the default
            // RDF/XML.
            allRecords.write(out, null);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }    
        
       System.out.println("Done!");

    }        
        
}

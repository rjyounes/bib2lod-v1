package org.ld4l.bib2lod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        
        // Read in program arguments
        // TODO Use options parser instead of ordered arguments
        // The directory to read RDF files from.
        String readdir = args[0];   
        // The file to write output to.
        String outdir = args[1];           
        // Namespace for minting URIs for new individuals.
        String localNamespace = args[2];
         
        List<InputStream> records = readFiles(readdir);
        
        File ontologyDir = new File("ontologies");
        List<InputStream> ontologies = readFiles(ontologyDir.getCanonicalPath());
        
        // Process the RDF input files and receive a union model for printing.
        RDFPostProcessor p = new RDFPostProcessor();
        Model allRecords = p.processRecords(records, ontologies, localNamespace);
        
        // Write out the union model to a file.
        try {
            String outfile = getOutputFilename(outdir);
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
    
    /**
     * Convert a directory of RDF files into a list of input streams for
     * processing.
     * @param directory - the directory
     * @return List<InputStream>
     */
    private static List<InputStream> readFiles(File directory) {
        // Convert the directory of RDF files into a list of input streams
        // for processing.

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
        return records;
    }
    
    /**
     * Convert a directory of RDF files into a list of input streams for
     * processing.
     * @param readdir - path to the directory
     * @return List<InputStream>
     */
    private static List<InputStream> readFiles(String readdir) {
        File directory = new File(readdir);
        return readFiles(directory);
    }
    
    private static String getOutputFilename(String outdir) {
        
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        Date date = new Date();
        String now = dateFormat.format(date);
        // Currently allowing only RDF/XML output. Later may offer 
        // serialization as a program argument. This will affect extension
        // as well as second parameter to allRecords.write(out, null) above.
        String ext = "rdf";
        String filename = "out." + now.toString() + "." + ext;
        Path path = Paths.get(outdir, filename);   
        return path.toString();
    }
        
}

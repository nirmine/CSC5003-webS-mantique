/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package itsudparis.application;

import com.opencsv.exceptions.CsvException;

import com.hp.hpl.jena.rdf.model.Model;
import itsudparis.tools.JenaEngine;

/**Added by Sami **/

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
/**
 * Added by Sami
 **/

/**
 * @author DO.ITSUDPARIS
 */
public class Main {

    /**
     * @param args rhe command line arguments
     */


    public static void main(String[] args) throws IOException, CsvException {
        // Read the dataSets
        //String salesPath = "/home/nirmine/Bureau/tpWebSemantic/dataSet/best_selling_artists.csv";
        String artistsPath = "src/data/artists.csv";
        String albumsPath = "src/data/albums.csv";
        // List<String[]> sales = readDataSet(salesPath);
        List<String[]> artists = readDataSet(artistsPath);
        List<String[]> albums = readDataSet(albumsPath);
        String NS = "";
        // lire le model a partir d'une ontologie
        Model model = JenaEngine.readModel("data/music.owl");
        if (model != null) {
            //lire le Namespace de l’ontologie
            NS = model.getNsPrefixURI("mo");
            String NSFOAF = "http://xmlns.com/foaf/0.1/";

            for (int i = 1; i < 1000; i++) {
//                 System.out.println(artist[0] + " " + artist[1]);
                /** Here goes the code for adding the data to the ontologie **/
                String[] artist = artists.get(i);
                JenaEngine.createInstanceOfClass(model, NS, "musicArtist", artist[1]); // artist name
                JenaEngine.createInstanceOfClass(model, NS, "Instrument", artist[3]); // Instruments
                /****/
            }

            for (int j=1; j < 1000; j++) {
                String[] album = albums.get(j);
//                 System.out.println(artist[0] + " " + artist[1]);
                /** Here goes the code for adding the data to the ontologie **/
                if (Integer.parseInt(album[1]) < 1000) {
                    JenaEngine.createInstanceOfClass(model, NS, "Activity", album[4]); // Years where they where active
                    JenaEngine.createInstanceOfClass(model, NS, "Record", album[2]); // Record / album
                    JenaEngine.createInstanceOfClass(model, NS, "Genre", album[3]); // Genre of record
                }
                /****/
            }

    
            // modifier le model
            JenaEngine.createInstanceOfClass(model, NS, "musicArtist", "nirmine");
            JenaEngine.createInstanceOfClass(model, NS, "musicArtist", "nirmine2");
            //JenaEngine.updateValueOfDataTypeProperty(model, NS,"nirmine", "name", "nirmine");
            /*JenaEngine.updateValueOfObjectProperty(model, NS,"nirmine", "member_of", "bts");*/

            //apply owl rules on the model
            Model owlInferencedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/owlrules.txt");
            // apply our rules on the owlInferencedModel
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(owlInferencedModel, "data/rules.txt");
            // query on the model after inference
            System.out.println(JenaEngine.executeQueryFile(inferedModel, "src/data/querry.txt"));
        } else {
            System.out.println("Error when reading model from ontology");
        }

    }

    public static List<String[]> readDataSet(String filePath) throws IOException, CsvException {

        // Create a new reader
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CSVReader csvReader = new CSVReaderBuilder(reader).build();

        // Read all the records
        List<String[]> records = null;
        records = csvReader.readAll();
        reader.close();

        // Iterate over the records
	/*for (String[] record : records) {
		// Do something with the record
		System.out.println(record[0] + " " + record[1]+" "+record[2] + " " + record[3]+" "+record[4] + " " + record[5]);
	}*/
        return records;
    }
}


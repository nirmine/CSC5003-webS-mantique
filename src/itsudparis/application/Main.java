/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package itsudparis.application;

<<<<<<< HEAD
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
=======
<<<<<<< HEAD

=======
>>>>>>> 546427273f38ded08b1846e410cf8cfde147dd25
import com.opencsv.exceptions.CsvException;
>>>>>>> 44abb81fd67f81418e82fb3b1962b977048e9b46

import com.hp.hpl.jena.rdf.model.Model;
import itsudparis.tools.JenaEngine;

/**Added by Sami **/

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
        String artistsPath = "src/data/artists.csv";
        String albumsPath = "src/data/artistAlbum.csv";
        List<String[]> artists = readDataSet(artistsPath);
        List<String[]> albums = readDataSet(albumsPath);

        String NS = "";
        // lire le model a partir d'une ontologie
        Model model = JenaEngine.readModel("data/musicOnto.owl");
        if (model != null) {
            //System.out.println(model.getNsPrefixURI("geo"));
            //lire le Namespace de l’ontologie
            NS = model.getNsPrefixURI("mo");
            String NSFOAF = "http://xmlns.com/foaf/0.1/";

            for (int i = 1; i < 1000; i++) {
//                 System.out.println(artist[0] + " " + artist[1]);
                /** Here goes the code for adding the data to the ontologie **/
                /*List<String> intruments=new ArrayList<>(Arrays.asList("bassist","violinist","pianist","guitarist","violoncellist","drummer"));
                for(String ch : intruments){
                    JenaEngine.createInstanceOfClass(model, NSFOAF, "Instruments", ch); // instruments
                }*/
                String[] artist = artists.get(i);
                JenaEngine.createInstanceOfClass(model, NSFOAF, "Agent", artist[1]); // artist name
                JenaEngine.createInstanceOfClass(model, NS, "Instrument", artist[3]); // Instruments
                JenaEngine.createInstanceOfClass(model, model.getNsPrefixURI("geo"), "SpatialThing", artist[5]); // Country

                /** Adding properties */
                JenaEngine.updateValueOfObjectProperty(model,NS ,artist[1], "origin", artist[5]); // musicArtist - Country

                JenaEngine.updateValueOfDataTypeProperty(model,NS ,artist[1] , "realName", artist[1]);
                JenaEngine.updateValueOfDataTypeProperty(model,NS ,artist[1] , "artName", artist[2]);
                JenaEngine.updateValueOfDataTypeProperty(model,NS ,artist[1] , "birthYear", artist[4]);
                JenaEngine.updateValueOfDataTypeProperty(model,NS ,artist[1] , "city", artist[5]);
                JenaEngine.updateValueOfDataTypeProperty(model,NS ,artist[1] , "email", artist[7]);
                JenaEngine.updateValueOfDataTypeProperty(model,NS ,artist[1] , "zipCode", artist[8]);
                JenaEngine.updateValueOfDataTypeProperty(model,NS ,artist[1] , "role", artist[3]);
                /*if(artist[3].equals("dancer"))
                System.out.println("role: "+artist[3]);*/
                /****/
            }

            for (int j = 1; j < 2030; j++) {
                String[] album = albums.get(j);
//                 System.out.println(artist[0] + " " + artist[1]);
                /** Here goes the code for adding the data to the ontologie **/
                JenaEngine.createInstanceOfClass(model, NS, "Activity", album[13]); // Years where they where active
                JenaEngine.createInstanceOfClass(model, NS, "Record", album[11]); // Record / album
                JenaEngine.createInstanceOfClass(model, NS, "Genre", album[12]); // Genre of record

                /** Adding properties */
                //JenaEngine.updateValueOfObjectProperty(model, NS,album[1], "primary_instrument", album[3]); // musicArtist - instrument
                JenaEngine.updateValueOfObjectProperty(model, NS,album[1], "activity", album[13]); // musicArtist - Activity
                JenaEngine.updateValueOfObjectProperty(model, NS,album[1], "published", album[11]); // musicArtist - Album
//                JenaEngine.updateValueOfObjectProperty(model, NS,album[11], "producer", album[1]); // Album - musicArtist
                JenaEngine.updateValueOfObjectProperty(model, NS,album[11], "genre", album[12]); // Album - Genre

                JenaEngine.updateValueOfDataTypeProperty(model,NS ,album[11] , "TrackCount", album[14]);
                JenaEngine.updateValueOfDataTypeProperty(model,NS ,album[11] , "SalesNumber", album[15]);
                JenaEngine.updateValueOfDataTypeProperty(model,NS ,album[11] , "RollingStoneCritic", album[16]);
                JenaEngine.updateValueOfDataTypeProperty(model,NS ,album[11] , "MTVCritic", album[17]);
                JenaEngine.updateValueOfDataTypeProperty(model,NS ,album[11] , "MusicManiacCritic", album[18]);
                JenaEngine.updateValueOfDataTypeProperty(model,NS ,album[11] , "PubYear", album[13]);



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
            System.out.println(JenaEngine.executeQueryFile(inferedModel, "src/data/query.txt"));
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


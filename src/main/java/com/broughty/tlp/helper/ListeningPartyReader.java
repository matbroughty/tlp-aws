package com.broughty.tlp.helper;

import com.broughty.tlp.model.ListeningParty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The ListeningPartyReader class provides functionality to read a CSV file containing
 * the details of listening parties and parse them into a list of ListeningParty objects.
 */
public class ListeningPartyReader {
    // the csv file containing the listening party
    public static final String CSV_FILE = "https://timstwitterlisteningparty.com/data/time-slot-data.csv";

    // read the listening party file and return a list of listening party objects
    public static List<ListeningParty> readListeningPartyFile() {
        List<ListeningParty> listeningParties = new ArrayList<>();
        try {
            URI uri = new URI(CSV_FILE);
            URL url = uri.toURL();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 12) {
                    ListeningParty listeningParty = new ListeningParty(tidy(values[0]), tidy(values[1]), tidy(values[2]),
                            tidy(values[3]), tidy(values[4]), tidy(values[5]), tidy(values[6]), tidy(values[7]),
                            tidy(values[8]), tidy(values[9]), tidy(values[10]), tidy(values[11]), tidy(values[12]));
                    listeningParties.add(listeningParty);
                }
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return listeningParties;
    }

    // remove the leading and trailing quote characters " from start and end of string
    static String tidy(String value) {
        return value != null && !value.isBlank() && value.startsWith("\"") && value.endsWith("\"") ?
                value.substring(1, value.length() - 1) : value;
    }

}

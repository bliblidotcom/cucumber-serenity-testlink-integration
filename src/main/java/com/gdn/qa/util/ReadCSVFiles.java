package com.gdn.qa.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chandra.putra on 20/04/2017.
 */
public class ReadCSVFiles {
    public static List<String> readFileCSV(String CSVpath){
        List<String> storiesToRun = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(CSVpath));
            String line ="";
            while(!(line = String.valueOf(reader.readLine())).equalsIgnoreCase("null")){
                storiesToRun.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return storiesToRun;
    }
}

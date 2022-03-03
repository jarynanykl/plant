package com.engeto.pokojoverostliny;

import java.io.*;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class PlantList {
    private static final String FILE_ITEM_DELIMITER = "\t";

    ArrayList<Plant> plants = new ArrayList<>();

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public Plant getPlant(int index) {
        return plants.get(index);
    }

    public void removePlant(int index) {
        plants.remove(index);
    }

    public int size() {
        return plants.size();
    }

    public void importPlantsFromFile(String pathAndFile) throws PlantException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(pathAndFile)))) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                String record = scanner.nextLine();
                lineNumber++;
                try {
                    this.addPlant(Plant.parse(record, FILE_ITEM_DELIMITER));
                } catch (PlantException e) {
                    throw new PlantException("Neplatný vstupní soubor "+pathAndFile+" na řádku "+lineNumber+":\n\t"+e.getLocalizedMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new PlantException("Vstupní soubor "+pathAndFile+" nebyl nalezen: "+e.getLocalizedMessage());
        }
    }

    public void exportToFile(String pathAndFile) throws PlantException {
        int lineNumber = 0;
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(pathAndFile)))){
            for (Plant plant : plants) {
                String plantAsFileLine = plant.prepareOutputString(FILE_ITEM_DELIMITER);
                writer.println(plantAsFileLine);
                lineNumber++;
            }
        } catch (IOException e) {
            throw new PlantException("Error writing to : "+pathAndFile+" line "+lineNumber+": "+e.getLocalizedMessage());
        }
    }
}

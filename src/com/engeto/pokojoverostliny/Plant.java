package com.engeto.pokojoverostliny;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Plant {

    static final int DEFAULT_FREQUENCY_OF_WATERING = 7;

    String name;
    String notes;
    int frequencyOfWateringInDays;
    LocalDate lastWatering;
    LocalDate planted;

    public Plant(String name) {
        this(name, DEFAULT_FREQUENCY_OF_WATERING, LocalDate.now());
    }

    public Plant(String name, int frequencyOfWateringInDays, LocalDate planted) {
        this(name, "", frequencyOfWateringInDays, LocalDate.now(), planted);
    }

    public Plant(String name, String notes, int frequencyOfWateringInDays, LocalDate lastWatering, LocalDate planted) {
        this.name = name;
        this.notes = notes;
        this.frequencyOfWateringInDays = frequencyOfWateringInDays;
        this.lastWatering = lastWatering;
        this.planted = planted;
    }

    /**
     * Create instance of {@link Plant} from a string. Attributes are delimited by {@param delimiter}.
     * Format of the {@param text} parameter:
     *
     * "Plant-name {@param delimiter} notes {@param delimiter} frequency {@param delimiter} lastwatering  {@param delimiter} planted"
     *
     * @param text text describing plant attributes.
     * @param delimiter regex describing item delimiter.
     * @return Instance of plant with appropriate attributes.
     */
    public static Plant parse(String text, String delimiter) throws PlantException {
        String[] items = text.split(delimiter);

        int numberOfItems = items.length;
        if (numberOfItems != 5) throw new PlantException("Nesprávný počet položek na řádku! Očekáváme 5 položek, místo "+numberOfItems+" položek na řádku: "+text);

        String name = items[0];
        String notes = items[1];
        try {
            int frequencyOfWateringInDays = Integer.parseInt(items[2]);
            LocalDate lastWatering = LocalDate.parse(items[3]);
            LocalDate planted = LocalDate.parse(items[4]);

            return new Plant(name, notes, frequencyOfWateringInDays, lastWatering, planted);
        }
        catch (DateTimeParseException ex) { throw new PlantException("Špatně zadané datum na řádku: \""+text+"\"\n\t"+ex.getLocalizedMessage()); }
        catch (NumberFormatException ex) { throw new PlantException("Špatně zadané číslo na řádku: \""+text+"\"\n\t"+ex.getLocalizedMessage()); }


    }

    public String prepareOutputString(String delimiter) {
        return
                getName() + delimiter
                        + getNotes() + delimiter
                        + getFrequencyOfWateringInDays() + delimiter
                        + getLastWatering() + delimiter
                        + getPlanted();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getFrequencyOfWateringInDays() {
        return frequencyOfWateringInDays;
    }

    public void setFrequencyOfWateringInDays(int frequencyOfWateringInDays) throws PlantException {
        if (frequencyOfWateringInDays <= 0) throw new PlantException("Frekvence zálivky musí být alespoň jeden den! (Zadáno: "+frequencyOfWateringInDays+".)");
        this.frequencyOfWateringInDays = frequencyOfWateringInDays;
    }

    public LocalDate getLastWatering() {
        return lastWatering;
    }

    public void setLastWatering(LocalDate lastWatering) throws PlantException {
        LocalDate datePlanted = getPlanted();
        if (lastWatering.isBefore(datePlanted)) {
            throw new PlantException("Datum poslední zálivky "+lastWatering+" nesmí být starší než datum zasazení rostliny "+datePlanted+"!");
        }
        this.lastWatering = lastWatering;
    }

    public LocalDate getPlanted() {
        return planted;
    }

    public void setPlanted(LocalDate planted) {
        this.planted = planted;
    }

    public String getWateringInfo() {
        return getName() + ": poslední zalévání: " + lastWatering + ", doporučené další zalévání: "+ lastWatering.plusDays(getFrequencyOfWateringInDays());
    }
}
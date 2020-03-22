package com.coronavirus.core.report;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.coronavirus.core.CoronaVirus;

import net.sf.csv4j.CSVWriter;
import sim.engine.SimState;
import sim.engine.Steppable;

public class CoronaVirusReport implements Steppable {

  public static final int ORDERING = 3;
  private static final long serialVersionUID = 1L;
  // file names
  private static final String CLIMATE_FILE_NAME = "climate.csv";
  private static final String MOSQUITO_STATE_FILE_NAME = "mosquito-state.csv";
  private static final String EGGS_STATE_FILE_NAME = "eggs-state.csv";
  private static final String HUMAN_HEALTH_FILE_NAME = "human-health.csv";
  private static final String MOSQUITO_HEALTH_FILE_NAME = "mosquito-health.csv";
  private static final String HEALTH_CENTER_STATE_FILE_NAME = "health-center-state.csv";

  private CoronaVirus coronaVirus;
  // temperature and precipitation statistics
  private BufferedWriter bufferedClimateWriter;
  private CSVWriter csvClimateWriter;
  // state statistics
  private BufferedWriter bufferedMosquitoStateWriter;
  private CSVWriter csvMosquitoStatehWriter;
  private BufferedWriter bufferedEggsStatesWriter;
  private CSVWriter csvEggsStatesWriter;
  // health statistics
  private BufferedWriter bufferedMosquitoHealthWriter;
  private CSVWriter csvMosquitoHealthWriter;
  private BufferedWriter bufferedHumanHealthWriter;
  private CSVWriter csvHumanHealthWriter;
  // intervention statistics
  private BufferedWriter bufferedHealthCenterStateWriter;
  private CSVWriter csvHealthCenterStateWriter;

  public CoronaVirusReport(CoronaVirus coronaVirus) {
    this.coronaVirus = coronaVirus;
    this.buildHeaders();
  }

  public void step(SimState state) {
    this.coronaVirus = (CoronaVirus) state;

    // near midnight generate results
    if (this.coronaVirus.schedule.getSteps() % 1440 == 1439) {
      this.writeClimateStatistics();
      this.writeHumanHealthStatistics();
      this.writeHealthCenterStateStatistics();
    }
  }

  // statistics related to the temperature and precipitation
  private void writeClimateStatistics() {
    try {
      String[] data;
      String day = Long.toString(coronaVirus.getCurrentDay());
      String temperature = Double.toString(coronaVirus.getTemperature());
      String precipitation = Double.toString(coronaVirus.getPrecipitation());
      String waterAccumulationInHouses = Double.toString(coronaVirus.getWaterAccumulationInHouses());

      data = new String[] { day, temperature, precipitation, waterAccumulationInHouses };
      this.csvClimateWriter.writeLine(data);
    } catch (IOException ex) {
      Logger.getLogger(CoronaVirusReport.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void writeHumanHealthStatistics() {
    try {
      String[] data;
      String day = Long.toString(coronaVirus.getCurrentDay());
      String numberSuscpitable = Integer.toString(coronaVirus.getTotalOfHumansSusceptible());
      String numberExposed = Integer.toString(coronaVirus.getTotalOfHumansExposed());
      String numberMildInfected = Integer.toString(coronaVirus.getTotalOfHumansWithMildInfection());
      String numberSevereInfected = Integer.toString(coronaVirus.getTotalOfHumansWithSevereInfected());
      String numberToxicInfected = Integer.toString(coronaVirus.getTotalOfHumansWithToxicInfected());
      String numberRecovered = Integer.toString(coronaVirus.getTotalOfHumansRecovered());
      String numberDeadHumans = Integer.toString(coronaVirus.getAmountDeadHumans());

      data = new String[] { day, numberSuscpitable, numberExposed, numberMildInfected, numberSevereInfected,
          numberToxicInfected, numberRecovered, numberDeadHumans };
      this.csvHumanHealthWriter.writeLine(data);
    } catch (IOException ex) {
      Logger.getLogger(CoronaVirusReport.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  // statistics related to the intervention
  // TODO:
  private void writeHealthCenterStateStatistics() {
    try {
      String[] data;
      String day = Long.toString(coronaVirus.getCurrentDay());
      String numberOfVisits = Integer.toString(coronaVirus.getTotalVisitsMedicalCenter());
      String numberOfRefused = Boolean.toString(coronaVirus.isMaximumCapacityInDay());

      data = new String[] { day, numberOfVisits, numberOfRefused };
      this.csvHealthCenterStateWriter.writeLine(data);
    } catch (IOException ex) {
      Logger.getLogger(CoronaVirusReport.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void finish() {
    try {
      this.bufferedClimateWriter.close();
      this.bufferedMosquitoStateWriter.close();
      this.bufferedEggsStatesWriter.close();
      this.bufferedMosquitoHealthWriter.close();
      this.bufferedHumanHealthWriter.close();
      this.bufferedHealthCenterStateWriter.close();
    } catch (IOException ex) {
      Logger.getLogger(CoronaVirusReport.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void createFiles() throws IOException {
    this.bufferedClimateWriter = new BufferedWriter(new FileWriter(CLIMATE_FILE_NAME));
    this.csvClimateWriter = new CSVWriter(bufferedClimateWriter);

    this.bufferedMosquitoStateWriter = new BufferedWriter(new FileWriter(MOSQUITO_STATE_FILE_NAME));
    this.csvMosquitoStatehWriter = new CSVWriter(bufferedMosquitoStateWriter);

    this.bufferedEggsStatesWriter = new BufferedWriter(new FileWriter(EGGS_STATE_FILE_NAME));
    this.csvEggsStatesWriter = new CSVWriter(bufferedEggsStatesWriter);

    this.bufferedMosquitoHealthWriter = new BufferedWriter(new FileWriter(MOSQUITO_HEALTH_FILE_NAME));
    this.csvMosquitoHealthWriter = new CSVWriter(bufferedMosquitoHealthWriter);

    this.bufferedHumanHealthWriter = new BufferedWriter(new FileWriter(HUMAN_HEALTH_FILE_NAME));
    this.csvHumanHealthWriter = new CSVWriter(bufferedHumanHealthWriter);

    this.bufferedHealthCenterStateWriter = new BufferedWriter(new FileWriter(HEALTH_CENTER_STATE_FILE_NAME));
    this.csvHealthCenterStateWriter = new CSVWriter(bufferedHealthCenterStateWriter);
  }

  private void buildHeaders() {
    try {
      this.createFiles();
      // statistics related to the temperature and precipitation
      String[] climateHeader = new String[] { "DAY", "TEMPERATURE", "PRECIPITATION", "WATER_ACCUMULATION_IN_HOUSES" };
      csvClimateWriter.writeLine(climateHeader);

      // statistics related to the parameterization of mosquito evolution
      String[] mosquitoStateHeader = new String[] { "DAY", "AMOUNT", "CARRYING_EGGS", "AMOUNT_OF_DEAD" };
      csvMosquitoStatehWriter.writeLine(mosquitoStateHeader);

      String[] eggsStateHeader = new String[] { "DAY", "AMOUNT_OF_EGGS_IN_HOUSES", "AMOUNT_OF_EGGS_HATCHED",
          "AMOUNT_EGGS_UNIT_DEAD", "AMOUNT_GROUPS_OF_DEAD_EGGS" };
      csvEggsStatesWriter.writeLine(eggsStateHeader);

      // statistics related to the evolution of the infection
      String[] mosquitosHealthHeader = new String[] { "DAY", "SUSCEPTIBLE", "EXPOSED", "INFECTED", "AMOUNT_OF_DEAD" };
      csvMosquitoHealthWriter.writeLine(mosquitosHealthHeader);

      String[] humanHealthHeader = new String[] { "DAY", "SUSCEPTIBLE", "EXPOSED", "MILD_INFECTION", "SEVERE_INFECTION",
          "TOXIC_INFECTION", "RECOVERED", "AMOUNT_OF_DEAD" };
      csvHumanHealthWriter.writeLine(humanHealthHeader);

      // statistics related to the intervention
      String[] healthCenterStateHeader = new String[] { "DAY", "NUMBER_OF_VISITS", "MAXIMUM_CAPACITY_ON_DAY" };
      csvHealthCenterStateWriter.writeLine(healthCenterStateHeader);

    } catch (IOException ex) {
      Logger.getLogger(CoronaVirus.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}

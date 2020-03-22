package com.coronavirus.core;

import java.io.File;
import java.io.IOException;
import ec.util.Parameter;
import ec.util.ParameterDatabase;

public class Parameters {

  private GlobalParamters global = new GlobalParamters();
  private final static String A_FILE = "-file";

  public Parameters(String[] args) {
    if (args != null) {
      loadParameters(openParameterDatabase(args));
    }
  }

  // <editor-fold defaultstate="collapsed" desc="ECJ ParameterDatabase methods">
  /**
   * Initialize parameter database from file
   *
   * If there exists an command line argument '-file', create a parameter
   * database from the file specified. Otherwise create an empty parameter
   * database.
   *
   * @param args
   *          contains command line arguments
   * @return newly created parameter data base
   *
   * @see loadParameters()
   */
  private static ParameterDatabase openParameterDatabase(String[] args) {
    ParameterDatabase parameters = null;
    for (int x = 0; x < args.length - 1; x++) {
      if (args[x].equals(A_FILE)) {
        try {
          File parameterDatabaseFile = new File(args[x + 1]);
          parameters = new ParameterDatabase(parameterDatabaseFile.getAbsoluteFile());
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        break;
      }
    }
    if (parameters == null) {
      System.out.println("\nNot in a parameter Mode");// ("\nNo parameter file
                                                      // was specified");
      parameters = new ParameterDatabase();
    }
    return parameters;
  }

  private void loadParameters(ParameterDatabase parameterDB) {
    int intParameter = 0;
    double doubleParameter = 0;

    intParameter = returnIntParameter(parameterDB, "InitialMosquitoesNumber", global.initialMosquitoesNumber);
    global.initialMosquitoesNumber = intParameter;

    intParameter = returnIntParameter(parameterDB, "InitialHumansNumber", global.initialHumansNumber);
    global.initialHumansNumber = intParameter;

    intParameter = returnIntParameter(parameterDB, "InitialHumansNumberInfected", global.initialHumansNumberInfected);
    global.initialHumansNumberInfected = intParameter;

    intParameter = returnIntParameter(parameterDB, "InitialMosquitoesNumberInfected",
        global.initialMosquitoesNumberInfected);
    global.initialMosquitoesNumberInfected = intParameter;

    intParameter = returnIntParameter(parameterDB, "maximumFamilyOccupancyPerBuilding",
        global.maximumFamilyOccumpancyPerBuilding);
    global.maximumFamilyOccumpancyPerBuilding = intParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "waterAbsorption(mm)", global.waterAbsorption);
    global.waterAbsorption = doubleParameter;

    intParameter = returnIntParameter(parameterDB, "quantityOfVaccinesApplied", global.quantityOfVaccinesApplied);
    global.quantityOfVaccinesApplied = intParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfEggsAppearInHouses",
        global.probabilityOfEggsAppearInHouses);
    global.probabilityOfEggsAppearInHouses = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfMosquitoReachingGoal",
        global.probabilityOfMosquitoReachingGoal);
    global.probabilityOfMosquitoReachingGoal = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "transmissionProbabilityFromVectorToHuman",
        global.getTransmissionProbabilityFromVectorToHuman());
    global.transmissionProbabilityFromVectorToHuman = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "transmissionProbabilityMildInfectionToVector",
        global.getTransmissionProbabilityMildInfectionToVector());
    global.transmissionProbabilityMildInfectionToVector = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "transmissionProbabilitySevereInfectionToVector",
        global.getTransmissionProbabilitySevereInfectionToVector());
    global.transmissionProbabilitySevereInfectionToVector = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfMildInfection",
        global.probabilityOfMildInfection);
    global.probabilityOfMildInfection = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityFromSevereInfectionTotoxicInfection",
        global.probabilityFromSevereInfectionTotoxicInfection);
    global.probabilityFromSevereInfectionTotoxicInfection = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "", global.probabilityOfCarryEggsAtSimulationStart);
    global.probabilityOfCarryEggsAtSimulationStart = doubleParameter;

    intParameter = returnIntParameter(parameterDB, "maximumNumberRelativeFamily", global.maximumNumberRelativeFamily);
    global.maximumNumberRelativeFamily = intParameter;

    intParameter = returnIntParameter(parameterDB, "healthFacilityCapacity", global.healthFacilityCapacity);
    global.healthFacilityCapacity = intParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfHouseContainsNaturalFood",
        global.probabilityOfHouseContainsNaturalFood);
    global.probabilityOfHouseContainsNaturalFood = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityHomeContainingEggsAtSimulationStart",
        global.probabilityHomeContainingEggsAtSimulationStart);
    global.probabilityHomeContainingEggsAtSimulationStart = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfEggsGroupDeath",
        global.probabilityOfEggsGroupDeath);
    global.probabilityOfEggsGroupDeath = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfAnEggUnitDying",
        global.probabilityOfAnEggUnitDying);
    global.probabilityOfAnEggUnitDying = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfCarryingEggs", global.probabilityOfCarryingEggs);
    global.probabilityOfCarryingEggs = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfMosquitoesDying",
        global.probabilityOfMosquitoesDying);
    global.probabilityOfMosquitoesDying = doubleParameter;

    intParameter = returnIntParameter(parameterDB, "quantityOfMedicineAvailable", global.quantityOfMedicineAvailable);
    global.quantityOfMedicineAvailable = intParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityToGoGettingMedicalHelp",
        global.probabilityToGoGettingMedicalHelp);
    global.probabilityToGoGettingMedicalHelp = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfEggBeingFemale",
        global.probabilityOfEggBeingFemale);
    global.probabilityOfEggBeingFemale = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfAddingMosquitoesToBuildingsWithMosquitoes",
        global.probabilityOfAddingMosquitoesToBuildingsWithMosquitoes);
    global.probabilityOfAddingMosquitoesToBuildingsWithMosquitoes = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfHumanLeavingLateFromHome",
        global.probabilityOfHumanLeavingLateFromHome);
    global.probabilityOfHumanLeavingLateFromHome = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfDeath", global.probabilityOfDeath);
    global.probabilityOfDeath = doubleParameter;

    doubleParameter = returnDoubleParameter(parameterDB, "probabilityOfChangeOverDepositedWater",
        global.probabilityOfChangeOverDepositedWater);
    global.probabilityOfChangeOverDepositedWater = doubleParameter;

  }

  public int returnIntParameter(ParameterDatabase paramDB, String parameterName, int defaultValue) {
    return paramDB.getIntWithDefault(new Parameter(parameterName), null, defaultValue);
  }

  public boolean returnBooleanParameter(ParameterDatabase paramDB, String parameterName, boolean defaultValue) {
    return paramDB.getBoolean(new Parameter(parameterName), null, defaultValue);
  }

  double returnDoubleParameter(ParameterDatabase paramDB, String parameterName, double defaultValue) {
    return paramDB.getDoubleWithDefault(new Parameter(parameterName), null, defaultValue);
  }

  public GlobalParamters getGlobal() {
    return global;
  }

  public void setGlobal(GlobalParamters global) {
    this.global = global;
  }

  public class GlobalParamters {
    // parameter to initial number of agents
    private int initialMosquitoesNumber = 300;
    private int initialHumansNumber = 1000;
    // parameters to inicial number infected
    private int initialMosquitoesNumberInfected = 10;
    private int initialHumansNumberInfected = 10;
    // parameters to climate and related
    private double waterAbsorption = 0.1;
    // parameters to infection
    private double probabilityOfMildInfection = 0.85;
    private double transmissionProbabilityMildInfectionToVector = 0.7;
    private double transmissionProbabilitySevereInfectionToVector = 0.9;
    private double probabilityFromSevereInfectionTotoxicInfection = 0.1;
    private double transmissionProbabilityFromVectorToHuman = 0.9;
    private double probabilityOfDeath = 0.5;
    // parameters to intervention
    private int healthFacilityCapacity = 50;
    private int quantityOfMedicineAvailable = 200;
    private int quantityOfVaccinesApplied = 100;
    // parameter to behavior
    private double probabilityOfEggBeingFemale = 0.5;
    private double probabilityOfCarryEggsAtSimulationStart = 0.5;
    private double probabilityHomeContainingEggsAtSimulationStart = 0.5;
    private double probabilityOfHouseContainsNaturalFood = 0.9;
    private double probabilityOfEggsAppearInHouses = 0.05;
    private double probabilityOfMosquitoReachingGoal = 0.9;
    private double probabilityOfEggsGroupDeath = 0.1;
    private double probabilityOfAnEggUnitDying = 0.5;
    private double probabilityOfCarryingEggs = 0.8;
    private double probabilityOfMosquitoesDying = 0.05;
    private double probabilityToGoGettingMedicalHelp = 0.3;
    private int maximumNumberRelativeFamily = 15;
    private int maximumFamilyOccumpancyPerBuilding = 10;
    private double probabilityOfAddingMosquitoesToBuildingsWithMosquitoes = 0.01;
    private double probabilityOfHumanLeavingLateFromHome = 0.1;
    private double probabilityOfChangeOverDepositedWater = 0.5;

    public void setInitialHumansNumber(int num) {
      this.initialHumansNumber = num;
    }

    public int getInitialHumansNumber() {
      return initialHumansNumber;

    }

    public void setMaximumNumberRelativeFamily(int num) {
      this.maximumNumberRelativeFamily = num;
    }

    public int getMaximumNumberRelativeFamily() {
      return maximumNumberRelativeFamily;

    }

    public void setMaximumFamilyOccumpancyPerBuilding(int number) {
      this.maximumFamilyOccumpancyPerBuilding = number;
    }

    public int getMaximumFamilyOccumpancyPerBuilding() {
      return maximumFamilyOccumpancyPerBuilding;

    }

    public void setHealthFacilityCapacity(int capacity) {
      this.healthFacilityCapacity = capacity;
    }

    public int getHealthFacilityCapacity() {
      return healthFacilityCapacity;
    }

    public int getInitialHumansNumberInfected() {
      return initialHumansNumberInfected;
    }

    public void setInitialHumansNumberInfected(int initialHumansNumberInfected) {
      this.initialHumansNumberInfected = initialHumansNumberInfected;
    }

    public int getInitialMosquitoesNumber() {
      return initialMosquitoesNumber;
    }

    public void setInitialMosquitoesNumber(int initialMosquitoesNumber) {
      this.initialMosquitoesNumber = initialMosquitoesNumber;
    }

    public double getProbabilityOfEggsAppearInHouses() {
      return probabilityOfEggsAppearInHouses;
    }

    public void setProbabilityOfEggsAppearInHouses(double probability) {
      this.probabilityOfEggsAppearInHouses = probability;
    }

    public double getWaterAbsorption() {
      return waterAbsorption;
    }

    public void setWaterAbsorption(double waterAbsorption) {
      this.waterAbsorption = waterAbsorption;
    }

    public int getQuantityOfVaccinesApplied() {
      return quantityOfVaccinesApplied;
    }

    public void setQuantityOfVaccinesApplied(int quantityOfVaccinesApplied) {
      this.quantityOfVaccinesApplied = quantityOfVaccinesApplied;
    }

    public double getProbabilityOfMildInfection() {
      return probabilityOfMildInfection;
    }

    public void setProbabilityOfMildInfection(double probability) {
      this.probabilityOfMildInfection = probability;
    }

    public double getProbabilityFromSevereInfectionTotoxicInfection() {
      return probabilityFromSevereInfectionTotoxicInfection;
    }

    public void setProbabilityFromSevereInfectionTotoxicInfection(double probability) {
      this.probabilityFromSevereInfectionTotoxicInfection = probability;
    }

    public double getTransmissionProbabilityMildInfectionToVector() {
      return transmissionProbabilityMildInfectionToVector;
    }

    public void setTransmissionProbabilityMildInfectionToVector(
        int transmissionProbabilityFromHostWithMildInfectionToVector) {
      this.transmissionProbabilityMildInfectionToVector = transmissionProbabilityFromHostWithMildInfectionToVector;
    }

    public double getProbabilityOfCarryEggsAtSimulationStart() {
      return probabilityOfCarryEggsAtSimulationStart;
    }

    public void setProbabilityOfCarryEggsAtSimulationStart(double probabilityOfCarryEggsAtSimulationStart) {
      this.probabilityOfCarryEggsAtSimulationStart = probabilityOfCarryEggsAtSimulationStart;
    }

    public double getProbabilityOfHouseContainsNaturalFood() {
      return probabilityOfHouseContainsNaturalFood;
    }

    public void setProbabilityOfHouseContainsNaturalFood(double probabilityOfHouseContainsNaturalFood) {
      this.probabilityOfHouseContainsNaturalFood = probabilityOfHouseContainsNaturalFood;
    }

    public int getInitialMosquitoesNumberInfected() {
      return initialMosquitoesNumberInfected;
    }

    public void setInitialMosquitoesNumberInfected(int initialMosquitoesNumberInfected) {
      this.initialMosquitoesNumberInfected = initialMosquitoesNumberInfected;
    }

    public double getProbabilityHomeContainingEggsAtSimulationStart() {
      return probabilityHomeContainingEggsAtSimulationStart;
    }

    public void setProbabilityHomeContainingEggsAtSimulationStart(
        double probabilityHomeContainingEggsAtSimulationStart) {
      this.probabilityHomeContainingEggsAtSimulationStart = probabilityHomeContainingEggsAtSimulationStart;
    }

    public double getProbabilityOfEggsGroupDeath() {
      return probabilityOfEggsGroupDeath;
    }

    public void setProbabilityOfEggsGroupDeath(double probabilityOfEggsDying) {
      this.probabilityOfEggsGroupDeath = probabilityOfEggsDying;
    }

    public double getProbabilityOfCarryingEggs() {
      return probabilityOfCarryingEggs;
    }

    public void setProbabilityOfCarryingEggs(double probabilityOfCarryingEggs) {
      this.probabilityOfCarryingEggs = probabilityOfCarryingEggs;
    }

    public double getProbabilityOfMosquitoesDying() {
      return probabilityOfMosquitoesDying;
    }

    public void setProbabilityOfMosquitoesDying(double probabilityOfMosquitoesDying) {
      this.probabilityOfMosquitoesDying = probabilityOfMosquitoesDying;
    }

    public void setTransmissionProbabilityMildInfectionToVector(double transmissionProbabilityMildInfectionToVector) {
      this.transmissionProbabilityMildInfectionToVector = transmissionProbabilityMildInfectionToVector;
    }

    public double getTransmissionProbabilitySevereInfectionToVector() {
      return transmissionProbabilitySevereInfectionToVector;
    }

    public void setTransmissionProbabilitySevereInfectionToVector(
        double transmissionProbabilitySevereInfectionToVector) {
      this.transmissionProbabilitySevereInfectionToVector = transmissionProbabilitySevereInfectionToVector;
    }

    public double getTransmissionProbabilityFromVectorToHuman() {
      return transmissionProbabilityFromVectorToHuman;
    }

    public void setTransmissionProbabilityFromVectorToHuman(double transmissionProbabilityFromVectorToHost) {
      this.transmissionProbabilityFromVectorToHuman = transmissionProbabilityFromVectorToHost;
    }

    public int getQuantityOfMedicineAvailable() {
      return quantityOfMedicineAvailable;
    }

    public void setQuantityOfMedicineAvailable(int quantityOfMedicineAvailable) {
      this.quantityOfMedicineAvailable = quantityOfMedicineAvailable;
    }

    public double getProbabilityToGoGettingMedicalHelp() {
      return probabilityToGoGettingMedicalHelp;
    }

    public void setProbabilityToGoGettingMedicalHelp(double probabilityToGoGettingMedicalHelp) {
      this.probabilityToGoGettingMedicalHelp = probabilityToGoGettingMedicalHelp;
    }

    public double getProbabilityOfEggBeingFemale() {
      return probabilityOfEggBeingFemale;
    }

    public void setProbabilityOfEggBeingFemale(double probabilityOfEggBeingFemale) {
      this.probabilityOfEggBeingFemale = probabilityOfEggBeingFemale;
    }

    public double getProbabilityOfAnEggUnitDying() {
      return probabilityOfAnEggUnitDying;
    }

    public void setProbabilityOfAnEggUnitDying(double probabilityOfAnEggUnitDying) {
      this.probabilityOfAnEggUnitDying = probabilityOfAnEggUnitDying;
    }

    public double getProbabilityOfMosquitoReachingGoal() {
      return probabilityOfMosquitoReachingGoal;
    }

    public void setProbabilityOfMosquitoReachingGoal(double probabilityOfMosquitoReachingGoal) {
      this.probabilityOfMosquitoReachingGoal = probabilityOfMosquitoReachingGoal;
    }

    public double getProbabilityOfAddingMosquitoesToBuildingsWithMosquitoes() {
      return probabilityOfAddingMosquitoesToBuildingsWithMosquitoes;
    }

    public void setProbabilityOfAddingMosquitoesToBuildingsWithMosquitoes(
        double probabilityOfAddingMosquitoesToBuildingsWithMosquitoes) {
      this.probabilityOfAddingMosquitoesToBuildingsWithMosquitoes = probabilityOfAddingMosquitoesToBuildingsWithMosquitoes;
    }

    public double getProbabilityOfHumanLeavingLateFromHome() {
      return probabilityOfHumanLeavingLateFromHome;
    }

    public void setProbabilityOfHumanLeavingLateFromHome(double probabilityOfHumanLeavingLateFromHome) {
      this.probabilityOfHumanLeavingLateFromHome = probabilityOfHumanLeavingLateFromHome;
    }

    public double getProbabilityOfDeath() {
      return probabilityOfDeath;
    }

    public void setProbabilityOfDeath(double probabilityOfDeath) {
      this.probabilityOfDeath = probabilityOfDeath;
    }

    public double getProbabilityOfChangeOverDepositedWater() {
      return probabilityOfChangeOverDepositedWater;
    }

    public void setProbabilityOfChangeOverDepositedWater(double probabilityOfChangeOverDepositedWater) {
      this.probabilityOfChangeOverDepositedWater = probabilityOfChangeOverDepositedWater;
    }
  }
}

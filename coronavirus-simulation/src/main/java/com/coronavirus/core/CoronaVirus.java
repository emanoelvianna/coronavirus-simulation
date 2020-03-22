package com.coronavirus.core;

import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.xy.XYSeries;

import com.coronavirus.core.algorithms.TimeManager;
import com.coronavirus.core.report.CoronaVirusReport;
import com.coronavirus.model.Building;
import com.coronavirus.model.Climate;
import com.coronavirus.model.Facility;
import com.coronavirus.model.Human;
import com.coronavirus.model.enumeration.DayOfWeek;
import com.coronavirus.model.enumeration.HealthStatus;

import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.geo.GeomGridField;
import sim.field.geo.GeomVectorField;
import sim.field.grid.DoubleGrid2D;
import sim.field.grid.IntGrid2D;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Bag;

public class CoronaVirus extends SimState {

  private static final long serialVersionUID = -5966446373681187141L;
  public ObjectGrid2D allCamps;
  public GeomGridField allCampGeoGrid;
  public DoubleGrid2D rainfallGrid;
  public Continuous2D allHumans;
  public SparseGrid2D facilityGrid;
  public IntGrid2D roadGrid;
  public GeomVectorField roadLinks;
  public GeomVectorField campShape;
  public SparseGrid2D nodes;
  // the road nodes closest to each of the locations
  public ObjectGrid2D closestNodes;
  public Network roadNetwork = new Network();
  private final Parameters params;

  // charts and graphs
  public XYSeries rainfallSeries = new XYSeries(" Rainfall"); //
  public XYSeries totalSusceptibleSeries = new XYSeries("Susceptible");
  public XYSeries totalExposedSeries = new XYSeries("Exposed");
  public XYSeries totalMildInfectedSeries = new XYSeries("Mild Infected");
  public XYSeries totalSevereInfectedSeries = new XYSeries("Severe Infected");
  public XYSeries totalToxicInfectedSeries = new XYSeries("Toxic Infected");
  public XYSeries totalRecoveredSeries = new XYSeries("Recovered");
  public XYSeries totalDeathSeries = new XYSeries("Death");
  public XYSeries totalTotalPopSeries = new XYSeries("Total");

  public DefaultCategoryDataset dataset = new DefaultCategoryDataset();
  // shows age structure of agents
  public DefaultCategoryDataset agedataset = new DefaultCategoryDataset();
  // shows family size
  public DefaultCategoryDataset familydataset = new DefaultCategoryDataset();
  public DefaultValueDataset hourDialer = new DefaultValueDataset();
  public DefaultValueDataset dayDialer = new DefaultValueDataset();

  private Bag allFamilies; // holding all families
  private Bag familyHousing;
  private Bag allFacilities;
  private Bag works;
  private Bag schooles;
  private Bag healthCenters;
  private Bag mosques;
  private Bag market;
  private Bag foodCenter;
  private Bag other;
  private Facility facility;// schduling borehole refill
  private CoronaVirusReport report;
  private Climate climate;
  private TimeManager time;

  public int totalgridWidth = 10;
  public int totalgridHeight = 10;

  private int currentDay;
  private double temperature;
  private double precipitation;
  private double waterAccumulationInHouses;
  // used to the human statistics
  private int totalOfHumansSusceptible;
  private int totalOfHumansExposed;
  private int totalOfHumansWithMildInfection;
  private int totalOfHumansWithSevereInfected;
  private int totalOfHumansWithToxicInfected;
  private int totalOfHumansRecovered;
  private int amountDeadHumans;
  // used to the medical center statistics
  private int totalVisitsMedicalCenter;
  private boolean maximumCapacityInDay;

  public CoronaVirus(long seed, String[] args) {
    super(seed);
    this.params = new Parameters(args);
    this.time = new TimeManager();
    this.facility = new Facility();
    this.setAllFamilies(new Bag());
    this.familyHousing = new Bag();
    this.works = new Bag();
    this.allFacilities = new Bag();
    this.allCampGeoGrid = new GeomGridField();
    this.schooles = new Bag();
    this.healthCenters = new Bag();
    this.mosques = new Bag();
    this.market = new Bag();
    this.foodCenter = new Bag();
    this.other = new Bag();
    this.climate = new Climate();
    this.currentDay = 0;
    this.temperature = 0;
    this.precipitation = 0;
    this.waterAccumulationInHouses = 0;
    this.totalOfHumansSusceptible = 0;
    this.totalOfHumansExposed = 0;
    this.totalOfHumansWithMildInfection = 0;
    this.totalOfHumansWithSevereInfected = 0;
    this.totalOfHumansWithToxicInfected = 0;
    this.totalOfHumansRecovered = 0;
    this.amountDeadHumans = 0;
    this.totalVisitsMedicalCenter = 0;
    this.maximumCapacityInDay = false;
  }

  public void start() {
    super.start();
    SimulationBuilder builder = new SimulationBuilder();
    builder.create(this, this.random);

    schedule.scheduleRepeating(facility, Facility.ORDERING, 1);

    this.outputStatsToReporting(schedule);

    Steppable updater = new Steppable() {
      private static final long serialVersionUID = 1L;

      // all graphs and charts wll be updated in each steps
      public void step(SimState state) {
        if (isNewDay()) {
          // reset values
          waterAccumulationInHouses = 0;
          maximumCapacityInDay = false;
          amountDeadHumans = 0;

          // define temperature
          List<Double> temperatures = climate.getTemperature();
          if (currentDay < temperatures.size()) {
            temperature = temperatures.get(currentDay);
          }

          // define precipitation
          List<Double> rainfall = climate.getPrecipitation();
          double mm = params.getGlobal().getWaterAbsorption();
          for (Object object : getFamilyHousing()) {
            Building housing = (Building) object;
            double probability = params.getGlobal().getProbabilityOfChangeOverDepositedWater();
            if (probability >= random.nextDouble()) { // 50% chance
              housing.waterAbsorption(mm);
              if (currentDay < rainfall.size()) {
                housing.addWater(rainfall.get(currentDay));
                // used to the statistics
                precipitation = rainfall.get(currentDay);
              }
              // used to the statistics
              waterAccumulationInHouses += housing.getWater();
            }
          }
        }

        // garbage collector
        for (Object human : allHumans.getAllObjects()) {
          Human h = (Human) human;
          if (h.isDead()) {
            h.getFamily().removeMembers(h);
            if (h.getFamily().getMembers().isEmpty()) {
              getAllFamilies().remove(h.getFamily());
            }
            allHumans.allObjects.remove(h);
            // used to the statistics
            amountDeadHumans++;
          }
        }

        // getting all humans
        Bag humans = allHumans.getAllObjects();
        int totalOfHumansSusceptible = 0;
        int totalOfHumansExposed = 0;
        int totalOfHumansWithMildInfection = 0;
        int totalOfHumansWithSevereInfected = 0;
        int totalOfHumansWithToxicInfected = 0;
        int totalOfHumansRecovered = 0;
        for (int i = 0; i < humans.numObjs; i++) {
          Human human = (Human) humans.objs[i];
          if (HealthStatus.SUSCEPTIBLE.equals(human.getCurrentHealthStatus())) {
            totalOfHumansSusceptible++;
          } else if (HealthStatus.EXPOSED.equals(human.getCurrentHealthStatus())) {
            totalOfHumansExposed++;
          } else if (HealthStatus.MILD_INFECTION.equals(human.getCurrentHealthStatus())) {
            totalOfHumansWithMildInfection++;
          } else if (HealthStatus.SEVERE_INFECTION.equals(human.getCurrentHealthStatus())) {
            totalOfHumansWithSevereInfected++;
          } else if (HealthStatus.TOXIC_INFECTION.equals(human.getCurrentHealthStatus())) {
            totalOfHumansWithToxicInfected++;
          } else if (human.getCurrentHealthStatus().equals(HealthStatus.RECOVERED)) {
            totalOfHumansRecovered++;
          }
        }

        setTotalOfHumansSusceptible(totalOfHumansSusceptible);
        setTotalOfHumansExposed(totalOfHumansExposed);
        setTotalOfHumansWithMildInfection(totalOfHumansWithMildInfection);
        setTotalOfHumansWithSevereInfected(totalOfHumansWithSevereInfected);
        setTotalOfHumansWithToxicInfected(totalOfHumansWithToxicInfected);
        setTotalOfHumansRecovered(totalOfHumansRecovered);
        setAmountDeadHumans(amountDeadHumans);

        totalTotalPopSeries.add((double) (state.schedule.getTime()), allHumans.getAllObjects().numObjs);
        totalDeathSeries.add((double) (state.schedule.getTime()), amountDeadHumans);
        totalSusceptibleSeries.add((double) (state.schedule.getTime()), (totalOfHumansSusceptible));
        totalExposedSeries.add((double) (state.schedule.getTime()), (totalOfHumansExposed));
        totalMildInfectedSeries.add((double) (state.schedule.getTime()), (totalOfHumansWithMildInfection));
        totalSevereInfectedSeries.add((double) (state.schedule.getTime()), (totalOfHumansWithSevereInfected));
        totalToxicInfectedSeries.add((double) (state.schedule.getTime()), (totalOfHumansWithToxicInfected));
        totalRecoveredSeries.add((double) (state.schedule.getTime()), (totalOfHumansRecovered));

        int totalOfMosquitoSusceptible = 0;
        int totalOfMosquitoExposed = 0;
        int totalOfMosquitoesWithInfection = 0;

        int m = ((int) state.schedule.getTime()) % 60;

        double t = (getTime().currentHour((int) state.schedule.getTime())) + (m / 60.0);
        int h = getTime().dayCount((int) state.schedule.getTime());
        hourDialer.setValue(t);
        dayDialer.setValue(h);
      }
    };
    schedule.scheduleRepeating(updater);
  }

  private boolean isNewDay() {
    if (time.dayCount((int) schedule.getSteps()) > currentDay) {
      currentDay = time.dayCount((int) schedule.getSteps());
      return true;
    } else {
      return false;
    }
  }

  private boolean isMidnight() {
    return this.schedule.getSteps() % 1440 == 1439;
  }

  private void outputStatsToReporting(Schedule schedule) {
    this.report = new CoronaVirusReport(this);
    schedule.scheduleRepeating(this.report, CoronaVirusReport.ORDERING, 1.0);
  }

  public String getCurrentDayOfWeek() {
    int day = this.time.dayCount((int) schedule.getSteps());
    return String.valueOf(DayOfWeek.getDayOfWeek(day));
  }

  public void setInitialTemperature(double initial) {
    this.temperature = initial;
  }

  public void setInitialPrecipitation(double initial) {
    double mm = params.getGlobal().getWaterAbsorption();
    for (Object object : getFamilyHousing()) {
      Building housing = (Building) object;
      if (random.nextDouble() <= 0.5) { // 50% chance
        housing.waterAbsorption(mm);
        housing.addWater(initial);
      }
    }
  }

  public static void main(String[] args) {
    doLoop(CoronaVirus.class, args);
    System.exit(0);
  }

  public void finish() {
    super.finish();
    if (report != null) {
      this.report.finish();
    }
  }

  // used to the statistics
  public void addVisitToMedicalCenter() {
    this.totalVisitsMedicalCenter++;
  }

  public Parameters getParams() {
    return params;
  }

  public DefaultCategoryDataset getDataset() {
    return this.dataset;
  }

  public void setDataset(DefaultCategoryDataset dataset) {
    this.dataset = dataset;
  }

  public TimeManager getTime() {
    return time;
  }

  public Climate getClimate() {
    return climate;
  }

  public void setClimate(Climate climate) {
    this.climate = climate;
  }

  public int getCurrentDay() {
    return currentDay;
  }

  public void setCurrentDay(int currentDay) {
    this.currentDay = currentDay;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public Bag getAllFamilies() {
    return allFamilies;
  }

  public void setAllFamilies(Bag allFamilies) {
    this.allFamilies = allFamilies;
  }

  public Bag getWorks() {
    return works;
  }

  public void setWorks(Bag works) {
    this.works = works;
  }

  public Bag getAllFacilities() {
    return allFacilities;
  }

  public void setAllFacilities(Bag allFacilities) {
    this.allFacilities = allFacilities;
  }

  public Bag getSchooles() {
    return schooles;
  }

  public void setSchooles(Bag schooles) {
    this.schooles = schooles;
  }

  public Bag getHealthCenters() {
    return healthCenters;
  }

  public void setHealthCenters(Bag healthCenters) {
    this.healthCenters = healthCenters;
  }

  public Bag getMosques() {
    return mosques;
  }

  public void setMosques(Bag mosques) {
    this.mosques = mosques;
  }

  public Bag getFoodCenter() {
    return foodCenter;
  }

  public void setFoodCenter(Bag foodCenter) {
    this.foodCenter = foodCenter;
  }

  public Bag getOther() {
    return other;
  }

  public void setOther(Bag other) {
    this.other = other;
  }

  public Bag getFamilyHousing() {
    return familyHousing;
  }

  public void setFamilyHousing(Bag familyHousing) {
    this.familyHousing = familyHousing;
  }

  public Bag getMarket() {
    return market;
  }

  public void setMarket(Bag market) {
    this.market = market;
  }

  public int getTotalOfHumansSusceptible() {
    return totalOfHumansSusceptible;
  }

  public void setTotalOfHumansSusceptible(int totalOfHumansSusceptible) {
    this.totalOfHumansSusceptible = totalOfHumansSusceptible;
  }

  public int getTotalOfHumansExposed() {
    return totalOfHumansExposed;
  }

  public void setTotalOfHumansExposed(int totalOfHumansExposed) {
    this.totalOfHumansExposed = totalOfHumansExposed;
  }

  public int getTotalOfHumansWithMildInfection() {
    return totalOfHumansWithMildInfection;
  }

  public void setTotalOfHumansWithMildInfection(int totalOfHumansWithMildInfection) {
    this.totalOfHumansWithMildInfection = totalOfHumansWithMildInfection;
  }

  public int getTotalOfHumansWithSevereInfected() {
    return totalOfHumansWithSevereInfected;
  }

  public void setTotalOfHumansWithSevereInfected(int totalOfHumansWithSevereInfected) {
    this.totalOfHumansWithSevereInfected = totalOfHumansWithSevereInfected;
  }

  public int getTotalOfHumansWithToxicInfected() {
    return totalOfHumansWithToxicInfected;
  }

  public void setTotalOfHumansWithToxicInfected(int totalOfHumansWithToxicInfected) {
    this.totalOfHumansWithToxicInfected = totalOfHumansWithToxicInfected;
  }

  public int getTotalOfHumansRecovered() {
    return totalOfHumansRecovered;
  }

  public void setTotalOfHumansRecovered(int totalOfHumansRecovered) {
    this.totalOfHumansRecovered = totalOfHumansRecovered;
  }

  public int getTotalVisitsMedicalCenter() {
    return totalVisitsMedicalCenter;
  }

  public void setTotalVisitsMedicalCenter(int totalVisitsMedicalCenter) {
    this.totalVisitsMedicalCenter = totalVisitsMedicalCenter;
  }

  public int getAmountDeadHumans() {
    return amountDeadHumans;
  }

  public void setAmountDeadHumans(int amountDeadHumans) {
    this.amountDeadHumans = amountDeadHumans;
  }

  public double getPrecipitation() {
    return precipitation;
  }

  public void setPrecipitation(double precipitation) {
    this.precipitation = precipitation;
  }

  public boolean isMaximumCapacityInDay() {
    return maximumCapacityInDay;
  }

  public void setMaximumCapacityInDay(boolean maximumCapacity) {
    this.maximumCapacityInDay = maximumCapacity;
  }

  public double getWaterAccumulationInHouses() {
    return waterAccumulationInHouses;
  }

  public void setWaterAccumulationInHouses(double waterAccumulationInHouse) {
    this.waterAccumulationInHouses = waterAccumulationInHouse;
  }

}

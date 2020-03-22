package com.coronavirus.model;

import java.io.Serializable;

import com.coronavirus.core.CoronaVirus;

import sim.util.Bag;
import sim.util.Valuable;

public class Building implements Valuable, Serializable {

  private static final long serialVersionUID = 1L;
  private Facility facility;
  private Bag refugeeHH; // camp location for household
  private Bag humans; // who are on the field right now
  private int fieldID; // identify the type pf the field
  private int campID; // holds id of the three camps
  private double water; // hold water amount
  private boolean sap;
  private boolean nectar;
  private double elevation; // elevation
  private double timeOfMaturation;
  private int amountOfResources;
  private int quantityOfVaccines;
  private int locationX;
  private int locationY;
  private int patientCounter;

  public Building() {
    this.refugeeHH = new Bag();
    this.humans = new Bag();
    this.timeOfMaturation = 0;
    this.amountOfResources = 0;
    this.quantityOfVaccines = 0;
    this.water = 0;
    this.patientCounter = 0;
  }

  public Building(int x, int y) {
    this.humans = new Bag();
    this.refugeeHH = new Bag();
    this.locationX = x;
    this.locationY = y;
    this.timeOfMaturation = 0;
    this.amountOfResources = 0;
    this.quantityOfVaccines = 0;
    this.water = 0;
    this.patientCounter = 0;
  }

  // check how many familes can occupied in a field
  public synchronized boolean isCampOccupied(CoronaVirus dadaab) {
    if (this.getRefugeeHH().size() >= dadaab.getParams().getGlobal().getMaximumFamilyOccumpancyPerBuilding()) {
      return true;
    } else {
      return false;
    }
  }

  public synchronized boolean equals(Building b) {
    if (b.getLocationX() == this.getLocationX() && b.getLocationY() == this.getLocationY()) {
      return true;
    } else {
      return false;
    }
  }

  public synchronized boolean equals(int x, int y) {
    if (x == this.getLocationX() && y == this.getLocationY()) {
      return true;
    }
    return false;
  }

  // calaculate distance
  public double distanceTo(Building b) {
    return Math.sqrt(
        Math.pow(b.getLocationX() - this.getLocationX(), 2) + Math.pow(b.getLocationY() - this.getLocationY(), 2));
  }

  public double distanceTo(int xCoord, int yCoord) {
    return Math.sqrt(Math.pow(xCoord - this.getLocationX(), 2) + Math.pow(yCoord - this.getLocationY(), 2));
  }

  public Building copy() {
    Building fieldUnit = new Building(this.getLocationX(), this.getLocationY());
    return fieldUnit;
  }

  public void setRefugeeHH(Bag refugees) {
    this.refugeeHH = refugees;
  }

  public Bag getRefugeeHH() {
    return refugeeHH;
  }

  public void addRefugeeHH(Family r) {
    this.refugeeHH.add(r);
  }

  public void removeRefugeeHH(Family r) {
    this.refugeeHH.remove(r);
  }

  public void setRefugee(Bag humans) {
    this.humans = humans;
  }

  public Bag getHumans() {
    return humans;
  }

  public void addRefugee(Human r) {
    this.humans.add(r);
  }

  public void removeRefugee(Human r) {
    this.humans.remove(r);
  }

  public void setFieldID(int id) {
    this.fieldID = id;
  }

  public int getFieldID() {
    return fieldID;
  }

  public void setCampID(int id) {
    this.campID = id;
  }

  public int getCampID() {
    return campID;
  }

  public void setFacility(Facility f) {
    this.facility = f;
  }

  public Facility getFacility() {
    return facility;
  }

  // water - either from borehole or rainfall
  public void setWater(double flow) {
    this.water = flow;
  }

  public double getWater() {
    return water;
  }

  public void addWater(double water) {
    this.water = this.water + water;
  }

  public void waterAbsorption(double absorption) {
    this.water = this.water - absorption;
  }

  public void setElevation(double elev) {
    this.elevation = elev;
  }

  public double getElevation() {
    return elevation;
  }

  final public int getLocationX() {
    return locationX;
  }

  final public void setLocationX(int x) {
    this.locationX = x;
  }

  final public int getLocationY() {
    return locationY;
  }

  final public void setLocationY(int y) {
    this.locationY = y;
  }

  public double doubleValue() {
    return getCampID();
  }

  public boolean containsSap() {
    return this.sap;
  }

  public void setSap(boolean sap) {
    this.sap = sap;
  }

  public boolean containsNectar() {
    return this.nectar;
  }

  public void setNectar(boolean nectar) {
    this.nectar = nectar;
  }

  public boolean containsWater() {
    return this.water != 0;
  }

  public boolean containsHumans() {
    return this.humans.size() > 0;
  }
  
  public double getTimeOfMaturation() {
    return timeOfMaturation;
  }

  public void setTimeOfMaturation(double timeOfMaturation) {
    this.timeOfMaturation = timeOfMaturation;
  }

  public int getAmountOfResources() {
    return amountOfResources;
  }

  public void setAmountOfResources(int amountOfResources) {
    this.amountOfResources = amountOfResources;
  }

  public int getQuantityOfVaccines() {
    return quantityOfVaccines;
  }

  public void setQuantityOfVaccines(int quantityOfVaccines) {
    this.quantityOfVaccines = quantityOfVaccines;
  }

  // used to the define resources
  public void addPatient() {
    this.patientCounter++;
  }

  public void removePatient() {
    this.patientCounter--;
  }

  public int getPatientCounter() {
    return patientCounter;
  }

}
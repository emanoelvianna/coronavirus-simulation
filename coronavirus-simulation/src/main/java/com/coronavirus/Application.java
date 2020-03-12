package com.coronavirus;

import sim.engine.SimState;
import sim.field.geo.GeomVectorField;
import sim.field.grid.ObjectGrid2D;

public class Application extends SimState {

  private static final long serialVersionUID = 3104821895646282441L;
  
  public GeomVectorField shapefile;
  public ObjectGrid2D closestNodes;

  public Application(long seed, String[] args) {
    super(seed);
  }

  public static void main(String[] args) {
    System.out.println("Fuck you corona!");
  }

}

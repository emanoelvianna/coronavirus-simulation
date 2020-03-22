package com.coronavirus.core;

import java.util.ArrayList;

import com.coronavirus.model.Building;

import sim.field.network.Edge;

public class Node {

  private Building location;
  private ArrayList<Edge> links;

  public Node(Building l) {
    setLocation(l);
    setLinks(new ArrayList<Edge>());
  }

  public Building getLocation() {
    return location;
  }

  public void setLocation(Building location) {
    this.location = location;
  }

  public ArrayList<Edge> getLinks() {
    return links;
  }

  public void setLinks(ArrayList<Edge> links) {
    this.links = links;
  }

}
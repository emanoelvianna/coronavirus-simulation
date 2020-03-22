package com.coronavirus.model;

import java.util.ArrayList;
import java.util.List;

public class Climate {
  private List<String> date;
  private List<Double> temperature;
  private List<Double> precipitation;

  public Climate() {
    this.date = new ArrayList<String>();
    this.temperature = new ArrayList<Double>();
    this.precipitation = new ArrayList<Double>();
  }

  public List<String> getDate() {
    return date;
  }

  public void addDate(String date) {
    this.date.add(date);
  }

  public List<Double> getTemperature() {
    return temperature;
  }

  public void addTemperature(Double temperature) {
    this.temperature.add(temperature);
  }

  public List<Double> getPrecipitation() {
    return precipitation;
  }

  public void addPrecipitation(Double precipitation) {
    this.precipitation.add(precipitation);
  }
}

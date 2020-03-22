package com.coronavirus.model.enumeration;

public enum HealthStatus {
  SUSCEPTIBLE, EXPOSED, MILD_INFECTION, SEVERE_INFECTION, TOXIC_INFECTION, RECOVERED, DEAD, INFECTED;

  public static HealthStatus typeOfHealthStatus(HealthStatus currentHealthStatus) {
    switch (currentHealthStatus) {
    case SUSCEPTIBLE:
      return HealthStatus.SUSCEPTIBLE;
    case EXPOSED:
      return HealthStatus.EXPOSED;
    case MILD_INFECTION:
      return HealthStatus.MILD_INFECTION;
    case SEVERE_INFECTION:
      return HealthStatus.SEVERE_INFECTION;
    case TOXIC_INFECTION:
      return HealthStatus.TOXIC_INFECTION;
    default:
      return HealthStatus.SUSCEPTIBLE;
    }
  }

  public static boolean isHumanInfected(HealthStatus currentHealthStatus) {
    switch (currentHealthStatus) {
    case MILD_INFECTION:
      return true;
    case SEVERE_INFECTION:
      return true;
    case TOXIC_INFECTION:
      return true;
    default:
      return false;
    }
  }

  public static boolean isHumanExposed(HealthStatus currentHealthStatus) {
    switch (currentHealthStatus) {
    case EXPOSED:
      return true;
    default:
      return false;
    }
  }
}

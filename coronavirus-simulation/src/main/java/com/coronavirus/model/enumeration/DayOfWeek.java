package com.coronavirus.model.enumeration;

public enum DayOfWeek {

  SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

  // Simplicity we consider that the day zero is monday
  public static DayOfWeek getDayOfWeek(int currentDay) {
    switch (currentDay) {
    case 0:
      return MONDAY;
    case 1:
      return TUESDAY;
    case 2:
      return WEDNESDAY;
    case 3:
      return THURSDAY;
    case 4:
      return FRIDAY;
    case 5:
      return SATURDAY;
    default:
      return SUNDAY;
    }
  }

}

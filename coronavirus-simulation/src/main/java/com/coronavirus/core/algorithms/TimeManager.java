package com.coronavirus.core.algorithms;

public class TimeManager {
  final int HOURTIME = 60; // minute
  final int DURATION = 24; // 1 day is 12 hour - no need to simulate night time
  final int WEEKDURATION = 7; // 1 day = 12 hour, 1 week = 7 day = 7 * 12

  public int currentHour(int currentStep) {
    int h = 1;
    int t = ((int) currentStep) % HOURTIME;
    int m = ((int) currentStep - (t)) / HOURTIME;
    if (m <= DURATION) {
      h = m;
    } else {
      h = (int) m % DURATION;
    }

    return h;
  }

  public int currentDayInWeek(int currentStep) {
    int time = (int) currentStep % (HOURTIME * DURATION);
    int day = ((int) currentStep - (time)) / (HOURTIME * DURATION);
    int week = 0;

    if (day < WEEKDURATION) {
      week = day;
    } else {
      week = (int) day % WEEKDURATION;
    }

    return week + 1;
  }

  public int dayCount(int currentStep) {
    int time = (int) currentStep % (HOURTIME * DURATION);
    int day = ((int) currentStep - (time)) / (HOURTIME * DURATION);

    return day + 1;
  }
}

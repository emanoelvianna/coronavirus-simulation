package com.coronavirus.model;

import com.coronavirus.core.CoronaVirus;
import com.coronavirus.core.algorithms.TimeManager;
import com.coronavirus.model.enumeration.ActivityMapping;

import ec.util.MersenneTwisterFast;
import sim.util.Bag;

public class Activity {

  private MersenneTwisterFast random;
  private Human human;
  private TimeManager time;
  private int currentStep;
  private int minuteInDay;

  public Activity(Human human, TimeManager time, MersenneTwisterFast random, int currentStep, int minuteInDay) {
    this.random = random;
    this.human = human;
    this.time = time;
    this.currentStep = currentStep;
    this.minuteInDay = minuteInDay;
  }

  public ActivityMapping defineActivity(CoronaVirus yellowFever) {
    if (this.gettingMedicalHelp(yellowFever)) {
      return ActivityMapping.HEALTH_CENTER;
    } else if (this.human.hasSymptomsOfInfection()) {
      return ActivityMapping.STAY_HOME;
    }
    return defineActivitiesAccordingToSomeCriterion(yellowFever);
  }

  private ActivityMapping defineActivitiesAccordingToSomeCriterion(CoronaVirus yellowFever) {
    synchronized (this.random) {
      if (this.minuteInDay >= (8 * 60) && this.minuteInDay <= (18 * 60)) {
        if (time.currentDayInWeek(currentStep) < 6) {
          double probability = yellowFever.getParams().getGlobal().getProbabilityOfHumanLeavingLateFromHome();
          if (probability >= this.random.nextDouble()) { // 1% chance stay home
            return ActivityMapping.STAY_HOME;
          } else if (this.human.isWorker()) {
            return ActivityMapping.WORK;
          } else if (this.human.isStudent()) {
            return this.everydayActivitiesForStudents();
          } else {
            return this.differentActivities();
          }
        } else {
          return differentActivities();
        }
      }
      return ActivityMapping.STAY_HOME;
    }
  }

  public ActivityMapping everydayActivitiesForStudents() {
    synchronized (this.random) {
      if (this.minuteInDay >= (8 * 60) && this.minuteInDay <= (12 * 60)) {
        return ActivityMapping.SCHOOL;
      } else if (0.5 >= this.random.nextDouble()) { // 50% chance
        if (0.4 >= this.random.nextDouble()) // 40% chance
          return ActivityMapping.SOCIAL_VISIT;
        else if (0.3 >= this.random.nextDouble()) // 30% chance
          return ActivityMapping.RELIGION_ACTIVITY;
        else
          return ActivityMapping.MARKET;
      }
      return ActivityMapping.STAY_HOME;
    }
  }

  public ActivityMapping differentActivities() {
    synchronized (this.random) {
      if (0.8 >= this.random.nextDouble()) { // 80% chance
        if (0.4 >= this.random.nextDouble()) { // 40% chance
          return ActivityMapping.SOCIAL_VISIT;
        } else if (0.3 >= this.random.nextDouble()) { // 30% chance
          return ActivityMapping.RELIGION_ACTIVITY;
        } else {
          return ActivityMapping.MARKET;
        }
      } else { // 20% chance of home activity
        return ActivityMapping.STAY_HOME;
      }
    }
  }

  public Building bestActivityLocation(Human human, Building position, ActivityMapping activityMapping, CoronaVirus d) {
    switch (activityMapping) {
    case STAY_HOME:
      return human.getHome();
    case WORK:
      return betstLocation(human.getHome(), d.getWorks(), d);
    case SCHOOL:
      return betstLocation(human.getHome(), d.getSchooles(), d);
    case RELIGION_ACTIVITY:
      return betstLocation(human.getHome(), d.getMosques(), d);
    case MARKET:
      return betstLocation(human.getHome(), d.getMarket(), d);
    case HEALTH_CENTER:
      return betstLocation(human.getHome(), d.getHealthCenters(), d);
    case SOCIAL_VISIT:
      return socialize(human, d);
    default:
      return human.getHome();
    }
  }

  public int stayingPeriod(ActivityMapping activityMapping) {
    final int MINUTE = 60;
    int period = 0;
    int minimumStay = 20; // minimum delay
    int maximumStay = 180; // three hour

    switch (activityMapping) {
    case STAY_HOME:
      period = maximumStay;
      break;
    case SCHOOL:
      // time at school maximum until ~12:00 pm
      period = 4 * MINUTE;
      break;
    case WORK:
      // time at work maximum until ~19:00 pm
      period = 10 * MINUTE;
      break;
    case SOCIAL_VISIT:
      // maximum time up to 8 hours
      synchronized (this.random) {
        period = minimumStay + this.random.nextInt(8 * MINUTE);
      }
      break;
    case RELIGION_ACTIVITY:
      // maximum time up to 4 hours
      synchronized (this.random) {
        period = minimumStay + this.random.nextInt(4 * MINUTE);
      }
      break;
    case MARKET:
      // maximum time up to 2 hours
      synchronized (this.random) {
        period = minimumStay + this.random.nextInt(2 * MINUTE);
      }
      break;
    case HEALTH_CENTER:
      // maximum time up to 24 hours
      synchronized (this.random) {
        period = 4 + this.random.nextInt(20 * MINUTE);
      }
      break;
    }
    return (period + this.minuteInDay);
  }

  public void doActivity(ActivityMapping activityMapping) {
    switch (activityMapping) {
    case STAY_HOME:
      break;
    case HEALTH_CENTER:
      this.human.receiveTreatment();
      break;
    default:
    }
  }

  private Building betstLocation(Building fLoc, Bag fieldBag, CoronaVirus yellowFever) {
    Bag newLoc = new Bag();
    double bestScoreSoFar = Double.POSITIVE_INFINITY;
    for (int i = 0; i < fieldBag.numObjs; i++) {
      Building potLoc = ((Building) fieldBag.objs[i]);
      double fScore = fLoc.distanceTo(potLoc);
      if (fScore > bestScoreSoFar) {
        continue;
      }
      if (fScore <= bestScoreSoFar) {
        bestScoreSoFar = fScore;
        newLoc.clear();
      }
      newLoc.add(potLoc);
    }
    Building f = null;
    if (newLoc != null) {
      int winningIndex = 0;
      if (newLoc.numObjs >= 1) {
        winningIndex = yellowFever.random.nextInt(newLoc.numObjs);
      }
      // System.out.println("other" + newLoc.numObjs);
      f = (Building) newLoc.objs[winningIndex];
    }
    return f;
  }

  public Building getNextTile(CoronaVirus yellowFever, Building subgoal, Building position) {
    // move in which direction?
    int moveX = 0, moveY = 0;
    int dx = subgoal.getLocationX() - position.getLocationX();
    int dy = subgoal.getLocationY() - position.getLocationY();
    if (dx < 0) {
      moveX = -1;
    } else if (dx > 0) {
      moveX = 1;
    }
    if (dy < 0) {
      moveY = -1;
    } else if (dy > 0) {
      moveY = 1;
    }
    // can either move in Y direction or X direction: see which is better
    Building xmove = ((Building) yellowFever.allCamps.field[position.getLocationX() + moveX][position.getLocationY()]);
    Building ymove = ((Building) yellowFever.allCamps.field[position.getLocationX()][position.getLocationY() + moveY]);

    boolean xmoveToRoad = ((Integer) yellowFever.roadGrid.get(xmove.getLocationX(), xmove.getLocationY())) > 0;
    boolean ymoveToRoad = ((Integer) yellowFever.roadGrid.get(ymove.getLocationX(), ymove.getLocationX())) > 0;

    if (moveX == 0 && moveY == 0) {
      // we are ON the subgoal, so don't move at all
      // both are the same result, so just return the xmove (which is identical)
      return xmove;
    } else if (moveX == 0) {
      // this means that moving in the x direction is not a valid move: it's +0
      return ymove;
    } else if (moveY == 0) {
      // this means that moving in the y direction is not a valid move: it's +0
      return xmove;
    } else if (xmoveToRoad == ymoveToRoad) {
      // equally good moves: pick randomly between them
      if (yellowFever.random.nextBoolean()) {
        return xmove;
      } else {
        return ymove;
      }
    } else if (xmoveToRoad && moveX != 0) { // x is a road: pick it
      return xmove;
    } else if (ymoveToRoad && moveY != 0) { // yes// y is a road: pick it
      return ymove;
    } else if (moveX != 0) { // move in the better direction
      return xmove;
    } else if (moveY != 0) { // yes
      return ymove;
    } else {
      return ymove; // no justification
    }
  }

  private Building socialize(Human ref, CoronaVirus yellowFever) {
    Bag potential = new Bag();
    Building newLocation = null;
    potential.clear();
    int camp = ref.getHome().getCampID(); // get camp id
    // select any camp site but not the camp that belong to the agent
    for (Object campsite : yellowFever.getFamilyHousing()) {
      Building cmp = ((Building) campsite);
      if (cmp.getCampID() == camp && cmp.equals(ref.getHome()) != true && cmp.getRefugeeHH().numObjs > 0) {
        potential.add(cmp); // potential locations to visit
      }
    }

    if (potential.numObjs == 1) {
      newLocation = (Building) potential.objs[0];
    } else {
      newLocation = (Building) potential.objs[yellowFever.random.nextInt(potential.numObjs)];
    }

    return newLocation;
  }

  private boolean gettingMedicalHelp(CoronaVirus yellowFever) {
    synchronized (this.random) {
      double probability = yellowFever.getParams().getGlobal().getProbabilityToGoGettingMedicalHelp();
      if (this.human.hasSymptomsOfInfection() && !this.human.getReceivedTreatment())
        if (probability >= this.random.nextDouble())
          return true;
      return false;
    }
  }

  public Human getRefugee() {
    return human;
  }

  public void setRefugee(Human human) {
    this.human = human;
  }

}

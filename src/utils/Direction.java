package utils;

import java.util.concurrent.ThreadLocalRandom;

public enum Direction {
  UP,
  RIGHT,
  DOWN,
  LEFT;

  public static Direction random() {
    return Direction.values()[ThreadLocalRandom.current().nextInt(4)];
  }
}

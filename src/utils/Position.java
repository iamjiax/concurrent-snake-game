package utils;

import java.util.concurrent.ThreadLocalRandom;

public class Position {
  private int row;
  private int col;

  public Position(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public static Position random(int size) {
    return new Position(
        ThreadLocalRandom.current().nextInt(size), ThreadLocalRandom.current().nextInt(size));
  }

  public static Position copy(Position o) {
    return new Position(o.getRow(), o.getCol());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Position)) {
      return false;
    }
    Position op = (Position) o;
    return op.getRow() == row && op.getCol() == col;
  }

  @Override
  public int hashCode() {
    return row * 31 + col;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", row, col);
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public Position move(Direction d) {
    switch (d) {
      case UP:
        row = row - 1;
        break;
      case RIGHT:
        col = col + 1;
        break;
      case DOWN:
        row = row + 1;
        break;
      case LEFT:
        col = col - 1;
        break;
    }
    return this;
  }

  public boolean outBound(int size) {
    return row < 0 || row >= size || col < 0 || col >= size;
  }
}

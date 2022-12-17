package snakeServer.model;

import static utils.Config.BOARD_SIZE;

import utils.Direction;
import utils.Position;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Player {
  private String name;
  private InetAddress IP;
  private int port;

  private LinkedList<Position> snake;
  private Direction dir;
  private boolean alive;

  public Player(String name, InetAddress IP, int port) {
    this.name = name;
    this.IP = IP;
    this.port = port;
    this.alive = true;
  }

  protected void initSnake() {
    Position head = Position.random(BOARD_SIZE);
    snake =
        Stream.of(
                head,
                Position.copy(head).move(Direction.DOWN),
                Position.copy(head).move(Direction.DOWN).move(Direction.DOWN))
            .collect(Collectors.toCollection(LinkedList::new));
    do {
      this.dir = Direction.random();
    } while (Position.copy(snake.getFirst()).move(this.dir).equals(snake.get(1)));
  }

  public String getName() {
    return this.name;
  }

  public InetAddress getIP() {
    return this.IP;
  }

  public int getPort() {
    return this.port;
  }

  public LinkedList<Position> getSnake() {
    return this.snake;
  }

  public Direction getDir() {
    return this.dir;
  }

  public boolean isAlive() {
    return this.alive;
  }

  public Position getHead() {
    return this.snake.getFirst();
  }

  protected boolean isInSnake(Position o) {
    return snake.stream().anyMatch(p -> p.equals(o));
  }

  protected boolean isInSnakeButNotTail(Position o) {
    for (Position p : snake) {
      if (p.equals(snake.getLast())) {
        continue;
      }
      if (p.equals(o)) {
        return true;
      }
    }
    return false;
  }

  public void updateDirection(Direction newDir) {
    Position next = Position.copy(snake.getFirst()).move(newDir);
    if (next.equals(snake.get(1))) {
      return;
    }
    dir = newDir;
  }

  public void moveSnake(Position nextHead) {
    snake.addFirst(nextHead);
    snake.removeLast();
  }

  public void eatApple(Position apple) {
    snake.addFirst(apple);
  }

  public void die() {
    this.alive = false;
  }
}

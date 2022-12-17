package snakeClient.view;

import static utils.Config.*;

import utils.Position;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import snakeClient.model.GameState;

public class Board extends JPanel {
  private static final Image GREEN = new ImageIcon("src/snakeClient/images/green.png").getImage();
  private static final Image APPLE = new ImageIcon("src/snakeClient/images/apple.png").getImage();
  private static final Image RED = new ImageIcon("src/snakeClient/images/red.png").getImage();

  public Board() {
    int size = UNIT_SIZE * BOARD_SIZE;
    setPreferredSize(new Dimension(size, size));
    setBorder(BorderFactory.createLineBorder(Color.BLUE));
    setBackground(Color.BLACK);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    int state = GameState.get().getState();
    switch (state) {
      case WAIT_PLAYER:
        renderWaitPlayer(g);
        break;
      case WAIT_START:
        renderWaitStart(g);
        break;
      case GAME_OVER:
        renderApple(g);
        renderMySnake(g);
        renderOpponentSnake(g);
        renderGameOver(g);
        break;
      case IN_PROGRESS:
        renderApple(g);
        renderMySnake(g);
        renderOpponentSnake(g);
        break;
    }
  }

  private void renderWaitPlayer(Graphics g) {
    renderText(g, "waiting for opponent");
  }

  private void renderWaitStart(Graphics g) {
    renderText(g, "game is about to start");
  }

  private void renderApple(Graphics g) {
    Position p = GameState.get().getApplePosition();
    render(g, APPLE, p);
  }

  private void renderMySnake(Graphics g) {
    LinkedList<Position> snake1 = GameState.get().getSnake1();
    if (snake1 == null) return;
    snake1.stream().forEach(p -> render(g, GREEN, p));
  }

  private void renderOpponentSnake(Graphics g) {
    LinkedList<Position> snake2 = GameState.get().getSnake2();
    if (snake2 == null) return;
    snake2.stream().forEach(p -> render(g, RED, p));
  }

  private void renderGameOver(Graphics g) {
    String winnerName = GameState.get().getWinnerName();
    if (winnerName != null) {
      renderText(g, winnerName + " is winner");
    } else {
      renderText(g, "it is a draw");
    }
  }

  private void renderText(Graphics g, String text) {
    g.setColor(Color.BLUE);
    Font font = new Font("TimesRoman", Font.PLAIN, 30);
    FontMetrics metrics = g.getFontMetrics(font);
    int x = (BOARD_SIZE * UNIT_SIZE - metrics.stringWidth(text)) / 2;
    int y = (BOARD_SIZE * UNIT_SIZE - metrics.getHeight()) / 2 + metrics.getAscent();
    g.setFont(font);
    g.drawString(text, x, y);
  }

  private void render(Graphics g, Image image, Position p) {
    if (p == null) return;
    g.drawImage(image, p.getCol() * UNIT_SIZE, p.getRow() * UNIT_SIZE, this);
  }
}

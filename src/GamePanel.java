import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {

  static int GAP = 2;
  public Paddle paddle;
  public Ball ball;
  public Block blocks[][];
  public Thread th;

  private int score = 0;

  public GamePanel() {
    System.out.println("Game Panel 생성자");
    this.setBackground(Color.BLACK);
    this.setPreferredSize(new Dimension(60 * 10 + GAP * 9, 900));

    th = new Thread(this);
    th.start();

    paddle = new Paddle();
    ball = new Ball();
    ball.x = paddle.x + 35;
    ball.y = paddle.y - 10;
    blocks = new Block[5][10];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 10; j++) {
        blocks[i][j] = new Block();
        blocks[i][j].x = (60 + GAP) * j;
        blocks[i][j].y = (30 + GAP) * i;
        blocks[i][j].color = i;
        if (j % 2 == 0) {
          blocks[i][j].isHide = true;
        }
      }
    }

    this.addKeyListener(
        new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_LEFT) {
              paddle.isLeft = true;
              paddle.isRight = false;
            } else if (keyCode == KeyEvent.VK_RIGHT) {
              paddle.isLeft = false;
              paddle.isRight = true;
            } else if (keyCode == KeyEvent.VK_SPACE) {
              ball.shoot();
            }
          }

          public void keyReleased(KeyEvent e) {
            paddle.isLeft = false;
            paddle.isRight = false;
          }
        }
      );
    this.setFocusable(true);
    this.requestFocus();
  }

  public void paintComponent(Graphics g) {
    // super.paintComponents(g);
    super.paintComponent(g);
    g.setColor(Color.WHITE);
    g.fillRect(paddle.x, paddle.y, paddle.width, paddle.height);
    g.fillOval(ball.x, ball.y, ball.width, ball.height);
    // g.setColor(Color.RED);
    // g.drawRect(paddle.x, paddle.y, paddle.width, paddle.height);
    // g.drawOval(ball.x, ball.y, ball.width, ball.height);
    g.setFont(new Font("Arial", Font.PLAIN, 20));
    g.drawString(Integer.toString(score), 500, 850);
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 10; j++) {
        if (blocks[i][j].color == 0) {
          g.setColor(Color.BLUE);
        } else if (blocks[i][j].color == 1) {
          g.setColor(Color.GREEN);
        } else if (blocks[i][j].color == 2) {
          g.setColor(Color.MAGENTA);
        } else if (blocks[i][j].color == 3) {
          g.setColor(Color.YELLOW);
        } else if (blocks[i][j].color == 4) {
          g.setColor(Color.ORANGE);
        }
        if (!blocks[i][j].isHide) {
          g.fillRect(
            blocks[i][j].x,
            blocks[i][j].y,
            blocks[i][j].width,
            blocks[i][j].height
          );
        }
      }
    }
    if (ball.combo > 0) {
      g.setFont(new Font("Arial", Font.PLAIN, 24));
      g.drawString(Integer.toString(ball.combo) + "COMBO", 500, 450);
    }
    for (int i = 0; i < ball.life; i++) {
      g.setColor(Color.WHITE);
      g.fillOval(30 + (i * 20 + 20), 850, 10, 10);
    }

    if (!ball.moving) {
      g.setColor(Color.WHITE);
      g.setFont(new Font("Arial", Font.BOLD, 32));
      g.drawString("READY", 250, 400);
      g.setFont(new Font("Arial", Font.BOLD, 18));
      g.drawString("Press Space to Start", 220, 480);
    }
    if (ball.life < 0) {
      g.setColor(Color.RED);
      g.setFont(new Font("Arial", Font.BOLD, 32));
      g.drawString("GAME OVER", 220, 400);
    }
  }

  @Override
  public void run() {
    while (true) {
      try {
        Thread.sleep(20);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      repaint();
      paddleMove();
      ball.move(paddle);
      score += ball.checkCollision(blocks);
      // for (Block itemList[] : blocks) {
      //   for (Block item : itemList) {
      //     if (!item.isHide) {
      //       if (
      //         hitObject(
      //           new Rectangle(ball.x, ball.y, ball.width, ball.height),
      //           new Rectangle(item.x, item.y, item.width, item.height)
      //         )
      //       ) {
      //         item.isHide = true;
      //         // ball.speedX = -ball.speedX;
      //         // ball.speedY = -ball.speedY;

      //         if (
      //           ball.x + ball.width - item.x < 5 ||
      //           ball.x - item.x + item.width < 5
      //         ) {
      //           ball.speedX = -ball.speedX;
      //           System.out.print(
      //             ball.x + ball.width - item.x < 5
      //               ? "좌측면 충돌"
      //               : ball.x - item.x + item.width < 5
      //                 ? "우측면 충돌"
      //                 : "측면충돌"
      //           );
      //           System.out.println(
      //             ball.x + ball.width - item.x < 5
      //               ? ball.x + ball.width - item.x
      //               : ball.x - item.x + item.width < 5
      //                 ? "우측면 충돌"
      //                 : "측면충돌"
      //           );
      //         }
      //         if (
      //           item.y - ball.y + ball.height < 5 ||
      //           item.y + item.height - ball.y < 5
      //         ) {
      //           ball.speedY = -ball.speedY;
      //           System.out.print(
      //             item.y - ball.y + ball.height < 5
      //               ? "하면 충돌"
      //               : item.y + item.height - ball.y < 5
      //                 ? "상면 충돌"
      //                 : "상하면충돌"
      //           );
      //           System.out.println(
      //             item.y - ball.y + ball.height < 5
      //               ? item.y - ball.y + ball.height
      //               : item.y + item.height - ball.y < 5
      //                 ? item.y + item.height - ball.y
      //                 : "상하면충돌"
      //           );
      //         }
      //         break;
      //       }
      //     }
      //   }
      // }
    }
  }

  public void paddleMove() {
    if (paddle.isLeft && paddle.x > 0) {
      paddle.x -= 10;
      if (!ball.moving) {
        ball.x -= 10;
      }
    } else if (paddle.isRight && paddle.x + paddle.width < 618) {
      paddle.x += 10;
      if (!ball.moving) {
        ball.x += 10;
      }
    }
  }

  public void ballMove() {
    ball.x += ball.speedX;
    ball.y += ball.speedY;
  }

  public boolean hitObject(Rectangle ball, Rectangle rect) {
    return ball.intersects(rect);
  }
}

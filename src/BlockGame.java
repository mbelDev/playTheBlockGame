import javax.swing.*;

public class BlockGame extends JFrame {

  public BlockGame() {
    this.setTitle("얼캐노이드");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GamePanel gamePanel = new GamePanel();
    this.setContentPane(gamePanel);

    this.setVisible(true);
    this.pack();
  }

  public static void main(String[] args) {
    new BlockGame();
  }
}

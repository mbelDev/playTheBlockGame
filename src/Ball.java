public class Ball {

  public int x, y;
  public int width, height, speedX, speedY, life;
  public boolean moving = false;
  public int combo = 0;
  public boolean isCombo = false;

  public int respawn = 0;

  public Ball() {
    x = 0;
    y = 0;
    life = 0;
    width = 10;
    height = 10;
    speedX = 0;
    speedY = 0;
  }

  public void shoot() {
    if (!moving) {
      speedY = -8;
      speedX = (int) Math.random() * 20 - 10;
      moving = true;
    }
  }

  public void move(Paddle _paddle) {
    if (moving) {
      x += speedX;
      y += speedY;

      if (x < 0 || x > 608) {
        speedX = -speedX;
        x = x < 0 ? x = 0 : x > 608 ? x = 608 : 608;
      }
      if (y < 0) {
        speedY = -speedY;
      }
      if (y > 900) {
        if (respawn <= 180) {
          respawn++;
        }
        if (respawn > 90) {
          life--;
          if (life >= 0) {
            combo = 0;
            respawn = 0;
            moving = false;
            x = _paddle.x + 35;
            y = _paddle.y - 10;
          }
        }
      }

      if (
        (x + width > _paddle.x) &&
        (x < _paddle.x + _paddle.width) &&
        (y + height > _paddle.y) &&
        (y < _paddle.y + _paddle.height)
      ) {
        //충돌했다
        if (!isCombo) {
          combo = 0;
        } else {
          isCombo = false;
        }
        int checkX = speedX > 0 ? 1 : -1;
        int checkY = speedY > 0 ? 1 : -1;
        while (
          x + checkX > _paddle.x + _paddle.width ||
          x + checkX + width < _paddle.x ||
          y + checkY > _paddle.y + _paddle.height ||
          y + checkY + height < _paddle.y
        ) {
          x += checkX;
          y += checkY;
        }
        speedY = -speedY;
        speedX = (int) -(((_paddle.x + (_paddle.width / 2)) - x) * 10 / 45);
        // System.out.println((_paddle.x + (_paddle.width / 2)) - x);
        // System.out.println(((_paddle.x + (_paddle.width / 2)) - x) * 10 / 45);
        // System.out.println(speedX);
      }
    }
  }

  public int checkCollision(Block _block[][]) {
    int score = 0;
    for (Block itemList[] : _block) {
      for (Block item : itemList) {
        if (
          (x + speedX < item.x + item.width) &&
          (x + speedX + width > item.x) &&
          (y + speedY < item.y + item.height) &&
          (y + speedY + height > item.y) &&
          !item.isHide
          // 진행방향 speed 앞에 블록이 있는가?
        ) {
          int checkX = speedX > 0 ? 1 : -1;
          int checkY = speedY > 0 ? 1 : -1;
          while (
            x + checkX > item.x + item.width ||
            x + checkX + width < item.x ||
            y + checkY > item.y + item.height ||
            y + checkY + height < item.y
            //관통하거나 박히지 않게 착지 시켜줌
          ) {
            x += checkX;
            y += checkY;
          }
          System.out.println(
            (x - item.x) +
            ":" +
            (y - item.y) +
            "(" +
            checkX +
            ":" +
            checkY +
            ")"
          );
          // if (x - item.x + item.width < 5 || item.x - x + width < 5) {
          //   speedX = -speedX;
          //   System.out.print(
          //     x - item.x + item.width < 5
          //       ? "좌측면 히트! "
          //       : item.x - x + width < 5 ? "우측면 히트! " : "측면 히트! "
          //   );
          //   System.out.println(
          //     x - item.x + item.width < 5
          //       ? x - item.x + item.width
          //       : item.x - x + width < 5 ? item.x - x + width : "측면 히트!"
          //   );
          // }

          // if (
          //   (item.y - y + height) * checkY < 5 || item.y + item.height - y < 5
          // ) {
          //   speedY = -speedY;
          //   System.out.print(
          //     (item.y - y + height) * checkY < 5
          //       ? "상면 히트!"
          //       : item.y + item.height - y < 5 ? "하면 히트!" : "상하면 히트!"
          //   );
          //   System.out.println(
          //     (item.y - y + height) * checkY < 5
          //       ? (item.y - y + height) * checkY
          //       : item.y + item.height - y < 5
          //         ? item.y + item.height - y
          //         : "상하면 히트!"
          //   );
          // } 아래 코드로 대체됨

          if (checkY < 0) { // 상진행중
            checkY = item.y + item.height - y;
            System.out.print(checkY < 5 ? "하충돌 " : "");
          }
          if (checkY > 0) { // 하진행중
            checkY = y + height - item.y;
            System.out.print(checkY < 5 ? "상충돌 " : "");
          }
          if (checkY < 5) {
            speedY = -speedY;
          }
          if (checkX < 0) { // 좌진행중
            checkX = item.x + item.width - x;
            System.out.print(checkX < 5 ? "우충돌 " : "");
          }
          if (checkX > 0) { // 우진행중
            checkX = x + width - item.x;
            System.out.print(checkX < 5 ? "좌충돌 " : "");
          }
          if (checkX < 5) {
            speedX = -speedX;
          }
          System.out.println("\t" + checkX + "/" + checkY);

          item.isHide = true;
          score += 1000 + (100 * combo * combo);
          combo++;
          isCombo = true;
          break; //동시에 여러개 블록에 판정 일어나지않게 멈춰줌
        }
      }
    }
    return score;
  }
}

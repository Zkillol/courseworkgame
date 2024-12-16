package com.example.game;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HelloController {

    @FXML
    public Pane gamePane;

    private Canvas canvas;
    private GraphicsContext gc;

    private Image backgroundImage;
    private Image playerUpImage;
    private Image playerDownImage;
    private Image playerLeftImage;
    private Image playerRightImage;
    private Image playerWithSwordUpImage;
    private Image playerWithSwordDownImage;
    private Image playerWithSwordLeftImage;
    private Image playerWithSwordRightImage;

    private Image currentPlayerImage;

    private Image chestClosedImage;
    private Image chestOpenImage;
    private Image swordImage;

    private boolean chestOpened = false;
    private boolean swordPicked = false;

    private double playerX = 300;
    private double playerY = 300;
    private double playerWidth = 100;
    private double playerHeight = 100;

    public boolean right = false;
    public boolean left = false;
    public boolean up = false;
    public boolean down = false;

    private double playerSpeed = 5.0;

    private List<Mob> mobs;

    private boolean gameOver = false;
    private boolean victory = false;
    private Button restartButton;

    private final double chestX = 1400;
    private final double chestY = 400;
    private final double swordX = chestX + 20;
    private final double swordY = chestY + 100;

    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (!gameOver) {
                // Обновляем позицию игрока
                movePlayer();

                // Двигаем мобов
                for (Mob mob : mobs) {
                    mob.move(); // Обеспечиваем движение мобов
                }

                // Проверяем столкновения с мобами
                for (Mob mob : mobs) {
                    if (checkCollision(playerX, playerY, playerWidth, playerHeight, mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight())) {
                        if (!swordPicked) {
                            gameOver = true;
                            System.out.println("Вы погибли! Моб вас убил.");
                            showRestartButton();
                            return; // Останавливаем обработку
                        } else {
                            System.out.println("Нажмите ЛКМ, чтобы атаковать моба.");
                        }
                    }
                }

                // Проверяем взаимодействие с сундуком
                if (checkCollision(playerX, playerY, playerWidth, playerHeight, chestX, chestY, 100, 100) && !chestOpened) {
                    System.out.println("Нажмите E, чтобы открыть сундук.");
                }

                // Проверяем победу
                if (mobs.isEmpty()) {
                    victory = true;
                    gameOver = true;
                    System.out.println("Вы победили! Все мобы уничтожены!");
                    showRestartButton();
                }

                // Рисуем сцену
                drawScene();
            }
        }
    };



    @FXML
    public void initialize() {
        canvas = new Canvas(1500, 800);
        gc = canvas.getGraphicsContext2D();
        gamePane.getChildren().add(canvas);

        // Загрузка фонового изображения и изображений персонажа
        backgroundImage = new Image(getClass().getResource("/com/example/game/background.jpg").toString());

        // Обычные изображения персонажа
        playerUpImage = new Image(getClass().getResource("/com/example/game/playerUp.png").toString());
        playerDownImage = new Image(getClass().getResource("/com/example/game/playerDown.png").toString());
        playerLeftImage = new Image(getClass().getResource("/com/example/game/playerLeft.png").toString());
        playerRightImage = new Image(getClass().getResource("/com/example/game/playerRight.png").toString());

        // Изображения персонажа с мечом
        playerWithSwordUpImage = new Image(getClass().getResource("/com/example/game/playerWithSwordUp.png").toString());
        playerWithSwordDownImage = new Image(getClass().getResource("/com/example/game/playerWithSwordDown.png").toString());
        playerWithSwordLeftImage = new Image(getClass().getResource("/com/example/game/playerWithSwordLeft.png").toString());
        playerWithSwordRightImage = new Image(getClass().getResource("/com/example/game/playerWithSwordRight.png").toString());

        // Другие ресурсы
        chestClosedImage = new Image(getClass().getResource("/com/example/game/chestClosed.png").toString());
        chestOpenImage = new Image(getClass().getResource("/com/example/game/chestOpenned.png").toString());
        swordImage = new Image(getClass().getResource("/com/example/game/sword.png").toString());

        currentPlayerImage = playerDownImage;

        initializeMobs();

        restartButton = new Button("Restart");
        restartButton.setLayoutX(700);
        restartButton.setLayoutY(400);
        restartButton.setVisible(false);
        restartButton.setOnAction(event -> restartGame());
        gamePane.getChildren().add(restartButton);

        timer.start();

        gamePane.setOnKeyPressed(this::handleKeyPress);
        gamePane.setOnKeyReleased(this::handleKeyRelease);
        gamePane.setOnMousePressed(this::handleMouseClick);
        gamePane.setFocusTraversable(true);
        gamePane.requestFocus();



        drawScene();
    }

    private void initializeMobs() {
        mobs = new ArrayList<>();
        // Создаем мобов с фиксированными маршрутами
        mobs.add(new Mob(400, 50, 80, 80, 3));
        mobs.add(new Mob(800, 750, 80, 80, -2));
    }

    private void movePlayer() {
        if (up && playerY - playerSpeed >= 0) {
            playerY -= playerSpeed;
            currentPlayerImage = swordPicked ? playerWithSwordUpImage : playerUpImage;
        }
        if (down && playerY + playerHeight + playerSpeed <= canvas.getHeight()) {
            playerY += playerSpeed;
            currentPlayerImage = swordPicked ? playerWithSwordDownImage : playerDownImage;
        }
        if (left && playerX - playerSpeed >= 0) {
            playerX -= playerSpeed;
            currentPlayerImage = swordPicked ? playerWithSwordLeftImage : playerLeftImage;
        }
        if (right && playerX + playerWidth + playerSpeed <= canvas.getWidth()) {
            playerX += playerSpeed;
            currentPlayerImage = swordPicked ? playerWithSwordRightImage : playerRightImage;
        }
    }


    private void handleKeyPress(KeyEvent event) {
        if (!gameOver) {
            switch (event.getCode()) {
                case W -> up = true;
                case A -> left = true;
                case S -> down = true;
                case D -> right = true;
                case E -> {
                    if (checkCollision(playerX, playerY, playerWidth, playerHeight, chestX, chestY, 100, 100) && !chestOpened) {
                        chestOpened = true;
                        System.out.println("Сундук открыт!");
                    }
                    if (checkCollision(playerX, playerY, playerWidth, playerHeight, swordX, swordY, 50, 50) && !swordPicked && chestOpened) {
                        swordPicked = true;
                        playerSpeed = 3.0; // Уменьшаем скорость
                        System.out.println("Меч подобран! Скорость уменьшена.");
                    }
                }
            }
        }
    }

    private void handleKeyRelease(KeyEvent event) {
        if (!gameOver) {
            switch (event.getCode()) {
                case W -> up = false;
                case A -> left = false;
                case S -> down = false;
                case D -> right = false;
            }
        }
    }

    private void handleMouseClick(MouseEvent event) {
        if (!gameOver && swordPicked) {
            Iterator<Mob> mobIterator = mobs.iterator();
            while (mobIterator.hasNext()) {
                Mob mob = mobIterator.next();
                // Проверяем, находится ли моб в зоне поражения (игрок рядом)
                if (checkCollision(playerX, playerY, playerWidth, playerHeight, mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight())) {
                    mobIterator.remove(); // Удаляем моб из списка
                    System.out.println("Моб убит!");
                    break; // Останавливаем проверку после убийства
                }
            }
        }
    }



    private void drawScene() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());

        // Рисуем игрока
        gc.drawImage(currentPlayerImage, playerX, playerY, playerWidth, playerHeight);

        // Рисуем мобов
        for (Mob mob : mobs) {
            gc.drawImage(mob.getImage(), mob.getX(), mob.getY(), mob.getWidth(), mob.getHeight());
        }

        // Рисуем сундук
        gc.drawImage(chestOpened ? chestOpenImage : chestClosedImage, chestX, chestY, 100, 100);

        // Рисуем меч
        if (chestOpened && !swordPicked) {
            gc.drawImage(swordImage, swordX, swordY, 50, 50);
        }
    }

    private void showRestartButton() {
        restartButton.setVisible(true);
    }

    private void restartGame() {
        gameOver = false;
        victory = false;
        playerX = 300;
        playerY = 300;
        playerSpeed = 5.0;
        swordPicked = false;
        chestOpened = false;

        // Сброс движения
        up = false;
        down = false;
        left = false;
        right = false;

        initializeMobs();
        restartButton.setVisible(false);
        timer.start();
        drawScene();
        gamePane.requestFocus();
    }

    private boolean checkCollision(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }

    private static class Mob {
        private double x, y;
        private final double width, height;
        private double speedY;
        private final Image image;

        public Mob(double x, double y, double width, double height, double speedY) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.speedY = speedY;
            this.image = new Image(HelloController.class.getResource("/com/example/game/mob.png").toString());
        }




        public void move() {
            y += speedY;
            if (y <= 0 || y >= 750) {
                speedY = -speedY;
            }
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public Image getImage() {
            return image;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }
    }
}
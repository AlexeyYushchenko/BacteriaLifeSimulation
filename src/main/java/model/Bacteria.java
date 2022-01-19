package model;

import java.util.Objects;

public class Bacteria {
    private static int bacteriaNum = 0;
    private int x;
    private int y;
    private String colony;
    private boolean isAlive;
    private int birthDay;

    public Bacteria(int x, int y, boolean isAlive) {
        this.x = x;
        this.y = y;
        this.colony = " \uD83D\uDE0C ";
        this.isAlive = isAlive;
        this.birthDay = PetriDish.getCurrentDay();
        bacteriaNum++;
    }

    public static int getBacteriaNum() {
        return bacteriaNum;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getColony() {
        return colony;
    }

    public void setColony(String colony) {
        this.colony = colony;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getAge() {
        return PetriDish.getCurrentDay() - this.birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bacteria)) return false;
        Bacteria bacteria = (Bacteria) o;
        return x == bacteria.x &&
                y == bacteria.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Bacteria{" +
                "x=" + x +
                ", y=" + y +
                ", colony=" + colony +
                ", isAlive=" + isAlive +
                ", age=" + birthDay +
                '}';
    }

    public void draw(PetriDish petriDish) {
        petriDish.setCellValue(this.y, this.x, isAlive? this.colony : String.valueOf(" \uD83D\uDC80 ")); //Skull emoji 💀 if cell is not alive.
    }
}

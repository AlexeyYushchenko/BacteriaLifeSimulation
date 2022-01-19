package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PetriDish {
    private static final Random random = new Random();
    private int height;
    private int width;
    private String[][]matrix;
    private int K1; //randomNumberOrigin
    private int K2; //randomNumberBound (exclusive)
    private List<Bacteria> bacteriaList;
    private static int currentDay;
    private int bacteriaDied;
    private boolean isSimulationStopped;
    private int simulationDelayInMillis;
    private int timeoutInDays;
    private int freeSpace;
    private int probabilityToReproduce;
    private int probabilityToDie;

    public PetriDish(int N, int K1, int K2, int simulationDelayInMillis, int timeoutInDays, int probabilityToReproduce, int probabilityToDie) {
        if (N < 1 || N > 12113)
            throw new IllegalArgumentException("Canvas size must be within 1 and 12113");
        this.height = N;
        this.width = N;
        this.matrix = new String[height][width];
        this.K1 = K1;
        this.K2 = K2;
        this.bacteriaList = new ArrayList<>();
        this.currentDay = 1;
        this.bacteriaDied = 0;
        this.simulationDelayInMillis = simulationDelayInMillis;
        this.timeoutInDays = timeoutInDays;
        this.freeSpace = height * width;
        this.probabilityToReproduce = probabilityToReproduce;
        this.probabilityToDie = probabilityToDie;
    }

    private void draw() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                setCellValue(i, j, String.valueOf("\t"));
            }
        }
        for (Bacteria bacteria : bacteriaList){
            bacteria.draw(this);
        }
    }

    public int getTimeoutInDays() {
        return timeoutInDays;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public static int getCurrentDay() {
        return currentDay;
    }

    public void startSimulation() throws InterruptedException {
        do{
            System.out.println("DAY: " + currentDay);
            checkBacteria();
            timeToReproduce();

            if (currentDay <= 7){
                int n = random.nextInt(this.K2 - K1) + K1;
                for (int i = 0; i < n; i++) {
                    int x = random.nextInt(width);
                    int y = random.nextInt(height);
                    Bacteria bacteria = new Bacteria(x, y, true);
                    if (!bacteriaList.contains(bacteria)){
                        this.bacteriaList.add(bacteria);
                    }
                }
            }

            draw();
            printMatrix();
            Thread.sleep(simulationDelayInMillis);
            currentDay++;
            checkConditions();
        }while (!isSimulationStopped);
        printReport();
    }

    /**
     * Age is better defined through static constant day of creation and comparing it with each creature's age;
     * age = current day;
     * current age = current day - age;
     */

    private void checkBacteria() {
        for (Bacteria bacteria : bacteriaList){
            if (bacteria.getAge() > 7){
                if (bacteria.isAlive()){
                    if (Math.random() <= probabilityToDie / 100.0){
                        bacteria.setAlive(false);
                        bacteriaDied++;
                    }
                }
            }
        }
    }

    private void timeToReproduce() {
        List<Bacteria> bacteriaChildren = new ArrayList<>();
        int[] dr = new int[]{-1, 1,-1, 1,-1, 1, 0, 0}; //direction row
        int[] dc = new int[]{-1, 1, 1,-1, 0, 0, 1,-1}; //direction row

        for (Bacteria bacteria : bacteriaList){

            if (bacteria.getAge() > 14) continue; //только те, что младше.

            int row = bacteria.getX();
            int col = bacteria.getY();

            for (int i = 0; i < 8; i++) {
                int rr = row + dr[i];
                int cc = col + dc[i];

                if (rr < 0 || rr >= width) continue;
                if (cc < 0 || cc >= height) continue;

                Bacteria child = new Bacteria(rr, cc, true);
                if (!bacteriaList.contains(child) && !bacteriaChildren.contains(child) && Math.random() <= probabilityToReproduce / 100.0){
                      bacteriaChildren.add(child);
                }
            }
        }
        bacteriaList.addAll(bacteriaChildren);
    }

    private void printMatrix() {
        System.out.println(" * * * ");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }

    private void checkConditions() {
        if (currentDay > timeoutInDays){
            System.out.println("TIMEOUT");
            isSimulationStopped = true;
        }
        freeSpace = matrix.length * matrix[0].length - bacteriaList.size();
        if (freeSpace <= 0){
            System.out.println("No more free space");
            isSimulationStopped = true;
        }
    }

    private void printReport() {
        System.out.println();
        System.out.println("SIMULATION REPORT:");
        System.out.printf("Days: %s%n", currentDay);
        System.out.printf("Bacteria died: %s%n", bacteriaList.stream().filter(x -> !x.isAlive()).count());
    }

    public void setCellValue(int y, int x, String c) {
        this.matrix[y][x] = c;
    }
}

// симуляция заканчивается на 6 дне. Возможная причина - не установлен контроль создания и декременции свободных клеток.












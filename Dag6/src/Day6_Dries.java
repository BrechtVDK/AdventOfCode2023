import java.io.File;
import java.util.Scanner;

public class Day6_Dries {

    private static int[] timeInputs = new int[4];
    private static int[] distanceInputs = new int[4];

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            String timeString = scanner.nextLine();
            String distanceString = scanner.nextLine();

            String[] times = timeString.split("\\s+");
            timeInputs[0] = Integer.parseInt(times[1]);
            timeInputs[1] = Integer.parseInt(times[2]);
            timeInputs[2] = Integer.parseInt(times[3]);
            timeInputs[3] = Integer.parseInt(times[4]);

            String[] distances = distanceString.split("\\s+");
            distanceInputs[0] = Integer.parseInt(distances[1]);
            distanceInputs[1] = Integer.parseInt(distances[2]);
            distanceInputs[2] = Integer.parseInt(distances[3]);
            distanceInputs[3] = Integer.parseInt(distances[4]);
        } catch (Exception e) {
            // TODO: handle exception
        }

        int result = 1;

        for (int i = 0; i < timeInputs.length; i++) {
            int possibilities = calculateNumberOfPossibilitiesWithBetterTime(i);
            System.out.println(possibilities);
            result *= possibilities;
        }

        System.out.println(result);
    }

    public static int calculateNumberOfPossibilitiesWithBetterTime(int index) {
        int time = timeInputs[index];
        int distance = distanceInputs[index];
        int numberOfPossibilities = 0;

        for(int i = 1; i < time; i++) {
            int timeLeftToRace = time - i;
            int speed = i;

            if(speed * timeLeftToRace > distance) {
                numberOfPossibilities++;
            }
        }

        return numberOfPossibilities;
    }

}

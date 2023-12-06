import java.io.File;
import java.util.Scanner;

public class Day6_2_Dries {

    public static void main(String[] args) {
        int time = 0;
        long distance = 0;

        try (Scanner scanner = new Scanner(new File("input.txt"))) {
            String timeString = scanner.nextLine();
            String distanceString = scanner.nextLine();

            String[] times = timeString.split("\\s+");
            time = Integer.parseInt(times[1] + times[2] + times[3] + times[4]);

            String[] distances = distanceString.split("\\s+");
            distance = Long.parseLong(distances[1] + distances[2] + distances[3] + distances[4]);

        } catch (Exception e) {
            // TODO: handle exception
        }

        System.out.println(time);
        System.out.println(distance);

        int result = calculateNumberOfPossibilitiesWithBetterTime(time, distance);

        System.out.println(result);
    }

    public static int calculateNumberOfPossibilitiesWithBetterTime(int time, long distance) {
        int numberOfPossibilities = 0;

        for(int i = 1; i < time; i++) {
            long timeLeftToRace = time - i;
            long speed = i;

            if(i < 10) {
                System.out.println("Speed*timeleft: " + speed*timeLeftToRace);
            }

            long x = speed * timeLeftToRace;

            if(x > distance) {
                numberOfPossibilities++;
            }
        }

        return numberOfPossibilities;
    }

}

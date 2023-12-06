package StartUp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StartUp2 {
    public static void main(String[] args) {
        try {
            // Replace "path/to/your/engine/schematic.txt" with the actual file path
            String filePath = "input.txt";
            int sum = calculatePartNumberSum(filePath);
            System.out.println("The sum of all part numbers is: " + sum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int calculatePartNumberSum(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int sum = 0;

        while ((line = reader.readLine()) != null) {
            for (int i = 0; i < line.length(); i++) {
                char currentChar = line.charAt(i);

                if (Character.isDigit(currentChar)) {
                    // Check adjacent symbols (including diagonals)
                    if (isAdjacentSymbol(line, i) || isAdjacentSymbolAboveBelow(reader, i)) {
                        sum += Character.getNumericValue(currentChar);
                    }
                }
            }
        }

        reader.close();
        return sum;
    }

    private static boolean isAdjacentSymbol(String line, int index) {
        return index > 0 && line.charAt(index - 1) != '.' || (index < line.length() - 1 && line.charAt(index + 1) != '.');
    }

    private static boolean isAdjacentSymbolAboveBelow(BufferedReader reader, int index) throws IOException {
        String aboveLine = reader.readLine();
        String belowLine = reader.readLine();

        if (aboveLine != null && belowLine != null) {
            return index > 0 && aboveLine.charAt(index - 1) != '.' || (index < belowLine.length() - 1 && belowLine.charAt(index + 1) != '.');
        }

        return false;
    }
}

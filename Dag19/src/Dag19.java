import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dag19 {

	public static void main(String[] args) {
		Dag19 d = new Dag19();

	}

	private String fileName = "input.txt";
	//private String fileName = "test.txt";
	List<Workflow> workflowList = new ArrayList<>();
	List<Rating> ratingList = new ArrayList<>();
	List<Rating> aList = new ArrayList<>();

	public Dag19() {
		readFile();
		run();
		System.out.printf("Sum part 1: %d%n", calculateSumAList());
	}

	private long calculateSumAList() {
		return aList.stream().mapToLong(r -> r.getA()+r.getM()+r.getS()+r.getX()).sum();
		
	}

	private void run() {
		for (Rating rating : ratingList) {
			String res = "in";
			while (!res.equals("A") && !res.equals("R")) {
				Workflow w = getWorkflow(res);
				res = w.runWorkflow(rating);
			}
			if (res.equals("A")) {
				aList.add(rating);
			}
		}
	}

	private Workflow getWorkflow(String label) {
		return workflowList.stream().filter(workflow -> workflow.getLabel().equals(label)).findFirst().orElse(null);
	}

	private void readFile() {
		try (Scanner scanner = new Scanner(new File(fileName))) {
			int rating = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.isBlank()) {
					rating = 1;
					continue;
				}
				switch (rating) {
				case 0 -> workflowList.add(new Workflow(line));
				case 1 -> ratingList.add(new Rating(line));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class Workflow {
		private String label;
		private String endLabel;
		private List<Instruction> listInstructions = new ArrayList<>();

		public Workflow(String line) {
			int indexAccolade = line.indexOf("{");
			setLabel(line.substring(0, indexAccolade));
			int lastIndexOfComma = line.lastIndexOf(",");
			setEndLabel(line.substring(lastIndexOfComma + 1, line.length() - 1));
			setListInstructions(line.substring(indexAccolade + 1, lastIndexOfComma));

		}

		private void setListInstructions(String substring) {
			for (String instruction : substring.split(",")) {
				listInstructions.add(new Instruction(instruction));
			}
		}

		private void setEndLabel(String endLabel) {
			this.endLabel = endLabel;
		}

		public String getLabel() {
			return label;
		}

		public String getEndLabel() {
			return endLabel;
		}

		public List<Instruction> getListInstructions() {
			return listInstructions;
		}

		private void setLabel(String label) {
			this.label = label;
		}

		public String runWorkflow(Rating rating) {
			for (Instruction instruction : listInstructions) {
				char letter = instruction.getLetter();

				boolean trueOrFalse = switch (letter) {
				case 'x' -> instruction.isTrueOrFalse(rating.getX());
				case 'm' -> instruction.isTrueOrFalse(rating.getM());
				case 'a' -> instruction.isTrueOrFalse(rating.getA());
				case 's' -> instruction.isTrueOrFalse(rating.getS());
				default -> throw new IllegalArgumentException("Unexpected value: " + letter);
				};

				if (trueOrFalse) {
					return instruction.getNextLabel();
				}
			}
			// alles false;
			return endLabel;
		}

		@Override
		public String toString() {
			return "Workflow [label=" + label + ", endLabel=" + endLabel + ", listInstructions=" + listInstructions
					+ "]";
		}

	}

	public class Instruction {
		private char letter;
		private int number;
		private char operator;
		private String nextLabel;

		public Instruction(String instruction) {
			setLetter(instruction);
			setNumber(instruction);
			setOperator(instruction);
			setNextLabel(instruction);
		}

		private void setNextLabel(String instruction) {
			this.nextLabel = instruction.split("[>||<]")[1].split(":")[1];

		}

		public String getNextLabel() {
			return this.nextLabel;
		}

		private void setOperator(String instruction) {
			this.operator = instruction.charAt(1);

		}

		private void setNumber(String instruction) {
			this.number = Integer.parseInt(instruction.split("[>||<]")[1].split(":")[0]);

		}

		private void setLetter(String instruction) {
			this.letter = instruction.charAt(0);
		}

		public char getLetter() {
			return letter;
		}

		public boolean isTrueOrFalse(int x) {
			return switch (operator) {
			case '<' -> x < number;
			case '>' -> x > number;
			default -> throw new IllegalArgumentException("Unexpected value: " + operator);
			};
		}

		@Override
		public String toString() {
			return "Instruction [letter=" + letter + ", number=" + number + ", operator=" + operator + ", nextLabel="
					+ nextLabel + "]";
		}

	}

	public class Rating {
		private int x, m, a, s;

		public Rating(String line) {
			setNumbers(line);
		}

		private void setNumbers(String line) {
			int i = 0;
			for (String token : line.split("=")) {
				switch (i++) {
				case 1 -> this.x = Integer.parseInt(token.split(",")[0]);
				case 2 -> this.m = Integer.parseInt(token.split(",")[0]);
				case 3 -> this.a = Integer.parseInt(token.split(",")[0]);
				case 4 -> this.s = Integer.parseInt(token.split("}")[0]);
				}
			}
		}

		public int getX() {
			return this.x;
		}

		public int getM() {
			return this.m;
		}

		public int getA() {
			return this.a;
		}

		public int getS() {
			return this.s;
		}

		@Override
		public String toString() {
			return "Rating [x=" + x + ", m=" + m + ", a=" + a + ", s=" + s + "]";
		}

	}
}

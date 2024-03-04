import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Give expression: ");
        String expressionStr = sc.nextLine();

        try {
            ComplexNumber result = evaluateExpression(expressionStr);
            System.out.println("The expression: " + expressionStr);
            System.out.println("The result: " + result);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static ComplexNumber evaluateExpression(String expressionStr) {
        // White spaces
        expressionStr = expressionStr.replaceAll("\\s+", "");

        // Check for empty expression
        if (expressionStr.isEmpty()) {
            throw new IllegalArgumentException("Empty expression.");
        }

        // Split the expression (example, "1+2i*3-4i" becomes ["1", "+", "2i", "*", "3", "-", "4i"])
        String[] elements = expressionStr.split("(?=[+\\-*/])|(?<=[+\\-*/])");

        // Check if we have enough elements to create a valid expression
        if (elements.length < 3 || elements.length % 2 == 0) {
            throw new IllegalArgumentException("Invalid expression.");
        }

        int currentIndex = 0;
        int[] realPartComplexNumber = new int[(elements.length + 1) / 4 + 1];
        int[] imaginaryPartComplexNumber = new int[(elements.length + 1) / 4 + 1];
        String operatorBetweenComplexNumbers;
        String[] operatorsBetweenRealAndImaginaryPartOfAComplexNumber = new String[(elements.length + 1) / 4 + 1];


        for (int i = 0; i < elements.length - 2; i += 4) {
            realPartComplexNumber[currentIndex] = Integer.parseInt(elements[i]);
            currentIndex++;
        }

        currentIndex = 0;
        for (int i = 2; i < elements.length; i += 4) {
            imaginaryPartComplexNumber[currentIndex] = Integer.parseInt(elements[i].replace("i", ""));
            currentIndex++;
        }

        operatorBetweenComplexNumbers = elements[3];


        currentIndex = 0;
        for (int i = 1; i < elements.length - 1; i += 4) {
            operatorsBetweenRealAndImaginaryPartOfAComplexNumber[currentIndex] = elements[i];
            currentIndex++;
        }

        for (int i = 0; i < operatorsBetweenRealAndImaginaryPartOfAComplexNumber.length; i++) {
            if (Objects.equals(operatorsBetweenRealAndImaginaryPartOfAComplexNumber[i], "-")) {
                imaginaryPartComplexNumber[i] *= -1;
            }
        }
        
        ComplexNumber[] numbers = new ComplexNumber[(elements.length + 1) / 4 + 1];
        for (int i = 0; i < realPartComplexNumber.length - 1; i++) {
            numbers[i] = new ComplexNumber(realPartComplexNumber[i], imaginaryPartComplexNumber[i]);
        }

        return getComplexNumberResult(operatorBetweenComplexNumbers, numbers);
    }

    private static ComplexNumber getComplexNumberResult(String operatorBetweenComplexNumbers, ComplexNumber[] numbers) {
        ComplexNumber result;
        switch (operatorBetweenComplexNumbers) {
            case "+" -> {
                ComplexExpression addExpr = new AdditionExpression(numbers);
                result = addExpr.execute();
            }
            case "-" -> {
                ComplexExpression decExpr = new SubtractionExpression(numbers);
                result = decExpr.execute();
            }
            case "*" -> {
                ComplexExpression mulExpr = new MultiplicationExpression(numbers);
                result = mulExpr.execute();
            }
            case "/" -> {
                    ComplexExpression divExpr = new DivisionExpression(numbers);
                result = divExpr.execute();
            }
            default -> throw new IllegalArgumentException("Unknown operator: " + operatorBetweenComplexNumbers);
        }

        return result;
    }
}
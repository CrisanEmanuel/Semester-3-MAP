public class AdditionExpression extends ComplexExpression {
    public AdditionExpression(ComplexNumber[] args) {
        super(Operation.ADDITION, args);
    }

    @Override
    public ComplexNumber execute() {
        ComplexNumber result = arguments[0];
        for (int i = 1; i < arguments.length -1; i++) {
            result = executeOneOperation(result, arguments[i]);
        }
        return result;
    }

    @Override
    protected ComplexNumber executeOneOperation(ComplexNumber number1, ComplexNumber number2) {
        return number1.addition(number2);
    }
}
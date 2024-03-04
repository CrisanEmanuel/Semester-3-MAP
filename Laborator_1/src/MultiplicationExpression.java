public class MultiplicationExpression extends ComplexExpression {
    public MultiplicationExpression(ComplexNumber[] args) {
        super(Operation.MULTIPLICATION, args);
    }
    @Override
    public ComplexNumber execute() {
        ComplexNumber result = arguments[0];
        for (int i = 1; i < arguments.length - 1; i++) {
            result = executeOneOperation(result, arguments[i]);
        }
        return result;
    }
    @Override
    protected ComplexNumber executeOneOperation(ComplexNumber number1, ComplexNumber number2) {
        return number1.multiplication(number2);
    }
}
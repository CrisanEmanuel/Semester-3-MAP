public abstract class ComplexExpression {

    Operation operation;
    ComplexNumber[] arguments; // array of complex numbers

    public ComplexExpression(Operation operation, ComplexNumber[] arguments) {
        this.operation = operation;
        this.arguments = arguments;
    }

    protected abstract ComplexNumber executeOneOperation(ComplexNumber number1, ComplexNumber number2);

    public abstract ComplexNumber execute();
}
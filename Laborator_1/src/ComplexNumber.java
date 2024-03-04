public class ComplexNumber {

    int real, imaginary;

    public ComplexNumber(int real, int imaginary){
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber addition(ComplexNumber other) {
       return new ComplexNumber(this.real + other.real, this.imaginary + other.imaginary);
    }

    public ComplexNumber subtraction(ComplexNumber other) {
        return new ComplexNumber(this.real - other.real, this.imaginary - other.imaginary);
    }

    public ComplexNumber multiplication(ComplexNumber other) {
        int newReal = this.real * other.real - this.imaginary * other.imaginary;
        int newImaginary = this.real * other.imaginary + this.imaginary * other.real;
        return new ComplexNumber(newReal, newImaginary);
    }

    public ComplexNumber division(ComplexNumber other) {
        int denominator = other.real * other.real + other.imaginary * other.imaginary;
        int newReal = (this.real * other.real + this.imaginary * other.imaginary) / denominator;
        int newImaginary = (other.real * this.imaginary - this.real * other.imaginary) / denominator;
        return new ComplexNumber(newReal, newImaginary);
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(this.real, -1 * this.imaginary);
    }

    @Override
    public String toString() {
        if (this.imaginary >= 0) {
            return this.real + "+" + this.imaginary + "i";
        } else {
            return this.real + "-" + (-1 * this.imaginary) + "i";
        }
    }
}
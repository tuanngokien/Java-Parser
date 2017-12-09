/*
    - There are 4 is-a relationships
    + Square is a Rectangle
    + Square is a Shape
    + Rectangle is a Shape
    + Circle is a Shape
    
    - Class Circle inherit attributes from Shape ( Cirlce is a Shape), and Circle has different attributes with Rectangle ( Radius and Width - Length)   
    - Variable PI should be final because it's not changed through the program executing
    NKT
*/


public class Bt02{
    public static void main(String[] args){
        Shape shape1 = new Shape("Green",true);
        Circle circle1 = new Circle(9.8,"Blue",false);
        Rectangle rectangle1 = new Rectangle(1.0, 9.8, "Pink",true);
        Square square1 = new Square(2.0);
        
        System.out.print(shape1);
        System.out.print(circle1);
        System.out.print(rectangle1);
        System.out.print(square1);
    }
}

class Shape{
    private String color;
    private boolean filled;
    
    public Shape(){
        color = "Red";
        filled = true;
    }
    
    public Shape(String color, boolean filled){
        this.color = color;
        this.filled = filled;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }
    
    @Override
    public String toString(){
        return "Shape: \n\tColor: " + color +"\n\tFilled: " + filled + "\n";
    }
    
}

class Circle extends Shape implements Comparable<Circle>, Cloneable{
    private final double PI = Math.PI;
    private double radius ;
    
    public Circle(){
        super();
        this.radius = 1.0;
    }
    
    public Circle(double radius){
        super();
        this.radius = radius;
    }
    
    public Circle(double radius, String color, boolean filled){
        super(color, filled);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
    
    public double getArea(){
        return PI*radius*radius;
    }
    
    public double getPerimeter(){
        return 2*PI*radius;
    }
    
    @Override
    public String toString(){
        return "Circle: \n\tColor: " + super.getColor() +"\n\tFilled: " + super.isFilled() + "\n\tRadius: " + radius  + "\n";
    }
    @Override
    public int compareTo(Circle b){
        if(radius > b.radius){
            return 1;
        }else{
            return 0;
        }
    }
    
//    @Override
//    public Shape clone() throws CloneNotSupportedException{
//        Object clone = (Object)super.clone();
//        return (Shape) clone;
//    }                   

}

class Rectangle extends Shape{
    private double width;
    private double length;
    
    public Rectangle(){
        super();
        width = 1.0;
        length = 1.0;
    }
    
    public Rectangle(double width, double length){
        super();
        this.width = width;
        this.length = length;
    }
    
    public Rectangle(double width, double length, String color, boolean filled){
        super(color, filled);
        this.width = width;
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
    
    public double getArea(){
        return width * length;
    }
    
    public double getPerimeter(){
        return 2 * (width + length);
    }
    
    @Override
    public String toString(){
        return "Rectangle: \n\tColor: " + super.getColor() +"\n\tFilled: " + super.isFilled() + "\n\tWidth: " + width + " - Length: " + length  + "\n";
    }
}
    

class Square extends Rectangle{
    public Square(){
        super();
    }
    
    public Square(double side){
        super(side, side);
    }
    
    public Square(double side, String color, boolean filled){
        super(side, side, color, filled);
    }
    
    @Override
    public void setWidth(double side){
        super.setWidth(side);
        super.setLength(side);
    }
    
    @Override
    public void setLength(double side){
        super.setWidth(side);
        super.setLength(side);
    }
    
    @Override
    public String toString(){
        return "Square: \n\tColor: " + super.getColor() +"\n\tFilled: " + super.isFilled() + "\n\tSide: " + super.getLength()  + "\n";
    }
    
}
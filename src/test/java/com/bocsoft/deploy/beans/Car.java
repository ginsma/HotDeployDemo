package test.java.com.bocsoft.deploy.beans;

/**
 * Created by Jean on 16/5/10.
 */

public class Car {
    private int maxSpeed;
    public  String brand;
    private double price;
    public String corp;

    public Car() {

    }

    public Car(String brand, String corp, double price) {
        this.brand = brand;
        this.price = price;
        this.corp = corp;
    }

    public Car(String brand, String corp, int maxSpeed) {
        this.brand = brand;
        this.price = price;
        this.maxSpeed = maxSpeed;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }



}

package model;

/**
 * Created by nitinpoddar on 8/20/15.
 */
public class Temperature {

    private float minTemp;
    private float maxTemp;
    private double temp;

    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public float getMinTemp() {

        return minTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }
}

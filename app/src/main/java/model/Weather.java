package model;

/**
 * Created by nitinpoddar on 8/20/15.
 */
public class Weather {

    public Place place;
    public String iconData;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Snow snow = new Snow();
    public Clouds clouds = new Clouds();
    public Wind wind = new Wind();
}

package com.harman.autowaterproject;

/**
 * Created by Hryasch on 07.01.2017.
 */

public class Flower
{
    private int    id;                      // номер
    private int    wetness;                 // текущая влажность
    private String name;                    // название
    private int    valve_pin;               // пин клапана
    private int    hygrometer_pin;          // пин гигрометра
    private int    critical_wetness;        // критическое значение влаги
    private int    critical_temperature;    // критическая температура
    private int    critical_luminosity;     // критическое освещение

    public Flower()
    {
        id = -1;
        wetness = -1;
        name = "EmptyFlower";
        valve_pin = -1;
        hygrometer_pin = -1;
        critical_wetness = -1;
        critical_temperature = -1;
        critical_luminosity = -1;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Flower(String name, int valve_pin, int hygrometer_pin, int critical_wetness, int critical_temperature, int critical_luminosity)
    {
        this.id = -1;
        this.name = name;
        this.valve_pin = valve_pin;
        this.hygrometer_pin = hygrometer_pin;
        this.critical_wetness = critical_wetness;
        this.critical_temperature = critical_temperature;
        this.critical_luminosity = critical_luminosity;
        this.wetness = -1;
    }

    public Flower(int _id, String name, int valve_pin, int hygrometer_pin, int critical_wetness, int critical_temperature, int critical_luminosity)
    {
        this.id = _id;
        this.name = name;
        this.valve_pin = valve_pin;
        this.hygrometer_pin = hygrometer_pin;
        this.critical_wetness = critical_wetness;
        this.critical_temperature = critical_temperature;
        this.critical_luminosity = critical_luminosity;
        this.wetness = -1;
    }

    public String getName()
    {
        return name;
    }

    public int getValve_pin()
    {
        return valve_pin;
    }

    public int getHygrometer_pin()
    {
        return hygrometer_pin;
    }

    public int getCritical_wetness()
    {
        return critical_wetness;
    }

    public int getCritical_temperature()
    {
        return critical_temperature;
    }

    public int getWetness() {
        return wetness;
    }

    public void setWetness(int wetness) {
        this.wetness = wetness;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setValve_pin(int valve_pin)
    {
        this.valve_pin = valve_pin;
    }

    public void setHygrometer_pin(int hygrometer_pin)
    {
        this.hygrometer_pin = hygrometer_pin;
    }

    public void setCritical_wetness(int critical_wetness)
    {
        this.critical_wetness = critical_wetness;
    }

    public void setCritical_temperature(int critical_temperature)
    {
        this.critical_temperature = critical_temperature;
    }

    public void setCritical_luminosity(int critical_luminosity)
    {
        this.critical_luminosity = critical_luminosity;
    }

    public int getCritical_luminosity()
    {
        return critical_luminosity;
    }
}

package info.bhl.parcodom;


public class Parkingmeters_prices {
    private String parkeerzone;
    private String rayon;
    private int parkeertarief;
    private int dagkaart;
    public Parkingmeters_prices(){}

    public Parkingmeters_prices(String parkeerzone, String rayon, int parkeertarief, int dagkaart, int avondkaart){
        this.parkeerzone = parkeerzone;
        this.rayon = rayon;
        this.parkeertarief = parkeertarief;
        this.dagkaart = dagkaart;

    }

    //-------Setters-------//

    public void setParkeerzone(String parkeerzone) {this.parkeerzone = parkeerzone;}
    public void setRayon(String rayon) {this.rayon = rayon;}
    public void setParkeertarief(int parkeertarief) {this.parkeertarief = parkeertarief;}
    public void setDagkaart (int dagkaart) {this.dagkaart = dagkaart;}

    //-------GETTERS-------//

    public String getParkeerzone() {return parkeerzone;}
    public  String getRayon() {return rayon;}
    public int getParkeertarief() {return parkeertarief;}
    public int getDagkaart() {return dagkaart;}

}



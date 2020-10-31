package base.service.dto;

public class RaspunsAplicatie {

    private Integer aplicareID;
    private Boolean raspuns;

    public RaspunsAplicatie(){}
    
    public RaspunsAplicatie(Integer aplicareID, Boolean raspuns) {
        this.aplicareID = aplicareID;
        this.raspuns = raspuns;
    }
    public Integer getAplicareID() {
        return aplicareID;
    }
    public void setAplicareID(Integer aplicareID) {
        this.aplicareID = aplicareID;
    }
    public Boolean getRaspuns() {
        return raspuns;
    }
    public void setRaspuns(Boolean raspuns) {
        this.raspuns = raspuns;
    }
}

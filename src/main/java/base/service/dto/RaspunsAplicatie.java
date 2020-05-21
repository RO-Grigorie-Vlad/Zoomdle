package base.service.dto;

public class RaspunsAplicatie {

    private Integer aplicareLicentaID;
    private Boolean raspuns;

    public RaspunsAplicatie(){}
    
    public RaspunsAplicatie(Integer aplicareLicentaID, Boolean raspuns) {
        this.aplicareLicentaID = aplicareLicentaID;
        this.raspuns = raspuns;
    }
    public Integer getAplicareLicentaID() {
        return aplicareLicentaID;
    }
    public void setAplicareLicentaID(Integer aplicareLicentaID) {
        this.aplicareLicentaID = aplicareLicentaID;
    }
    public Boolean getRaspuns() {
        return raspuns;
    }
    public void setRaspuns(Boolean raspuns) {
        this.raspuns = raspuns;
    }
}

package base.service.dto;

public class AplicareDTO {

    public AplicareDTO(){}
    
    private String currentUserLogin;
    private Integer idLicentaOrConsulatie;

    public AplicareDTO(String currentUserLogin, Integer idLicentaOrConsulatie) {
        this.currentUserLogin = currentUserLogin;
        this.idLicentaOrConsulatie = idLicentaOrConsulatie;
    }

    public String getCurrentUserLogin() {
        return currentUserLogin;
    }

    public void setCurrentUserLogin(String currentUserLogin) {
        this.currentUserLogin = currentUserLogin;
    }

    public Integer getIdLicentaOrConsulatie() {
        return idLicentaOrConsulatie;
    }

    public void setIdLicentaOrConsulatie(Integer idLicentaOrConsulatie) {
        this.idLicentaOrConsulatie = idLicentaOrConsulatie;
    }

    @Override
    public String toString() {
        return "AplicareDTO [currentUserLogin=" + currentUserLogin + ", idLicentaOrConsulatie=" + idLicentaOrConsulatie
                + "]";
    }

    

    
    

}
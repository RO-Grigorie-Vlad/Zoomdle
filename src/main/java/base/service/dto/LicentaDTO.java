package base.service.dto;

public class LicentaDTO {

    public LicentaDTO(){}
    
    private String currentUserLogin;
    private Integer licentaID;

    public LicentaDTO(String currentUserLogin, Integer licentaID) {
        this.currentUserLogin = currentUserLogin;
        this.licentaID = licentaID;
    }

    public String getCurrentUserLogin() {
        return currentUserLogin;
    }

    public void setCurrentUserLogin(String currentUserLogin) {
        this.currentUserLogin = currentUserLogin;
    }

    public Integer getLicentaID() {
        return licentaID;
    }

    public void setLicentaID(Integer licentaID) {
        this.licentaID = licentaID;
    }

    
    

}
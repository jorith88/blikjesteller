package nl.jorith.blikje.websocket;

public class SocketMessage {
    private String blikje;
    private Boolean add;
    private Boolean substract;

    public String getBlikje() {
        return blikje;
    }

    public void setBlikje(String blikje) {
        this.blikje = blikje;
    }

    public Boolean getAdd() {
        return add;
    }

    public void setAdd(Boolean add) {
        this.add = add;
    }

    public Boolean getSubstract() {
        return substract;
    }

    public void setSubstract(Boolean substract) {
        this.substract = substract;
    }
}

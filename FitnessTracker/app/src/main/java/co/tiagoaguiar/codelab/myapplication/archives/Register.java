package co.tiagoaguiar.codelab.myapplication.archives;

public class Register {
    private int id;
    private String type;
    private double response;
    private String createdDate;

    public void setId(int id) {
        this.id = id;
    }

    public int getId (){
        return id;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setResponse(double response) {
        this.response = response;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getResponse() {
        return response;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getType() {
        return type;
    }
}

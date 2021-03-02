package br.com.italoschramm.bibilinguo.util;

public class Erro {

    private String message = "";
    private int idErro = 0;
    private boolean hasErro = false;

    public void initialize(){
        message = "";
        idErro = 0;
        hasErro = false;
    }

    public void setErro(String msg, int id){
        message = msg;
        idErro = id;
        hasErro = true;
    }

    public String getMessage() {
        return message;
    }

    public int getIdErro() {
        return idErro;
    }

    public boolean isHasErro() {
        return hasErro;
    }
}

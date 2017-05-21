
package ias;


public class Memoria {
    private int dado;
    private String esq, dir;

    public Memoria() {
        this.esq = "";
        this.dir = "";
    }
    
    

    public int getDado() {
        return dado;
    }

    public void setDado(int dado) {
        this.dado = dado;
    }

    public String getEsq() {
        return esq;
    }

    public void setEsq(String esq) {
        this.esq = esq;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
    
    
}

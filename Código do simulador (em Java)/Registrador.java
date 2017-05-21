
package ias;


public class Registrador {

    private float valor;
    private Instrucao instrEsq, instrDir;

    public Registrador(Instrucao instrEsq, Instrucao instrDir) {
        this.instrEsq = instrEsq;
        this.instrDir = instrDir;
    }
    
    

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Instrucao getInstrEsq() {
        return instrEsq;
    }

    public void setInstrEsq(Instrucao instrEsq) {
        this.instrEsq = instrEsq;
    }

    public Instrucao getInstrDir() {
        return instrDir;
    }

    public void setInstrDir(Instrucao instrDir) {
        this.instrDir = instrDir;
    }
    
    

}

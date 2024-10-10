package componentes;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Mateo
 */
public abstract class  Componente {
    private int x;
    private int y;
    private int valor;
    enum tipoComponente {
        LED,
        CABLES,
        COMPUERTA,
        PIN,
        SWITCH,
    }
    public tipoComponente tipoComp;
    
    public  void simular(int valor){}
    public  void simular(){}
//
    public Componente(int x, int y) {
        this.x = x;
        this.y = y;
        this.valor=-1;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the valor
     */
    public int getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(int valor) {
        this.valor = valor;
    }
    
}

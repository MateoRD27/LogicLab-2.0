package componentes;

import compuertas.Compuertas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/*
Integrantes de grupo: 
Torres Kevin
Ramos Mateo 
Gonzales Lauren 
 */
public class Pines extends Componente {

    private String tipoPin;
    private Cables cableEntradaSalida;
    private Leds ledEntrada;
   // private Switches switcheSalida;
    private Compuertas compuertaPadre;

    public Pines(int x, int y, String tipoPin) {
        super(x, y);
        this.compuertaPadre = null;
        this.cableEntradaSalida = null;
        this.ledEntrada = null;
       // this.switcheSalida=null;
        this.tipoPin = tipoPin;
        tipoComp = tipoComponente.PIN;
    }

    @Override
    public void simular(int valor) {
        setValor(valor);
        // caso en que sea un pin de salida
        if (getTipoPin().equals("SALIDA")) {
            if (getCableEntradaSalida() != null) {
                getCableEntradaSalida().simular(valor);
            }
             
        } else {
            // caso en que sea un pin de entrada
            //caso en que sea un pin de compuerta
            if (getCompuertaPadre() != null) {
                getCompuertaPadre().simular();
            }

            // cas en que sea un pin de led
            if (getLedEntrada() != null) {
                getLedEntrada().simular(valor);
            }

        }

    }

    public void setXY(int x, int y) {
        super.setX(x);
        super.setY(y);

        if (cableEntradaSalida != null) {
            if (getTipoPin().equals("ENTRADA")) {
                cableEntradaSalida.setX2Y2(getX(), getY());
            } else {
                cableEntradaSalida.setXY(x, y);
            }
            // Debug
        }

    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(5));
        g.setColor(Color.BLACK);
        g.fillOval(getX() - 4, getY() - 4, 8, 8); // Dibuja un círculo pequeño (8x8)
    }

    public boolean estaEnLaLinea(int posicionX, int posicionY) {
        // Definición de los radios para un área de 12x12
        int radioX = 6;
        int radioY = 6;

        // Verificar si el punto está dentro del rectángulo delimitador del óvalo
        if (posicionX >= getX() - radioX && posicionX <= getX() + radioX
                && posicionY >= getY() - radioY && posicionY <= getY() + radioY) {

            // Cálculo de la distancia desde el centro del óvalo
            double dx = (posicionX - getX()) / (double) radioX;
            double dy = (posicionY - getY()) / (double) radioY;

            // Verificar si está dentro del óvalo
            return (dx * dx + dy * dy) <= 1; // Verifica la ecuación del elipse
        }

        return false;
    }

    /**
     * @param x the x to set
     */
    @Override
    public void setX(int x) {
        super.setX(x);
    }

    /**
     * @return the y
     */
    @Override
    public int getY() {
        return super.getY();
    }

    /**
     * @param y the y to set
     */
    @Override
    public void setY(int y) {
        super.setY(y);
    }

    /**
     * @return the tipoPin
     */
    public String getTipoPin() {
        return tipoPin;
    }

    /**
     * @param tipoPin the tipoPin to set
     */
    public void setTipoPin(String tipoPin) {
        this.tipoPin = tipoPin;
    }

    /**
     * @return the cableEntradaSalida
     */
    public Cables getCableEntradaSalida() {
        return cableEntradaSalida;
    }

    /**
     * @param cableEntradaSalida the cableEntradaSalida to set
     */
    public void setCableEntradaSalida(Cables cableEntradaSalida) {
        this.cableEntradaSalida = cableEntradaSalida;
    }

    /**
     * @return the compertaPadre
     */
    public Compuertas getCompuertaPadre() {
        return compuertaPadre;
    }

    /**
     * @param compertaPadre the compertaPadre to set
     */
    public void setCompuertaPadre(Compuertas compertaPadre) {
        this.compuertaPadre = compertaPadre;
    }

    /**
     * @return the ledEntrada
     */
    public Leds getLedEntrada() {
        return ledEntrada;
    }

    /**
     * @param ledEntrada the ledEntrada to set
     */
    public void setLedEntrada(Leds ledEntrada) {
        this.ledEntrada = ledEntrada;
    }

    /**
     * @return the switcheSalida
     */
   /* public Switches getSwitcheSalida() {
        return switcheSalida;
    }

    /**
     * @param switcheSalida the switcheSalida to set
     */
   /* public void setSwitcheSalida(Switches switcheSalida) {
        this.switcheSalida = switcheSalida;
    }*/

}

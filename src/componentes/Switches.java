package componentes;

import java.awt.Color;
import java.awt.Graphics;

public class Switches extends Componente {

    private Pines pinSwitch = null; // Pin asociado al switch
    protected int width, height;

    public Switches(int x, int y) {
        super(x, y);
        this.width = 20;  // Ancho del switch
        this.height = 20; // Alto del switch

        // Agregar el pin de entrada al switch, centrado dentro de él
        pinSwitch = new Pines(x + width / 2 - 5, y + height / 2 - 5, "SALIDA");
        tipoComp = tipoComponente.SWITCH;
    }

    // cambiamos el valor de 0 o 1 cada vez que se unde el boton
    public void setValor() {
        if (getValor() == -1 || getValor() == 1) {
            setValor(0);
        } else {
            setValor(1);
        }
        simular();
    }

    @Override
    public void simular() {
        getPinSwitch().simular(getValor()); /// se inicia la simulasion en el pn enviandole el valor de entrada 
    }

    // Getters y Setters
    public Pines getPinSwitch() {
        return pinSwitch;
    }

    public void setPinSwitch(Pines pinSwitch) {
        this.pinSwitch = pinSwitch;
    }

    public void draw(Graphics g) {
        // Dibujar el switch (rectángulo azul)
        if (getValor() == 1) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.BLUE);
        }

        g.drawRect(getX(), getY(), width, height);
        g.fillRect(getX(), getY(), width, height);

        // Dibujar el pin dentro del switch como un pequeño círculo negro
        g.setColor(Color.BLACK);
        g.fillOval(getX() + width / 2 - 5, getY() + height / 2 - 5, 10, 10); // Pin en el centro del switch

        // Actualizar la posición del pin para que siempre esté dentro del switch
        pinSwitch.setXY(getX() + width / 2 - 5, getY() + height / 2 - 5);
    }

    public boolean estaEnLaLinea(int posicionX, int posicionY) {
        return posicionX >= getX() && posicionX <= getX() + width && posicionY >= getY() && posicionY <= getY() + height;
    }

}

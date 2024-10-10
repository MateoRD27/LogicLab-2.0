package componentes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import componentes.Leds;


public class Leds extends Componente {
    protected int width, height;
    private Pines pin; // Pin asociado al LED

    public Leds(int x, int y) {
        super(x, y);
        this.width = 20;  // Ancho del LED
        this.height = 20; // Alto del LED
        // Agregar el pin de entrada
        agregarPin(x, y);

       
    }

    @Override
    public void simular(int valor) {
        setValor(valor);
    }
    
    


    public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    // Dibujar el LED
    g2d.setColor(getValor() == 1 ? Color.RED : Color.GRAY);
    g2d.fillOval(getX(), getY(), width, height);

    // Dibujar el pin como un pequeño círculo negro dentro del LED
    g2d.setColor(Color.BLACK);
    g2d.fillOval(getX() + width / 2 - 5, getY() + height / 2 - 5, 10, 10);

    // Actualizar la posición del pin para que siempre esté centrado dentro del LED
    pin.setXY(getX() + width / 2 - 5, getY() + height / 2 - 5);
}


    public boolean estaEnLaLinea(int posicionX, int posicionY) {
        return posicionX >= getX() && posicionX <= getX() + width && posicionY >= getY() && posicionY <= getY() + height;
    }
    
    // Metodo para aceptar el cable
    public void conectarPin(Cables cable) {
    if (cable != null) {
        pin.setCableEntradaSalida(cable); // Conectar el cable al pin del LED
        cable.conectarAPin(pin); // Conectar el cable al pin correctamente
    }
}
    
      private void agregarPin(int x, int y) {
                pin = new Pines(x + width / 2 - 5, y + height / 2 - 5, "ENTRADA"); // Posición inicial centrada en el LED
                pin.setLedEntrada(this);
    }


    // Getters y Setters
    public Pines getPin() {
        return pin;
    }

    public void setPin(Pines pin) {
        this.pin = pin;
    }

  


}

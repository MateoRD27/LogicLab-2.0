package compuertas;

/*
Integrantes de grupo: 
Torres Kevin
Ramos Mateo 
Gonzales Lauren 
 */
import componentes.Pines;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public final class And extends Compuertas {

    public And(int x, int y, String nomCompu) {
        super(x, y, 2, nomCompu);
        agregarPin(x, y, "SALIDA");
        agregarPin(x, y, "ENTRADA");
        agregarPin(x, y, "ENTRADA");
    }

    @Override
    //se dibuja la compuerta 
    public void draw(Graphics2D g) {

        g.setStroke(new BasicStroke(5));
        g.setColor(Color.GREEN);
        g.drawArc(getX(), getY(), 50, 50, 270, 180); // Arco
        g.drawLine(getX(), getY(), getX(), getY() + 50); // Linea vertical
        g.drawLine(getX(), getY(), getX() + 25, getY()); // Linea de arriba del AND
        g.drawLine(getX(), getY() + 50, getX() + 25, getY() + 50); // Linea de abajo del AND
        g.drawLine(getX() + 50, getY() + 25, getX() + 70, getY() + 25); // Linea que resalta afuera    
        switch (getNumEntra()) {
            case 3 -> {
                g.setStroke(new BasicStroke(5));
                g.setColor(Color.GREEN);
                g.drawLine(getX() - 20, getY() + 10, getX(), getY() + 10); // Patita 1
                g.drawLine(getX() - 20, getY() + 25, getX(), getY() + 25); // Patita 2
                g.drawLine(getX() - 20, getY() + 40, getX(), getY() + 40); // Patita 3
                for (Pines pine : getPines()) {
                    if (pine.getTipoPin().equals("SALIDA")) {
                        pine.setXY(getX() + 70, getY() + 25);  // añade las nuevas coordenadas del pin
                    } else {
                        int index = getPines().indexOf(pine); // se obtiene el indice de lo pnes en el array 
                        pine.setXY(getX()-20, getY() + (10 + ((index-1)*15)));
                    }
                }
                drawPin(g);
            }
            case 4 -> {
                g.setStroke(new BasicStroke(5));
                g.setColor(Color.GREEN);
                g.drawLine(getX() - 20, getY() + 5, getX(), getY() + 5);    // Patita 1
                g.drawLine(getX() - 20, getY() + 18, getX(), getY() + 18);  // Patita 2
                g.drawLine(getX() - 20, getY() + 31, getX(), getY() + 31);  // Patita 3
                g.drawLine(getX() - 20, getY() + 44, getX(), getY() + 44);  // Patita 4
                
                for (Pines pine : getPines()) {
                    if (pine.getTipoPin().equals("SALIDA")) {
                        pine.setXY(getX() + 70, getY() + 25);
                    } else {
                        int index = getPines().indexOf(pine); // se obtiene el indice de lo pnes en el array 
                        pine.setXY(getX()-20, getY() + (5 + ((index-1)*13)));
                    }
                }
                drawPin(g);
                
            }
            default -> {
                g.drawLine(getX() - 20, getY() + 15, getX(), getY() + 15); // Patita 1
                g.drawLine(getX() - 20, getY() + 35, getX(), getY() + 35); // Patita 2
                for (Pines pine : getPines()) {
                    if (pine.getTipoPin().equals("SALIDA")) {
                        pine.setXY(getX() + 70, getY() + 25);
                    } else {
                        int index = getPines().indexOf(pine); // se obtiene el indice de lo pnes en el array 
                        pine.setXY(getX()-20, getY() + (15 + ((index-1)*20)));
                    }
                }
                drawPin(g);
            }
        }

    }

    @Override
    public void comprobarTabla() { // por definir
        int v=1;  
        for (int i = 1; i < getPines().size(); i++) {
            v=v*getPines().get(i).getValor();
        }
        
        // 
        setValor(v);
        asignarValorSalidaAPin();
    }

    @Override
    public void setNumEntra(int numEntra) { // esto es para agregar los pines segun el numero de entradas
        super.setNumEntra(numEntra);
        if (numEntra >= getPines().size()) {
            for (int i = getPines().size(); i <= numEntra; i++) {
                agregarPin(getX(), getY(), "ENTRADA");
            }
        }else if(getPines().size() > numEntra){
            eliminarPin();
        }
    }

}

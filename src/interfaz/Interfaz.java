package interfaz;

/*
Integrantes de grupo: 
Torres Kevin
Ramirez Leonardo
Ramos Mateo
Gonzales Lauren 
 */
import circuito.Circuitos;
import componentes.Cables;
import componentes.Leds;
import componentes.Switches;
import compuertas.Compuertas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class Interfaz extends javax.swing.JFrame implements NombreCompuerta {

        // Instancia de la clase Circuitos, que maneja los circuitos lógicos

    Circuitos circuito = new Circuitos();
    //private ArrayList<Compuertas> compuertas = new ArrayList<>();  //arreglo donde almecenar las compuertas 
    private Compuertas arrastrarCompuerta = null; //para tener la compuerta que se esta deslizando
    private Compuertas clickCompuerta = null; //para tener la compuerta que se esta likeando para eliminar
    private int offsetX, offsetY;
    private Leds arrastrarLed = null; // LED que se está deslizando
    private Switches arrastredSwitch = null; //Switch que se está arrastrando
    private Cables arrastrarCable = null; // Ahora se maneja correctamente el arrastre de cables

    CompuertaLogica compuert;
    private boolean modoDibujarCable = false;// modo dibujar cable
    private boolean modoSimulacion = false;  // para facilitar el modo simulacion 
    private int xInicioCable, yInicioCable, xFinCable, yFinCable; // coordenadas x,y y x1,y1

    private static final int POSICION_INICIAL_X = 100;
    private static final int POSICION_INICIAL_Y = 100;

    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem item1 = new JMenuItem("Eliminar compuerta");
    JMenuItem item2 = new JMenuItem("Añadir 3 entradas");
    JMenuItem item3 = new JMenuItem("Añadir 4 entradas");

    // Menús emergentes para LEDs, Switches y Cables
    JPopupMenu ledPopupMenu = new JPopupMenu();
    JMenuItem eliminarLed = new JMenuItem("Eliminar LED");

    JPopupMenu switchPopupMenu = new JPopupMenu();
    JMenuItem eliminarSwitch = new JMenuItem("Eliminar Switch");

    JPopupMenu cablePopupMenu = new JPopupMenu();
    JMenuItem eliminarCable = new JMenuItem("Eliminar Cable");

    private int index = 0;  // Índice para llevar el control de las compuertas o componentes

    public Interfaz() {
        initComponents();
        popupMenu.add(item1);
        popupMenu.add(item2);
        popupMenu.add(item3);

        // Agregar opciones de popup para LEDs, Switches y Cables
        ledPopupMenu.add(eliminarLed);
        switchPopupMenu.add(eliminarSwitch);
        cablePopupMenu.add(eliminarCable);

        // En el constructor, asegurarte de que los listeners estén bien configurados
        eliminarLed.addActionListener(e -> {
            if (arrastrarLed != null) {
                clickEliminarLed(arrastrarLed);
            }
        });

        eliminarSwitch.addActionListener(e -> {
            if (arrastredSwitch != null) {
                clickEliminarSwitch(arrastredSwitch);
            }
        });

        eliminarCable.addActionListener(e -> {
            if (arrastrarCable != null) {
                clickEliminarCable(arrastrarCable);
            }
        });

                // Añade un mouse listener para detectar acciones del mouse sobre el panel

        Panel.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                        // Se ejecuta cuando el botón del mouse es presionado

                if (!modoSimulacion) { // Solo si no está en modo simulación
                    if (modoDibujarCable) { // Si el modo de dibujar cables está activado
                        // Guardamos las coordenadas de lo que nos dan
                        xInicioCable = evt.getX();
                        yInicioCable = evt.getY();
                    } else {
                                        // Si no es el modo de dibujar cable, se llama a manejar el clic normal

                        onMousePressed(evt);
                    }
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
        // Se ejecuta cuando el botón del mouse es soltado

                if (!modoSimulacion) {
                    if (modoDibujarCable) {
            // Guardamos las coordenadas finales del cable cuando el mouse es soltado

                        xFinCable = evt.getX();
                        yFinCable = evt.getY();
                        // Aquí podrías agregar la lógica para "guardar" el cable, si es necesario
                        circuito.agregarCable(xInicioCable, yInicioCable, xFinCable, yFinCable);
                        repaint();
                    } else {
                        onMouseReleased();
                    }
                }

            }
        });

        // Configuración de MouseMotionListener para el draw, maneja el arrastre del mouse

        Panel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
        // Se ejecuta mientras el mouse es arrastrado

                if (!modoSimulacion) {
                    if (modoDibujarCable) {
                        xFinCable = evt.getX();
                        yFinCable = evt.getY();
                        repaint();
                    } else {
                        onMouseDragged(evt);
                    }
                }

            }
        });

        // Configuración de MouseListener para detectar clics en el panel

        Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                // Mostrar el menú solo si es un clic derecho
                // Detecta el tipo de clic (derecho o izquierdo) 

                if (SwingUtilities.isRightMouseButton(e) && modoSimulacion == false) {
                    onMouseLine(e);
                 // Si se hace clic derecho y no está en modo simulación, muestra el menú contextual para las compuertas

                }

                if (SwingUtilities.isLeftMouseButton(e) && modoSimulacion == true) {
                    // llamamos a la funcionalidad de los swiches ///////////---------------////////////---------------/////////////
                    circuito.iniciarSimulación(e.getX(), e.getY());
                    repaint();
                 // Si se hace clic izquierdo y está en modo simulación, llama a la función de simulación del circuito

                }
            }
        });

        // Acción del menú emergente "Eliminar compuerta" - item1

        item1.addActionListener(e -> {
            if (clickCompuerta != null) {

                circuito.eliminarCompuerta(clickCompuerta);// Elimina la compuerta de la lista
                clickCompuerta = null;  // Reinicia la referencia
                repaint();  // Vuelve a pintar el panel
                JOptionPane.showMessageDialog(null, "Compuerta eliminada, quedan " + circuito.getCompuertas().size() + " compuertas.");

            } else {
                JOptionPane.showMessageDialog(null, "No hay compuerta seleccionada.");
            }
        });

        item2.addActionListener(e -> {
            if (clickCompuerta != null) {

                index = circuito.getCompuertas().indexOf(clickCompuerta); // obtenemos el indice del objeto que esta en la lista 
                circuito.getCompuertas().get(index).setNumEntra(3); // aumentamos el numero de entradas de la compuerta 
                clickCompuerta = null;  // Reinicia la referencia
                repaint();  // Vuelve a pintar el panel
                index = 0;
            } else {
                JOptionPane.showMessageDialog(null, "No hay compuerta seleccionada.");
            }
        });

        item3.addActionListener(e -> {
            if (clickCompuerta != null) {
                index = circuito.getCompuertas().indexOf(clickCompuerta); // obtenemos el indice del objeto que esta en la lista 

                circuito.getCompuertas().get(index).setNumEntra(4); // aumentamos el numero de entradas de la compuerta 
                clickCompuerta = null;  // Reinicia la referencia
                repaint();  // Vuelve a pintar el panel

                index = 0;
            } else {
                JOptionPane.showMessageDialog(null, "No hay compuerta seleccionada.");
            }
        });

    }

// metodos 
 /*
    public void hacerZoom() {

    }

    public void moverLienzo() {

    }
*/
    // Cambios en los métodos de eliminación
    
    // Método para eliminar un LED cuando se hace clic en el botón 

    private void clickEliminarLed(Leds led) {
        // Elimina el LED del circuito
        circuito.eliminarLed(led);
        arrastrarLed = null; // Reiniciar la referencia
        repaint();
        JOptionPane.showMessageDialog(null, "LED eliminado.");
    }

    private void clickEliminarSwitch(Switches switches) {
                // Elimina el switch del circuito

        circuito.eliminarSwitch(switches);
        arrastredSwitch = null; // Reiniciar la referencia
        repaint();
        JOptionPane.showMessageDialog(null, "Switch eliminado.");
    }

    private void clickEliminarCable(Cables cable) {
 
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar este cable?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            // Elimina el cable del circuito
            circuito.eliminarCable(cable); // Asegúrate de que este método esté correctamente implementado
            // Actualiza la interfaz
            arrastrarCable = null;
            repaint(); // Vuelve a dibujar el panel para reflejar los cambios
            JOptionPane.showMessageDialog(null, "Cable Eliminado.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BotonAnd = new javax.swing.JButton();
        BotonOr = new javax.swing.JButton();
        BotonNot = new javax.swing.JButton();
        Panel = new javax.swing.JPanel();
        BotonNor = new javax.swing.JButton();
        buttonSwitch = new javax.swing.JButton();
        led = new javax.swing.JButton();
        cable = new javax.swing.JButton();
        simular = new javax.swing.JButton();
        nand = new javax.swing.JButton();
        xnor = new javax.swing.JButton();
        xor = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        BotonAnd.setText("AND");
        BotonAnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonAndActionPerformed(evt);
            }
        });

        BotonOr.setText("OR");
        BotonOr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonOrActionPerformed(evt);
            }
        });

        BotonNot.setText("NOT");
        BotonNot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonNotActionPerformed(evt);
            }
        });

        Panel.setBackground(new java.awt.Color(255, 255, 255));
        Panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Panel de Compuertas dibujadas"));

        javax.swing.GroupLayout PanelLayout = new javax.swing.GroupLayout(Panel);
        Panel.setLayout(PanelLayout);
        PanelLayout.setHorizontalGroup(
            PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 963, Short.MAX_VALUE)
        );
        PanelLayout.setVerticalGroup(
            PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        BotonNor.setText("NOR");
        BotonNor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonNorActionPerformed(evt);
            }
        });

        buttonSwitch.setText("Añadir Switch");
        buttonSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSwitchActionPerformed(evt);
            }
        });

        led.setText("Añadir Led");
        led.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ledActionPerformed(evt);
            }
        });

        cable.setText("Añadir Cable");
        cable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cableActionPerformed(evt);
            }
        });

        simular.setText("Simular");
        simular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simularActionPerformed(evt);
            }
        });

        nand.setText("NAND");
        nand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nandActionPerformed(evt);
            }
        });

        xnor.setText("XNOR");
        xnor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xnorActionPerformed(evt);
            }
        });

        xor.setText("XOR");
        xor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonSwitch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(led, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BotonAnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BotonOr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BotonNot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BotonNor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(simular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nand, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xnor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(xor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BotonAnd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BotonOr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BotonNot)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nand)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xnor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BotonNor)
                        .addGap(27, 27, 27)
                        .addComponent(buttonSwitch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(led)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cable)
                        .addGap(18, 18, 18)
                        .addComponent(simular, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(37, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D gr = (Graphics2D) Panel.getGraphics();
        for (Compuertas compu : circuito.getCompuertas()) {
            compu.draw(gr);
        }

        // Dibujar cable si estamos en modo de dibujo
        if (modoDibujarCable) {
            gr.setColor(Color.BLACK);
            gr.setStroke(new BasicStroke(2));
            gr.drawLine(xInicioCable, yInicioCable, xFinCable, yFinCable);
        }

        for (Cables cable : circuito.getCables()) {
            cable.draw(gr);
        }

        // Dibujar LEDs
        for (Leds led : circuito.getLeds()) {
            led.draw(gr);
        }
        //Dibujar switches
        for (Switches switches : circuito.getSwitches()) {
            switches.draw(gr);
        }

    }

    private void onMousePressed(MouseEvent evt) {
            // Verifica si se hace clic en una compuerta y almacena la referencia para moverla

        for (Compuertas compu : circuito.getCompuertas()) {
            if (compu.estaEnLaLinea(evt.getX(), evt.getY())) {
                arrastrarCompuerta = compu;
                offsetX = evt.getX() - compu.getX(); // Calcula el desplazamiento con respecto a la compuerta
                offsetY = evt.getY() - compu.getY();
                break;
            }
        }
            // Verifica si se hace clic en un LED y almacena la referencia para moverlo

        for (Leds led : circuito.getLeds()) {
            if (led.estaEnLaLinea(evt.getX(), evt.getY())) {
                arrastrarLed = led;
                offsetX = evt.getX() - led.getX();
                offsetY = evt.getY() - led.getY();
                break;
            }
        }
        // Verifica si se está haciendo clic en un switch
        for (Switches switches : circuito.getSwitches()) {     // Verifica si se hace clic en un switch y almacena la referencia para moverlo

            if (switches.estaEnLaLinea(evt.getX(), evt.getY())) {
                arrastredSwitch = switches;
                offsetX = evt.getX() - switches.getX();
                offsetY = evt.getY() - switches.getY();
                break;
            }
        }
    }

   
    private void onMouseLine(MouseEvent evt) {
        for (Compuertas compu : circuito.getCompuertas()) {
            if (compu.estaEnLaLinea(evt.getX(), evt.getY())) {
                clickCompuerta = compu;
                popupMenu.show(Panel, 800, -90); // mostrar el panel
                repaint();
                break;
            }

        }
        // Verificar LEDs
        for (Leds led : circuito.getLeds()) {
            if (led.estaEnLaLinea(evt.getX(), evt.getY())) {
                arrastrarLed = led; // Almacenar el LED seleccionado
                ledPopupMenu.show(Panel, evt.getX() + 10, evt.getY() + 10); // Mostrar menú para LED
                return; // Salir del método
            }
        }

        // Verificar Switches
        for (Switches switches : circuito.getSwitches()) {
            if (switches.estaEnLaLinea(evt.getX(), evt.getY())) {
                arrastredSwitch = switches; // Almacenar el switch seleccionado
                switchPopupMenu.show(Panel, evt.getX(), evt.getY()); // Mostrar menú para switch
                return; // Salir del método
            }
        }

        // Verificar Cables
        for (Cables cable : circuito.getCables()) {
            if (cable.estaEnLaLinea(evt.getX(), evt.getY())) {
                arrastrarCable = cable; // Almacenar el cable seleccionado
                cablePopupMenu.show(Panel, evt.getX(), evt.getY()); // Mostrar menú para cable
                return; // Salir del método
            }
        }
    }     // Verifica si el mouse se encuentra sobre una compuerta, LED o switch para mostrar el menú contextual


    private void onMouseReleased() {
        arrastrarCompuerta = null;  // Dejar de deslizar la compuerta
        arrastrarLed = null; // Dejar de deslizar el LED
        arrastredSwitch = null; // dejar de arrastrar el switch
    }

    private void onMouseDragged(MouseEvent evt) {
        if (arrastrarCompuerta != null) {
            // Actualizar la posición de la compuerta que se esta moviendo 
            arrastrarCompuerta.cambiarPosicion(evt.getX() - offsetX, evt.getY() - offsetY);
            repaint();
        }
        if (arrastrarLed != null) {
            arrastrarLed.setX(evt.getX() - offsetX);
            arrastrarLed.setY(evt.getY() - offsetY);
            repaint();
        }
        if (arrastredSwitch != null) {
            arrastredSwitch.setX(evt.getX() - offsetX);
            arrastredSwitch.setY(evt.getY() - offsetY);
            repaint();
        }
    }

    public void nuevaCompuerta(CompuertaLogica compuerta) { // agregar conpuerta al circuito
        int x = POSICION_INICIAL_X;
        int y = POSICION_INICIAL_Y;
        circuito.agregarCompuerta(compuerta, x, y);
        repaint();
    }


    private void BotonAndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonAndActionPerformed

        if (!modoSimulacion) {
            compuert = CompuertaLogica.AND;
            nuevaCompuerta(compuert);
        }
    }//GEN-LAST:event_BotonAndActionPerformed

    private void BotonOrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonOrActionPerformed

        if (!modoSimulacion) {
            compuert = CompuertaLogica.OR;
            nuevaCompuerta(compuert);
        }
    }//GEN-LAST:event_BotonOrActionPerformed

    private void BotonNotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonNotActionPerformed

        if (!modoSimulacion) {
            compuert = CompuertaLogica.NOT;
            nuevaCompuerta(compuert);
        }
    }//GEN-LAST:event_BotonNotActionPerformed

    private void BotonNorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonNorActionPerformed

        if (!modoSimulacion) {
            compuert = CompuertaLogica.NOR;
            nuevaCompuerta(compuert);
        }

    }//GEN-LAST:event_BotonNorActionPerformed

    private void buttonSwitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSwitchActionPerformed
        Switches nuevoSwitches = new Switches(POSICION_INICIAL_X, POSICION_INICIAL_Y);
        circuito.agregarWSwitches(nuevoSwitches); // Agregar el switch al circuito
        repaint();
    }//GEN-LAST:event_buttonSwitchActionPerformed

    private void cableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cableActionPerformed
        modoDibujarCable = !modoDibujarCable;
    }//GEN-LAST:event_cableActionPerformed

    private void ledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ledActionPerformed
        // Crear un nuevo LED en una posición fija o aleatoria
        circuito.agregarLed(POSICION_INICIAL_X, POSICION_INICIAL_Y); // Agregar el LED al circuito
        repaint();
    }//GEN-LAST:event_ledActionPerformed

    private void simularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simularActionPerformed
        modoSimulacion = !modoSimulacion;
        if(modoSimulacion==true){
            circuito.iniciarSimulacion();
            repaint();
        }
    }//GEN-LAST:event_simularActionPerformed

    private void nandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nandActionPerformed
        // TODO add your handling code here:

        if (!modoSimulacion) {
            compuert = CompuertaLogica.NAND;
            nuevaCompuerta(compuert);
        }

    }//GEN-LAST:event_nandActionPerformed

    private void xnorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xnorActionPerformed
        // TODO add your handling code here:

        if (!modoSimulacion) {
            compuert = CompuertaLogica.XNOR;
            nuevaCompuerta(compuert);
        }
    }//GEN-LAST:event_xnorActionPerformed

    private void xorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xorActionPerformed
        // TODO add your handling code here:

        if (!modoSimulacion) {
            compuert = CompuertaLogica.XOR;
            nuevaCompuerta(compuert);
        }

    }//GEN-LAST:event_xorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonAnd;
    private javax.swing.JButton BotonNor;
    private javax.swing.JButton BotonNot;
    private javax.swing.JButton BotonOr;
    private javax.swing.JPanel Panel;
    private javax.swing.JButton buttonSwitch;
    private javax.swing.JButton cable;
    private javax.swing.JButton led;
    private javax.swing.JButton nand;
    private javax.swing.JButton simular;
    private javax.swing.JButton xnor;
    private javax.swing.JButton xor;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

}

package circuito;

import componentes.Cables;
import componentes.Leds;
import componentes.Pines;
import componentes.Switches;
import compuertas.And;
import compuertas.Compuertas;
import compuertas.Nand;
import compuertas.Nor;
import compuertas.Not;
import compuertas.Or;
import compuertas.Xnor;
import compuertas.Xor;
import interfaz.NombreCompuerta;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/*
Integrantes de grupo: 
Torres Kevin
Ramos Mateo 
Gonzales Lauren 
 */
public class Circuitos implements NombreCompuerta {

    // Atributos: listas para almacenar los diferentes componentes del circuito.
    private ArrayList<Compuertas> compuertas = new ArrayList<>();  // Arreglo de compuertas lógicas
    private ArrayList<Cables> cables = new ArrayList<>();          // Arreglo de cables
    private ArrayList<Leds> leds = new ArrayList<>();              // Arreglo de LEDs
    private ArrayList<Switches> switches = new ArrayList<>();      // Arreglo de switches (interruptores)

    // Método para crear una nueva compuerta lógica y agregarla al circuito.
    public void agregarCompuerta(CompuertaLogica compuert, int x, int y) {
        Compuertas compu = crearCompu(compuert); // Crear la compuerta correspondiente
        compu.cambiarPosicion(x, y); // Cambiar la posición de la compuerta en el gráfico
        getCompuertas().add(compu); // Agregar la compuerta a la lista de compuertas
    }

    // Método para agregar un cable entre dos puntos del circuito.
    // Se determina si el cable conecta dos pines o si se conecta a otros cables.
    public void agregarCable(int x, int y, int x1, int y1) {
        Pines origen = encontrarPinCercano(x, y); // Buscar el pin de origen
        Pines destino = encontrarPinCercano(x1, y1); // Buscar el pin de destino
        Cables cableOrigen = encontrarCablesCercanos(x, y); // Buscar cable cercano al origen
        Cables cableDestino = encontrarCablesCercanos(x1, y1); // Buscar cable cercano al destino

        Leds led = encontrarLed(x, y);
        if (led == null) {
            led = encontrarLed(x1, y1);
        }
        Switches switche = encontrarSwitch(x1, y1);
        if (switche == null) {
            switche = encontrarSwitch(x, y);
        }

        // Crear el nuevo cable que conecta los puntos dados
        Cables cable = new Cables(x, y, x1, y1);

        // si se encuanra un led en l posicion 
        if (led != null) {
            led.getPin().setCableEntradaSalida(cable);
            cable.setDestinoPinCable(led.getPin());
        }

        // si se encuatra un switch se conecta el cable al pin del switch 
        if (switche != null) {
            switche.getPinSwitch().setCableEntradaSalida(cable);
            cable.setOrigenPinCable(switche.getPinSwitch());
        }

        // Si se encuentra el pin de origen, conectamos el cable a ese pin
        if (origen != null) {
            origen.setCableEntradaSalida(cable); // Conecta el cable al pin de origen
            cable.conectarAPin(origen); // Asocia el pin con el cable
        }

        // Si se encuentra el pin de destino, conectamos el cable a ese pin
        if (destino != null) {
            destino.setCableEntradaSalida(cable); // Conecta el cable al pin de destino
            cable.conectarAPin(destino); // Asocia el pin con el cable
        }

        // Si se encuentra un cable cercano al origen, se conectan ambos cables
        if (cableOrigen != null) {
            cableOrigen.conectarACable(cable); // Conectar el nuevo cable al cable de origen
            cable.setCableOrigen(cableOrigen); // Asocia el nuevo cable con el cable de origen
        }

        // Si se encuentra un cable cercano al destino, se conectan ambos cables
        if (cableDestino != null) {
            cableDestino.conectarACable(cable); // Conectar el nuevo cable al cable de destino
            cable.setCableOrigen(cableDestino); // Asocia el nuevo cable con el cable de destino
        }

        // Finalmente, agregamos el cable a la lista de cables
        cables.add(cable);

        // Si no se encontraron los pines ni los cables de origen/destino, se reporta un error
        if (origen == null && destino == null && cableOrigen == null) {
            System.out.println("No se pudo conectar el cable, los pines no fueron encontrados.");
        }
    }

    // Métodos para iniciar la simulación del circuito
    public void iniciarSimulación(int x, int y) {
        Switches switche = encontrarSwitch(x, y);
        if (switche != null) {
            switche.setValor();
        }
    }
    // inicia todos los switches
    public void iniciarSimulacion(){
        for (Switches swit : getSwitches()) {
            swit.setValor();
        }
    }

    public void finalizarSimulación() {
        // Implementar lógica para finalizar la simulación
    }

    // Método para validar errores en el circuito
    public void validarErrores() {
        // Implementar lógica para validar errores
    }

//para encontrar el led seleccionado 
    private Leds encontrarLed(int x, int y) {
        for (Leds ledd : leds) {
            if (ledd.estaEnLaLinea(x, y)) {
                return ledd;
            }
        }
        return null;
    }

    // para encontrar el switch seleccionado 
    private Switches encontrarSwitch(int x, int y) {
        for (Switches switc : switches) {
            if (switc.estaEnLaLinea(x, y)) {
                return switc;
            }
        }
        return null;
    }

    // Método auxiliar para encontrar un cable cercano a las coordenadas dadas
    private Cables encontrarCablesCercanos(int x, int y) {
        for (Cables cabl : cables) {
            if (cabl.estaEnLaLinea(x, y)) {
                return cabl; // Retorna el cable si se encuentra cercano a las coordenadas
            }
        }
        return null; // Si no se encuentra, retorna null
    }

    // Método auxiliar para encontrar un pin cercano a las coordenadas dadas
    private Pines encontrarPinCercano(int x, int y) {
        for (Compuertas compuerta : compuertas) {
            for (Pines pin : compuerta.getPines()) {
                if (pin.estaEnLaLinea(x, y)) {
                    return pin; // Retorna el pin si se encuentra cercano a las coordenadas
                }
            }
        }

        return null; // Si no se encuentra, retorna null
    }
    // Lógica para mover la posición de un LED en el circuito.

    public void moverLed(Leds led, int newX, int newY) {
        led.setX(newX); // Actualiza la posición X del LED
        led.setY(newY); // Actualiza la posición Y del LED
    }

    // Lógica para mover la posición de un switch en el circuito.
    public void moverSwitch(Switches swich, int newX, int newY) {
        swich.setX(newX); // Actualiza la posición X del switch
        swich.setY(newY); // Actualiza la posición Y del switch
    }

    // Método para agregar un LED al circuito.
    public void agregarLed(int x, int y) {
        Leds led =  new Leds(x, y);
        leds.add(led); // Agregar LED al arreglo
    }

    // Método para agregar un switch (interruptor) al circuito.
    public void agregarWSwitches(Switches swich) {
        switches.add(swich); // Agregar switch al arreglo
    }

    // Método para eliminar una compuerta del circuito
    public void eliminarCompuerta(Compuertas comp) {
        getCompuertas().remove(comp); // Elimina la compuerta de la lista
    }

    // Método para crear una compuerta específica según su nombre
    private Compuertas crearCompu(CompuertaLogica compuert) {
        switch (compuert.getNombre()) {
            case "AND":
                return new And(0, 0, compuert.getNombre());
            case "OR":
                return new Or(0, 0, compuert.getNombre());
            case "NOT":
                return new Not(0, 0, compuert.getNombre());
            case "NOR":
                return new Nor(0, 0, compuert.getNombre());
            case "NAND":
                return new Nand(0, 0, compuert.getNombre());
            case "XOR":
                return new Xor(0, 0, compuert.getNombre());
            case "XNOR":
                return new Xnor(0, 0, compuert.getNombre());
            default:
                return null; // Si el nombre de la compuerta no coincide, retorna null
        }
    }

    // Getters para acceder a las listas de componentes
    public ArrayList<Compuertas> getCompuertas() {
        return compuertas;
    }

    public ArrayList<Cables> getCables() {
        return cables;
    }

    public ArrayList<Leds> getLeds() {
        return leds;
    }

    public ArrayList<Switches> getSwitches() {
        return switches;
    }

    // Métodos para eliminar componentes específicos del circuito
    public void eliminarLed(Leds led) {
        getLeds().remove(led); // Elimina el LED de la lista
    }

    public void eliminarSwitch(Switches switchObj) {
        getSwitches().remove(switchObj); // Elimina el switch de la lista
    }

    // eliminar completamente un cable
    public void eliminarCable(Cables cable) {
        if (cable.getOrigenPinCable() != null) {
            cable.getOrigenPinCable().setCableEntradaSalida(null);
        }
        if (cable.getDestinoPinCable() != null) {
            cable.getDestinoPinCable().setCableEntradaSalida(null);
        }
        if (cable.getCableOrigen() != null) {
            cable.getCableOrigen().getCablesConectados().remove(cable);
        }
        if (!cable.getCablesConectados().isEmpty()) {
            cable.quitarReferenciaAcablesHijos();
        }

        getCables().remove(cable); // Elimina el cable de la lista
        System.out.println(getCables().size() + "\n");
    }

}

package interfaz;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author Mateo
 */
public interface NombreCompuerta {
    public enum CompuertaLogica {
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    XOR("XOR"),
    NAND("NAND"),
    XNOR("XNOR"),
    NOR("NOR");

    private final String nombre;

    // Constructor para asignar el valor de cadena
    CompuertaLogica(String nombre) {
        this.nombre = nombre;
    }

    // Método para obtener el valor de cadena
    public String getNombre() {
        return nombre;
    }
}

}

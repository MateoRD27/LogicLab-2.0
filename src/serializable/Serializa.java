/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serializable;

import circuito.Circuitos;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Mateo
 */
public class Serializa {

    public static void serializarCircuito(Circuitos circuito) {
        // Crear un JFileChooser en modo de guardado
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo de serialización");

        // Mostrar la ventana de guardado
        int seleccion = fileChooser.showSaveDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();

            // Asegurarse de que el archivo tenga extensión .ser
            if (!archivo.getName().endsWith(".ser")) {
                archivo = new File(archivo.getAbsolutePath() + ".ser");
            }

            // Intentar serializar el objeto en la ruta seleccionada
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                oos.writeObject(circuito);
                JOptionPane.showMessageDialog(null, "Archivo guardado correctamente en: " + archivo.getAbsolutePath());
                System.out.println("Objeto Circuitos serializado y guardado en: " + archivo.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al serializar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Operación cancelada por el usuario.");
        }
    }

    public static Circuitos deserializarCircuito() {
        // Crear un JFileChooser para seleccionar el archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Abrir el cuadro de diálogo y almacenar el resultado
        int seleccion = fileChooser.showOpenDialog(null);

        // Verificar si se seleccionó un archivo
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();

            // Intentar deserializar el objeto desde el archivo seleccionado
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                // Leer el objeto y retornarlo
                Circuitos circuito = (Circuitos) ois.readObject();
                System.out.println("Objeto Circuitos deserializado correctamente.");
                return circuito;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al deserializar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }

        return null; // Retorna null si no se pudo deserializar el objeto
    }

}

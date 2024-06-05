
package controlers.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class TestScript {
    public static void main(String[] args) {
        Object[] objArray = new Object[] {new Cor(1, 2), new Cor(3, 4), "No soy un objeto Cor", new Cor(5, 6)};
        Cor[] corArray = convertToCor1(objArray);
        
        // Imprime el arreglo de objetos Cor resultante
        System.out.println("Arreglo de objetos Cor resultante:");
        for (Cor cor : corArray) {
            if (cor != null) {
                System.out.println("Cor[x=" + cor.x + ", y=" + cor.y + "]");
            } else {
                System.out.println("Elemento nulo en el arreglo de objetos Cor.");
            }
        }
    }

    public static Cor[] convertToCor(Object[] objArray) {
        if (objArray.length % 2 != 0) {
            throw new IllegalArgumentException("El arreglo no tiene la estructura correcta para convertir a Cor");
        }
    
        int numOfCors = objArray.length / 2;
        Cor[] corArray = new Cor[numOfCors];
    
        for (int i = 0; i < numOfCors; i++) {
            if (!(objArray[2 * i] instanceof Integer) || !(objArray[2 * i + 1] instanceof Integer)) {
                throw new IllegalArgumentException("El arreglo contiene elementos que no son enteros");
            }
            int x = (Integer) objArray[2 * i];
            int y = (Integer) objArray[2 * i + 1];
            corArray[i] = new Cor(x, y);
        }
    
        return corArray;
    }


    public static Cor[] convertToCor1(Object[] objArray) {
        Cor[] corArray = new Cor[objArray.length];
        
        for (int i = 0; i < objArray.length; i++) {
            // Comprueba si el elemento del arreglo es una instancia de la clase Cor
            if (objArray[i] instanceof Cor) {
                corArray[i] = (Cor) objArray[i]; // Realiza un casting al tipo Cor
            } else {
                // Si el elemento no es una instancia de la clase Cor, imprime un mensaje de advertencia
                System.err.println("Elemento en la posición " + i + " no es una instancia de la clase Cor.");
            }
        }
        
        return corArray;
    }
}

class Cor {
    public int x;
    public int y;

    public Cor(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


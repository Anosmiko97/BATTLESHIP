
package controlers.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

public class TestScript {
    public static void main(String[] args) {
        String str = "[1|1],[2|1],[3|1],[3|4],[4|1],[4|4],[5|1],[5|4],[5|6],[6|4],[6|6],[6|8],[7|3],[7|8],[8|3],[8|8],[9|3],";
        Cor[] cor = strToCorArray(str);
        System.out.println(cor[0].x + "," + cor[0].y);
        printCorArray(cor);
    }

    public static void printCorArray(Cor[] array) {
        for (Cor cor : array) {
            System.out.println(cor.x + "," + cor.y);
        }
    }

    public static Cor[] strToCorArray(String str) {
        Cor[] corArray = new Cor[17];
        str = str.substring(0, str.length() - 1);
        str = str.replace("[", "").replace("]", "");
        String[] pairs = str.split(",");

        for (int i = 0; i < pairs.length; i++) {
            String[] nums = pairs[i].split("\\|");
            int x = Integer.parseInt(nums[0]);
            int y = Integer.parseInt(nums[1]);
            addTo(corArray, x, y);
        }

        return corArray;
    }

    private static void addTo(Cor[] cors, int x, int y) {
        for (int i = 0; i < cors.length; i++) {
            if (cors[i] == null) {
                Cor cor = new Cor(x, y);
                cors[i] = cor;
                break;
            }
        }
    }


    public static int[][] convertStringToIntArray(String str) {
        str = str.substring(0, str.length() - 1);
        str = str.replace("[", "").replace("]", "");
        String[] pairs = str.split(",");
        printArray(pairs);
        int[][] result = new int[pairs.length][2];

        for (int i = 0; i < pairs.length; i++) {
            String[] nums = pairs[i].split("\\|");
            result[i][0] = Integer.parseInt(nums[0]);
            result[i][1] = Integer.parseInt(nums[1]);
        }

        return result;
    }

    private static void printArray(String[] corArray) {
        for (String cor : corArray) {
            if (cor != null) {
                System.out.print(cor + ",");
            } else {
                System.out.print("Coordenada: null");
            }
        }
        System.out.println();
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


    
}

class Cor {
    public int x;
    public int y;

    public Cor(int x, int y) {
        this.x = x;
        this.y = y;
    }
}


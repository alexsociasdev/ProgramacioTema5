import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Scanner;

public class Activitat5_3 {

    public static String calcularHashDeFitxer(File fitxer, String algoritmeHash) throws Exception {
        if (!fitxer.exists()) {
            throw new FileNotFoundException("El fitxer no existeix.");
        }

        MessageDigest digest = MessageDigest.getInstance(algoritmeHash);
        FileInputStream fis = new FileInputStream(fitxer);

        byte[] byteArray = new byte[1024];
        int bytesCount;

        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        fis.close();

        byte[] bytes = digest.digest();
        return HexFormat.of().formatHex(bytes);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Introdueix el camí del fitxer:");
            String pathFitxer = scanner.nextLine();
            File fitxer = new File(pathFitxer);

            System.out.println("Selecciona l'algoritme de hash (ex: SHA-256):");
            String algoritmeHash = scanner.nextLine();

            String hashActual = calcularHashDeFitxer(fitxer, algoritmeHash);
            System.out.println("Valor del hash calculat: " + hashActual);

            System.out.println("Introdueix el valor de hash anterior per comparar, o deixa buit si no n'hi ha:");
            String hashAnterior = scanner.nextLine();

            if (!hashAnterior.isEmpty() && hashAnterior.equals(hashActual)) {
                System.out.println("El fitxer no ha estat alterat.");
            } else if (!hashAnterior.isEmpty()) {
                System.out.println("El fitxer ha estat alterat o el hash proporcionat no és correcte.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("L'algoritme de hash no és suportat.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}

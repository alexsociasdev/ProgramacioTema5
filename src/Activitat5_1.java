import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class Activitat5_1 {

    public static SecretKey generarClauSecreta() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // Utilitza claus de 256 bits
        return keyGenerator.generateKey();
    }

    public static String xifrarMissatge(String missatge, SecretKey clau) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, clau);
        byte[] missatgeXifrat = cipher.doFinal(missatge.getBytes());
        return Base64.getEncoder().encodeToString(missatgeXifrat);
    }

    public static String desxifrarMissatge(String missatgeXifrat, SecretKey clau) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, clau);
        byte[] missatgeDesxifratBytes = cipher.doFinal(Base64.getDecoder().decode(missatgeXifrat));
        return new String(missatgeDesxifratBytes);
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Introdueix el missatge a xifrar:");
            String missatge = scanner.nextLine();

            SecretKey clau = generarClauSecreta();

            String missatgeXifrat = xifrarMissatge(missatge, clau);
            System.out.println("Missatge xifrat: " + missatgeXifrat);

            String missatgeDesxifrat = desxifrarMissatge(missatgeXifrat, clau);
            System.out.println("Missatge desxifrat: " + missatgeDesxifrat);

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

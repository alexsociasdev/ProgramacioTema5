import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.Scanner;
import java.util.Arrays;

public class Activitat5_5 {

    public static SecretKey generarClau(String passphrase, String algoritme) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Transformem la contrasenya en un hash.
        byte[] hash = digest.digest(passphrase.getBytes());
        if (algoritme.equals("AES")) {
            return new SecretKeySpec(Arrays.copyOfRange(hash, 0, 32), "AES"); // Usant els primers 32 bytes per AES.
        } else if (algoritme.equals("DES")) {
            return new SecretKeySpec(Arrays.copyOfRange(hash, 0, 8), "DES"); // Usant els primers 8 bytes per DES.
        } else {
            throw new IllegalArgumentException("Algoritme no suportat.");
        }
    }

    public static void xifrarFitxer(File fitxer, File fitxerXifrat, SecretKey clau, String algoritme) throws Exception {
        Cipher cipher = Cipher.getInstance(algoritme);
        cipher.init(Cipher.ENCRYPT_MODE, clau);

        FileInputStream fis = new FileInputStream(fitxer);
        FileOutputStream fos = new FileOutputStream(fitxerXifrat);

        byte[] bytes = new byte[1024];
        int numBytes;
        while ((numBytes = fis.read(bytes)) != -1) {
            byte[] output = cipher.update(bytes, 0, numBytes);
            if (output != null) fos.write(output);
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) fos.write(outputBytes);

        fis.close();
        fos.close();
    }

    public static void desxifrarFitxer(File fitxerXifrat, File fitxerDesxifrat, SecretKey clau, String algoritme) throws Exception {
        Cipher cipher = Cipher.getInstance(algoritme);
        cipher.init(Cipher.DECRYPT_MODE, clau);

        FileInputStream fis = new FileInputStream(fitxerXifrat);
        FileOutputStream fos = new FileOutputStream(fitxerDesxifrat);

        byte[] bytes = new byte[1024];
        int numBytes;
        while ((numBytes = fis.read(bytes)) != -1) {
            byte[] output = cipher.update(bytes, 0, numBytes);
            if (output != null) fos.write(output);
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) fos.write(outputBytes);

        fis.close();
        fos.close();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Introdueix el camí del fitxer a xifrar:");
            String pathFitxer = scanner.nextLine();
            File fitxerOriginal = new File(pathFitxer);

            System.out.println("Escull l'algoritme de xifratge (AES o DES):");
            String algoritme = scanner.nextLine();

            System.out.println("Introdueix la teva clau:");
            String passphrase = scanner.nextLine();
            SecretKey clau = generarClau(passphrase, algoritme);

            File fitxerXifrat = new File(pathFitxer + ".enc");
            xifrarFitxer(fitxerOriginal, fitxerXifrat, clau, algoritme);
            System.out.println("El fitxer ha estat xifrat a: " + fitxerXifrat.getAbsolutePath());

            System.out.println("Vols desxifrar un fitxer? (s/n)");
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("s")) {
                System.out.println("Introdueix el camí del fitxer xifrat:");
                String pathFitxerXifrat = scanner.nextLine();

                System.out.println("Introdueix la mateixa clau per desxifrar:");
                String passphraseDesxifratge = scanner.nextLine();
                SecretKey clauDesxifratge = generarClau(passphraseDesxifratge, algoritme);

                File fitxerDesxifrat = new File(pathFitxerXifrat + ".dec");
                desxifrarFitxer(new File(pathFitxerXifrat), fitxerDesxifrat, clauDesxifratge, algoritme);

                System.out.println("El fitxer ha estat desxifrat a: " + fitxerDesxifrat.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}

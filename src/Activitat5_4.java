import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Scanner;

public class Activitat5_4 {

    private static SecretKey generarClau(String algoritme) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algoritme);
        keyGenerator.init(256); // Utilitza claus de 256 bits per AES; ajustar per a DES si és necessari.
        return keyGenerator.generateKey();
    }

    private static void guardarClau(SecretKey clau, String nomFitxer) throws Exception {
        byte[] clauBytes = clau.getEncoded();
        FileOutputStream fos = new FileOutputStream(nomFitxer);
        fos.write(clauBytes);
        fos.close();
    }

    private static SecretKey llegirClau(String nomFitxer, String algoritme) throws Exception {
        byte[] clauBytes = Files.readAllBytes(new File(nomFitxer).toPath());
        return new SecretKeySpec(clauBytes, algoritme);
    }

    private static void xifrarFitxer(File fitxer, File fitxerXifrat, SecretKey clau, String algoritme) throws Exception {
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

    private static void desxifrarFitxer(File fitxerXifrat, File fitxerDesxifrat, SecretKey clau, String algoritme) throws Exception {
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
            String filePath = scanner.nextLine();
            File originalFile = new File(filePath);

            System.out.println("Escull l'algoritme (AES o DES):");
            String algoritme = scanner.nextLine();

            SecretKey clau = generarClau(algoritme);
            File fitxerXifrat = new File(filePath + ".enc");
            File clauFile = new File(filePath + ".key");

            xifrarFitxer(originalFile, fitxerXifrat, clau, algoritme);
            guardarClau(clau, clauFile.getAbsolutePath());

            System.out.println("El fitxer ha estat xifrat. Fitxer xifrat: " + fitxerXifrat.getAbsolutePath());
            System.out.println("Clau guardada a: " + clauFile.getAbsolutePath());

            System.out.println("Vols desxifrar algun fitxer? (s/n)");
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("s")) {
                System.out.println("Introdueix el camí del fitxer xifrat a desxifrar:");
                String pathFitxerXifrat = scanner.nextLine();
                System.out.println("Introdueix el camí del fitxer de clau:");
                String pathClau = scanner.nextLine();

                File fitxerPerDesxifrar = new File(pathFitxerXifrat);
                SecretKey clauDesxifratge = llegirClau(pathClau, algoritme);
                File fitxerDesxifrat = new File(pathFitxerXifrat + ".dec");

                desxifrarFitxer(fitxerPerDesxifrar, fitxerDesxifrat, clauDesxifratge, algoritme);
                System.out.println("Fitxer desxifrat a: " + fitxerDesxifrat.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}

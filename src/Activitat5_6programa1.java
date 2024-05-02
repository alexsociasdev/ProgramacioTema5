import java.security.*;
import java.util.Base64;
import java.util.Scanner;

public class Activitat5_6programa1 {

    public static void main(String[] args) throws Exception {
        // Generar parell de claus RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Entrada de missatge per l'usuari
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introdueix el missatge a signar:");
        String missatge = scanner.nextLine();

        // Signatura del missatge
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(missatge.getBytes());
        byte[] digitalSignature = signature.sign();

        // Mostrar la signatura en Base64
        System.out.println("Signatura digital (Base64): " + Base64.getEncoder().encodeToString(digitalSignature));

        // Guardar la clau pública en format X.509
        byte[] publicKeyEncoded = publicKey.getEncoded();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKeyEncoded);
        System.out.println("Clau pública (Base64, format X.509): " + publicKeyString);

        scanner.close();
    }
}

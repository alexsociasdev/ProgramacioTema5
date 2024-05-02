import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class Activitat5_6programa2 {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Entrada de clau pública de l'usuari
        System.out.println("Introdueix la clau pública (Base64, format X.509):");
        String publicKeyString = scanner.nextLine();
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // Entrada de missatge de l'usuari
        System.out.println("Introdueix el missatge original:");
        String missatge = scanner.nextLine();

        // Entrada de la signatura digital de l'usuari
        System.out.println("Introdueix la signatura digital (Base64):");
        String digitalSignatureString = scanner.nextLine();
        byte[] digitalSignature = Base64.getDecoder().decode(digitalSignatureString);

        // Verificació de la signatura
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(missatge.getBytes());
        boolean isValid = signature.verify(digitalSignature);

        if (isValid) {
            System.out.println("La signatura és vàlida.");
        } else {
            System.out.println("La signatura no és vàlida.");
        }

        scanner.close();
    }
}

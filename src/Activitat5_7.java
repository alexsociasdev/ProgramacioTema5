import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;

public class Activitat5_7 {

    public static void main(String[] args) throws Exception {
        // Generació de claus RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Entrada del missatge de l'usuari
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Introdueix el missatge a xifrar:");
        String message = reader.readLine();

        // Xifrat simètric del missatge usant AES
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // Utilitza una clau de 256 bits
        SecretKey aesKey = keyGen.generateKey();
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedMessage = aesCipher.doFinal(message.getBytes());

        // Xifrat de la clau AES usant RSA
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());

        // Guarda el missatge xifrat i la clau AES xifrada en arxius
        try (FileOutputStream fosMessage = new FileOutputStream("encrypted_message.dat");
             FileOutputStream fosKey = new FileOutputStream("encrypted_aes_key.dat")) {
            fosMessage.write(encryptedMessage);
            fosKey.write(encryptedAesKey);
        }

        // Desxifrat de la clau AES usant la clau privada RSA
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedAesKeyBytes = rsaCipher.doFinal(encryptedAesKey);
        SecretKey decryptedAesKey = new SecretKeySpec(decryptedAesKeyBytes, "AES");

        // Desxifrat del missatge usant la clau AES recuperada
        aesCipher.init(Cipher.DECRYPT_MODE, decryptedAesKey);
        byte[] decryptedMessageBytes = aesCipher.doFinal(encryptedMessage);
        String decryptedMessage = new String(decryptedMessageBytes);

        System.out.println("Missatge original: " + message);
        System.out.println("Missatge desxifrat: " + decryptedMessage);
    }
}

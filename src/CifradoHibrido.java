import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;

public class CifradoHibrido {

    public static void main(String[] args) throws Exception {
        KeyPair parClaves = GenerarClaves();
        PublicKey clavePublica = parClaves.getPublic();
        PrivateKey clavePrivada = parClaves.getPrivate();

        SecretKey claveAes = generarClaveAes(128);

        Scanner src = new Scanner(System.in);
        System.out.println("Escribe un mensaje para ser cifrado:");
        String mensaje = src.nextLine();
        String mensajeCifrado = cifrarMensaje(mensaje, claveAes.getEncoded());

        BufferedWriter bw = new BufferedWriter(new FileWriter("MensajeCifrado.txt"));
        bw.write(mensajeCifrado);
        System.out.println("Mensaje cifrado" + mensajeCifrado);
        bw.close();

        String claveAESCifrada = cifrarClaveAES(clavePublica, claveAes);
        BufferedWriter bw2 = new BufferedWriter(new FileWriter("MensajeClaveAES.txt"));
        bw2.write(claveAESCifrada);
        System.out.println("Clave cifrada " + claveAESCifrada);
        bw2.close();

        // Descifrado
        String claveAESDescifrada = descifrarClaveAES(clavePrivada, claveAESCifrada);
        SecretKeySpec claveDescifrada = new SecretKeySpec(Base64.getDecoder().decode(claveAESDescifrada), "AES");

        String mensajeDescifrado = descifrarMensaje(mensajeCifrado, claveDescifrada.getEncoded());
        System.out.println("Mensaje descifrado: " + mensajeDescifrado);
    }

    public static KeyPair GenerarClaves() throws NoSuchAlgorithmException {
        KeyPairGenerator claves = KeyPairGenerator.getInstance("RSA");
        claves.initialize(2048);
        return claves.generateKeyPair();
    }
    public static SecretKey generarClaveAes(int tam) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(tam, new SecureRandom());
        return keyGen.generateKey();
    }

    public static String cifrarMensaje(String mensaje, byte[] clave) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(clave, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(mensaje.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String cifrarClaveAES(PublicKey clavePublica, SecretKey claveAES) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
        byte[] claveAESCifrada = cipher.doFinal(claveAES.getEncoded());
        return Base64.getEncoder().encodeToString(claveAESCifrada);
    }

    public static String descifrarClaveAES(PrivateKey clavePrivada, String claveAESCifrada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
        byte[] claveDescifrada = cipher.doFinal(Base64.getDecoder().decode(claveAESCifrada));
        return Base64.getEncoder().encodeToString(claveDescifrada);
    }

    public static String descifrarMensaje(String mensajeCifrado, byte[] clave) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(clave, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(mensajeCifrado));
        return new String(decryptedBytes);
    }
}

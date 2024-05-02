import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

public class FirmaDigital {

    public static PublicKey clavePublica;
    public static PrivateKey clavePrivada;

    public static KeyPair GenerarClave() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] FirmarMensaje(String mensaje, PrivateKey clave) throws Exception {
        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initSign(clave);
        firma.update(mensaje.getBytes());
        return firma.sign();
    }

    public static boolean VerificarFirma(String mensaje, byte[] firmaAVerificar, PublicKey clave) throws Exception {
        Signature firma = Signature.getInstance("SHA256withRSA");
        firma.initVerify(clave);
        firma.update(mensaje.getBytes());
        return firma.verify(firmaAVerificar);
    }

    public static void main(String[] args) throws Exception {
        // Creación de clave pública y privada
        KeyPair clave = GenerarClave();
        clavePublica = clave.getPublic();
        clavePrivada = clave.getPrivate();
        System.out.println("Clave pública: " + clavePublica);
        System.out.println("Clave privada: " + clavePrivada);

        // Mensaje a cifrar
        Scanner src = new Scanner(System.in);
        System.out.println("Introduce el mensaje a cifrar:");
        String mensaje = src.nextLine();

        // Firmado del mensaje
        byte[] mensajeFirmado = FirmarMensaje(mensaje, clavePrivada);
        System.out.println("Mensaje firmado: " + java.util.Base64.getEncoder().encodeToString(mensajeFirmado));

        // Verificación del mensaje firmado
        boolean verificado = VerificarFirma(mensaje, mensajeFirmado, clavePublica);
        if (verificado) {
            System.out.println("La firma ha sido verificada correctamente");
        } else {
            System.out.println("Error en la verificación de la firma");
        }

        src.close(); // Es importante cerrar el Scanner al final del uso para liberar recursos.
    }
}

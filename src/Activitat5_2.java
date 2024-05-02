import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Activitat5_2 {

    public static String calcularHash(String algoritme, String missatge) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algoritme);
        byte[] hashBytes = digest.digest(missatge.getBytes());
        return HexFormat.of().formatHex(hashBytes);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Si us plau, proporciona un algorisme de hash i un missatge com a arguments.");
            return;
        }
        String algoritme = args[0];
        String missatge = args[1];

        try {
            String resultatHash = calcularHash(algoritme, missatge);
            System.out.println(resultatHash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("L'algorisme de hash proporcionat no és vàlid o no està suportat.");
        }
    }
}

package app.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtil {

    private static final String ALGORITHM = "SHA-256";

    private HashUtil() {
    }

    public static String gerarHash(String senha) {
        if (senha == null) {
            throw new IllegalArgumentException("Senha não pode ser nula");
        }
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo de hash não disponível: " + ALGORITHM, e);
        }
    }

    public static boolean compararSenha(String senhaPura, String hashArmazenado) {
        if (senhaPura == null || hashArmazenado == null) {
            return false;
        }
        String hashCalculado = gerarHash(senhaPura);
        return hashCalculado.equals(hashArmazenado);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}

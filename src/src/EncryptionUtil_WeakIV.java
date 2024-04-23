import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;


public class EncryptionUtil {
    private static final String ENCRYPT_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * Encrypt a given plain-text string
     *
     * @param plainText
     *            The plain-text string that should be encrypted
     * @param aesConfig
     *            The aes key and iv map to encrypt the given string
     *
     * @return The base64 encoded representation of the encrypted string
     */
    public static String encrypt(String plainText, ConfigProperties.AesConfig aesConfig) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(aesConfig.getAesKey());
            // rebuild key using SecretKeySpec
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            byte[] decodedIv = Base64.getDecoder().decode(aesConfig.getAesIv());
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, originalKey, new IvParameterSpec(decodedIv));
            byte[] encryptedText = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedText);
        } catch (Exception e) {
            throw new RuntimeException("Failure while encrypting request body", e);
        }
    }
    /**
     * Decrypt a given base64 encoded string
     *
     * @param encryptedText
     *            The base64 encoded representation of the encrypted string that
     *            should be decrypted
     * @param aesConfig
     *            The aes key and iv map to decrypt the given string
     *
     * @return The plain-text string that was decrypted from the given input
     */
    public static String decrypt(String encryptedText, ConfigProperties.AesConfig aesConfig) {
        try {
            byte[] encryptedPayload = Base64.getDecoder().decode(encryptedText);
            byte[] decodedKey = Base64.getDecoder().decode(aesConfig.getAesKey());
            // rebuild key using SecretKeySpec
            byte[] decodedIv = Base64.getDecoder().decode(aesConfig.getAesIv());
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, originalKey, new IvParameterSpec(decodedIv));
            byte[] plainText = cipher.doFinal(encryptedPayload);
            return new String(plainText);
        } catch (Exception e) {
            throw new RuntimeException("Failure while decrypting request body",e);
        }
    }
    
    
    public static void main(String[] args) throws NoSuchAlgorithmException {
    	
    	Random rand = new SecureRandom();
//        byte[] bytes = new byte[16];
//        rand.nextBytes(bytes);
        byte[] bytes = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bytes);
        String newIV = Base64.getEncoder().encodeToString(ivParameterSpec.getIV());
        System.out.println("New IV: " + newIV );

        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);        
        SecretKey ivParamKey = kgen.generateKey();
        String ivParamKeyStr = Base64.getEncoder().encodeToString(ivParamKey.getEncoded());
        System.out.println("iv:" + ivParamKeyStr);        
       
        ConfigProperties.AesConfig aesConfig = new ConfigProperties.AesConfig();		
		aesConfig.setAesIv(newIV);
		aesConfig.setAesKey(ivParamKeyStr);
		
		String original = "http://www.google.com";
		System.out.println("original : " + original);
		String encrypted = encrypt(original, aesConfig);
		System.out.println("encrypted : " + encrypted);	
		String decrypted = decrypt(encrypted, aesConfig);
		System.out.println("decrypted : " + decrypted);
		
		String original2 = "https://www.youtube.com/";
		System.out.println("original2 : " + original2);
		String encrypted2 = encrypt(original2, aesConfig);
		System.out.println("encrypted2 : " + encrypted2);	
		String decrypted2 = decrypt(encrypted2, aesConfig);
		System.out.println("decrypted2 : " + decrypted2);
    }
}















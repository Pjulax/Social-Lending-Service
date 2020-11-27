package pl.fintech.metissociallending.metissociallendingservice.infrastructure.security;

import org.springframework.stereotype.Component;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

@Component
public class AESImpl implements AES {
    private  final String ALGO = "AES";
    public String encrypt(String data) {
        if(data==null)
            return "";
        Key key = generateKey();
        try {
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encVal);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
            System.out.println(e);
        }
        return data;
    }

    public String decrypt(String encryptedData)  {
        if(encryptedData==null)
            return "";
        Key key = generateKey();
        try {
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
            byte[] decValue = c.doFinal(decodedValue);
            return new String(decValue);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
            System.out.println(e);
        }
        return encryptedData;
    }

    private Key generateKey(){
        String secretKey = "z@bEzP13cZen1@-5@-5uPeR-HE_HE_HE";
        return new SecretKeySpec(secretKey.getBytes(), ALGO);
    }
}
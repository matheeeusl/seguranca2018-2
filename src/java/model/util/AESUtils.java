/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.util;

import com.sun.crypto.provider.AESKeyGenerator;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Matheus
 */
public class AESUtils {

    private Key aesKey;
    private byte iv[];
    private IvParameterSpec ivSpec;
    private Cipher cipher;

    public String instanceKey;
    public String instanceIv;
            
    public String generateAES() throws NoSuchAlgorithmException {
        
        // Gera uma chave AES
        KeyGenerator sKenGen = KeyGenerator.getInstance("AES");
        aesKey = sKenGen.generateKey();
        char[] hex = Hex.encodeHex(aesKey.getEncoded());
        //TODO
        String salvarNoBancoParaAESKey = String.valueOf(hex);
        return salvarNoBancoParaAESKey;
    }
    public String generateIv() throws NoSuchAlgorithmException, NoSuchProviderException{
        // Gerando o iv com SecureRandom
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        iv = new byte[16];
        random.nextBytes(iv);
        ivSpec = new IvParameterSpec(iv);

        char[] ivToChar = Hex.encodeHex(iv);
        //TODO
        String salvarNoBancoParaIvSpec = String.valueOf(ivToChar);
        
        return salvarNoBancoParaIvSpec;
    }

    public void inicia() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
        // Instancia o cipher
        cipher = Cipher.getInstance("AES/CTR/NoPadding");
        instanceKey = generateAES();
        instanceIv = generateIv();
    } // fim inicia

    public String encrypt(String strToEncrypt) {
        try {
            try {
                inicia();
            } catch (NoSuchProviderException ex) {
            }
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
            final String encryptedString = Hex.encodeHexString(cipher.doFinal(strToEncrypt.getBytes()));
            return encryptedString;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            System.err.println(e);
        }
        return null;
    }

    public String decrypt(String mensagemAlice, String aesKeyBob, String ivBob) throws InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            try {
                cipher = Cipher.getInstance("AES/CTR/NoPadding");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            byte[] aesToByte = Hex.decodeHex(aesKeyBob.toCharArray());
            SecretKeySpec keyBanco = new SecretKeySpec(aesToByte, "AES");

            byte[] ivToByte = Hex.decodeHex(ivBob.toCharArray());
            IvParameterSpec ivSpecBanco = new IvParameterSpec(ivToByte);
            cipher.init(Cipher.DECRYPT_MODE, keyBanco, ivSpecBanco);
            byte[] embytes = { };
            try {
                embytes = Hex.decodeHex(mensagemAlice.toCharArray());
            } catch (DecoderException ex) {
                Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            String decryptedString = new String(cipher.doFinal(embytes));

            return decryptedString;

        } catch (IllegalBlockSizeException | BadPaddingException | DecoderException e) {
            System.out.println("dentro "+e);
        }
        return null;
    }

    public static void main(String args[]) throws UnsupportedEncodingException, InvalidKeyException {

        AESUtils aes = new AESUtils();

        try {
            String b = aes.decrypt("matheus", "ee1c48d1d0e1d62ccd1f74e6f0ff10bb", "843c80cbcdc4882e4a2bbdd6d78f7e5f");
            System.out.println(b);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

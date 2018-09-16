/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


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
    
    
     public void inicia() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
        // Instancia o cipher
        cipher = Cipher.getInstance("AES/CTR/NoPadding");

        // Gera uma chave AES
        System.out.print("Gerando chave \t-> "); 
        KeyGenerator sKenGen = KeyGenerator.getInstance("AES"); 
        aesKey = sKenGen.generateKey();
        System.out.println("Chave AES \t= " + Hex.encodeHexString(aesKey.getEncoded()));
        
        char[] hex = Hex.encodeHex(aesKey.getEncoded());
        //TODO
        String salvarNoBancoParaAESKey = String.valueOf(hex);
        
        // Gerando o iv com SecureRandom
        System.out.print("Gerando IV \t-> "); 
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        iv = new byte[64];
        random.nextBytes(iv);
        ivSpec = new IvParameterSpec(iv);
        System.out.println("IV \t= " + Hex.encodeHexString(iv));
        
        char[] ivToChar = Hex.encodeHex(iv);
        //TODO
        String salvarNoBancoParaIvSpec = String.valueOf(ivToChar);
        
    } // fim inicia
    
    public String encrypt(String strToEncrypt) {
        try {
            inicia();
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
            final String encryptedString = Hex.encodeHexString(cipher.doFinal(strToEncrypt.getBytes()));
            return encryptedString;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | NoSuchProviderException | BadPaddingException e) {
        
        }
        return null;
    }
    
    public String decrypt(String mensagemAlice, String aesKeyBob, String ivBob) throws InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            byte[] aesToByte = Hex.decodeHex(aesKeyBob.toCharArray());
            SecretKeySpec keyBanco = new SecretKeySpec(aesToByte, "AES");
            
            byte[] ivToByte = Hex.decodeHex(ivBob.toCharArray());
            IvParameterSpec ivSpecBanco = new IvParameterSpec(ivToByte);
            
            cipher.init(Cipher.DECRYPT_MODE, keyBanco, ivSpecBanco);
            //cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] embytes = {};
            try {
                embytes = Hex.decodeHex(mensagemAlice.toCharArray());
            } catch (DecoderException ex) {
                Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            String decryptedString = new String(cipher.doFinal(embytes));

            return decryptedString;

        } catch (IllegalBlockSizeException | BadPaddingException | DecoderException e ) {
            System.out.println(e);
        }
        return null;
    }

    public static void main(String args[]) throws UnsupportedEncodingException {
        
        AESUtils aes = new AESUtils();
        
        
    }
}

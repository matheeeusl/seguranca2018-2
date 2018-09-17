/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.util;

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
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

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
        String salvarNoBancoParaIvSpec = String.valueOf(ivToChar);
        return salvarNoBancoParaIvSpec;
    }

    public void inicia() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
        cipher = Cipher.getInstance("AES/CTR/NoPadding");
        instanceKey = generateAES();
        instanceIv = generateIv();
    }

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
            inicia();
            
            byte[] aesToByte;
            aesToByte = Hex.decodeHex(aesKeyBob.toCharArray());
            SecretKeySpec keyBanco = new SecretKeySpec(aesToByte, "AES");

            byte[] ivToByte = Hex.decodeHex(ivBob.toCharArray());
            IvParameterSpec ivSpecBanco = new IvParameterSpec(ivToByte);
            cipher.init(Cipher.DECRYPT_MODE, keyBanco, ivSpecBanco);
            byte[] embytes = Hex.decodeHex(mensagemAlice.toCharArray());

            String decryptedString = new String(cipher.doFinal(embytes));
            return decryptedString;

        } catch (IllegalBlockSizeException | BadPaddingException | DecoderException | NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException e) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public static void main(String args[]) throws UnsupportedEncodingException, InvalidKeyException {

        AESUtils aes = new AESUtils();
        //int addProvider1 = Security.addProvider(new BouncyCastleProvider());
        try {
            String b = aes.decrypt("8f445cee6fd214", "ee1c48d1d0e1d62ccd1f74e6f0ff10bb", "843c80cbcdc4882e4a2bbdd6d78f7e5f");
            System.out.println(b);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.util;


import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import org.apache.commons.codec.binary.Hex;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;
/**
 *
 * @author Matheus
 */
public class PasswordUtils {
    
    /*Usado para gerar o salt  */
    public String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }
    public static String generateDerivedKey(String password, String salt) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 10000, 256);
        SecretKeyFactory pbkdf2;
        String derivedPass = null;
        try {
            pbkdf2 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            SecretKey sk = pbkdf2.generateSecret(spec);
            derivedPass = Hex.encodeHexString(sk.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return derivedPass;
    }
    public boolean verifyUserPassword(String providedPassword, String securedPassword, String salt) throws NoSuchAlgorithmException
    {
        boolean returnValue;
        
        // Generate New secure password with the same salt
        String newSecurePassword = generateDerivedKey(providedPassword, salt);
        
        // Check if two passwords are equal
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }
    
    public static void main(String args[]) throws NoSuchAlgorithmException {
        PasswordUtils PU = new PasswordUtils();
        String senha;
        String salt;
        String senhaBanco;
        Scanner input = new Scanner(System.in);
        System.out.println("Digite a senha: ");
        senha = input.nextLine();
        
        System.out.println("Digite a senha do banco: ");
        senhaBanco = input.nextLine();
        
        System.out.println("Digite o salt: ");
        salt = input.nextLine();
        
        boolean bol = PU.verifyUserPassword(senha, senhaBanco, salt);
        
        System.out.println(bol);
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


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
    private String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[64];
        sr.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }
    public static String generateDerivedKey(String password, String salt) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 10000, 256);
        SecretKeyFactory pbkdf2 = null;
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
        boolean returnValue = false;
        
        // Generate New secure password with the same salt
        String newSecurePassword = generateDerivedKey(providedPassword, salt);
        
        // Check if two passwords are equal
        System.out.println(newSecurePassword+"   "+ securedPassword +"     "+ salt);
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }
    
    public static void main(String args[]) throws NoSuchAlgorithmException {
        PasswordUtils PU = new PasswordUtils();
        String email;
        String senha;
        String salt;
        Scanner input = new Scanner(System.in);
        System.out.println("Digite o email: ");
        email = input.nextLine();
        System.out.println("Digite a senha: ");
        senha = input.nextLine();
        salt = PU.getSalt();
       
        System.out.println("Email original = " + email);
        System.out.println("Senha original = " + senha);
        System.out.println("Sal gerado = " + salt);
        
        String chaveDerivada = generateDerivedKey(senha, salt);
        System.out.println("Chave derivada da senha = " + chaveDerivada );
        
        String novoPassword;
        System.out.println("Digite a senha: ");
        novoPassword = input.nextLine();
        
        System.out.println(PU.verifyUserPassword(novoPassword, chaveDerivada, salt));
    }
}

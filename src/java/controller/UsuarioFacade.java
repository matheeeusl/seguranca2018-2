/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Usuario;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.util.AESUtils;
import model.util.PasswordUtils;

/**
 *
 * @author Matheus
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "Exercicio11PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    public Usuario autenticar(String login, String senha){
        System.out.println("AUTENTICAR"+login+" "+senha);
        PasswordUtils pu = new PasswordUtils();
        AESUtils au = new AESUtils();
        List<Usuario> usuarios = listaUsuarios();
        Usuario user = null;

        for (int i = 0; i < usuarios.size(); i++) {
            try {
                Usuario userObject = usuarios.get(i);
                String decryptedLogin = au.decrypt(userObject.getLogin(), userObject.getAesKey(), userObject.getIv());
                if(decryptedLogin.equals(login)){
                    if(pu.verifyUserPassword(senha, userObject.getSenha(), userObject.getSalt())){
                        user = userObject;
                    }
                }
            } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException ex) {
                Logger.getLogger(UsuarioFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       // Query query = em.createQuery("SELECT c FROM Clientes c WHERE c.senha = :senha AND c.login = :login");
       // query.setParameter("senha", senha);
       // query.setParameter("login", login);
       // List<Usuario> cliente = query.getResultList();
        if(usuarios.isEmpty()){
            return null;
        }
        return user;  
    }
    public List<Usuario> listaUsuarios(){
        
        Query query = em.createQuery("SELECT u FROM Usuario u");
        List<Usuario> lista = query.getResultList();
        
        return lista;
    }
    
}

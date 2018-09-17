/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Matheus
 */
@ManagedBean(name = "usuario_bean")
@SessionScoped
public class Login implements Serializable {
    
    private String login;

    private String senha;

    public Login(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }
    public Login() {
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return this.login;
    }

    public String getSenha() {
        return this.senha;
    }
    
    
}

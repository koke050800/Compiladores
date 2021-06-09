/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores;

/**
 *
 * @author KOKE
 */
public class IdentificadorVariable {

    private boolean declarado;
    private boolean isLocal;
    private String nombreIdentVar;
    private int tipoDato;
    
    //los tipos de datos
    public static int tipoCaracter = 0;
    public static int tipoString = 1;
    public static int tipoInteger = 2;
    public static int tipoDecimal = 3;
    public static int tipoDouble = 4;
    public static int tipoConstante = 5;

    public IdentificadorVariable(String nombreIdentVar, int tipoDato, boolean isLocal) {
        this.declarado = true;
        this.nombreIdentVar = nombreIdentVar;
        this.tipoDato = tipoDato;
        this.isLocal = isLocal;
    }

    public boolean isDeclarado() {
        return declarado;
    }

    public void setDeclarado(boolean declarado) {
        this.declarado = declarado;
    }

    public String getNombreIdentVar() {
        return nombreIdentVar;
    }

    public void setNombreIdentVar(String nombreIdentVar) {
        this.nombreIdentVar = nombreIdentVar;
    }

    public int getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(int tipoDato) {
        this.tipoDato = tipoDato;
    }

    public static int getTipoCaracter() {
        return tipoCaracter;
    }

    public static void setTipoCaracter(int tipoCaracter) {
        IdentificadorVariable.tipoCaracter = tipoCaracter;
    }

    public static int getTipoString() {
        return tipoString;
    }

    public static void setTipoString(int tipoString) {
        IdentificadorVariable.tipoString = tipoString;
    }

    public static int getTipoInteger() {
        return tipoInteger;
    }

    public static void setTipoInteger(int tipoInteger) {
        IdentificadorVariable.tipoInteger = tipoInteger;
    }

    public static int getTipoDecimal() {
        return tipoDecimal;
    }

    public static void setTipoDecimal(int tipoDecimal) {
        IdentificadorVariable.tipoDecimal = tipoDecimal;
    }

    public static int getTipoDouble() {
        return tipoDouble;
    }

    public static void setTipoDouble(int tipoDouble) {
        IdentificadorVariable.tipoDouble = tipoDouble;
    }

    public boolean isIsLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public static int getTipoConstante() {
        return tipoConstante;
    }

    public static void setTipoConstante(int tipoConstante) {
        IdentificadorVariable.tipoConstante = tipoConstante;
    }
    
    
    
    
    
}

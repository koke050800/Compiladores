/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author KOKE
 */
public class LeerArchivo {

    String direccion = "";
    int nLlamadas = 1;
    int contadorTOKENS = 0;
    public static ArrayList<String> simbolosValidos = new  ArrayList<>();
    public static ArrayList<String> tokens = new  ArrayList<>();
    public static ArrayList<String> tokensVarNum = new  ArrayList<>();
    
    public LeerArchivo() {
        simbolosValidos.add("#");
        simbolosValidos.add("%");
        simbolosValidos.add("process");
        simbolosValidos.add("execute");
        simbolosValidos.add("load");
        simbolosValidos.add("show");
        simbolosValidos.add(":=");
        simbolosValidos.add("{");
        simbolosValidos.add("}");
        simbolosValidos.add("ifThen");
        simbolosValidos.add("ifExecute");
        simbolosValidos.add("whileThen");
        simbolosValidos.add("whileExecute");
        simbolosValidos.add(";");
        simbolosValidos.add("integer");
        simbolosValidos.add("decimal");
        simbolosValidos.add("double");
        simbolosValidos.add("caracter");
        simbolosValidos.add("String");
        simbolosValidos.add("=");
        simbolosValidos.add("==");
        simbolosValidos.add("/=");
        simbolosValidos.add("<");
        simbolosValidos.add("<=");
        simbolosValidos.add(">");
        simbolosValidos.add(">=");
        simbolosValidos.add("+");
        simbolosValidos.add("-");
        simbolosValidos.add("*");
        simbolosValidos.add("/");
        simbolosValidos.add(".");
        simbolosValidos.add("(");
        simbolosValidos.add(")");
        simbolosValidos.add("and");
        simbolosValidos.add("AND");
        simbolosValidos.add("or");
        simbolosValidos.add("OR");
        simbolosValidos.add("Main");
        simbolosValidos.add("return");
        
    }
    
    
    public static boolean tokenIsValido(String tokenEvaluar){
        boolean valido = false;
        
        if (tokenEvaluar.charAt(0) == 34) {//34 es el numero de las comillas
            //si tiene comillas de inicio y cierre todo lo que tenga adentro es valido
            if (tokenEvaluar.charAt(tokenEvaluar.length() - 1) == 34) {//nos vamos a la ultima pos del token
                valido = true;

            } 
            return valido;
        }

        for (int i = 0; i < simbolosValidos.size(); i++) {

            if (tokenEvaluar.equals(simbolosValidos.get(i))) {
                valido = true;
                return valido;
            }
        }

        int contadorPuntitos = 0;
        //aqui vamos a recorrer la palabara para verificar que sea una letra o numero
        for (int i = 0; i < tokenEvaluar.length(); i++) {
            valido = true;
            if (tokenEvaluar.charAt(i) == '.') {
                contadorPuntitos++;
                if (contadorPuntitos > 1) {
                    valido = false;
                }
            }
            char letraEvaluando = tokenEvaluar.charAt(i);
            if (!(letraEvaluando == '_' || letraEvaluando == '.' || (((int) letraEvaluando >= 65) && ((int) letraEvaluando <= 122)) || (((int) letraEvaluando >= 48) && ((int) letraEvaluando <= 57)))) {
                valido = false;
            }

        }

        return valido;
    }

    public static String generarToken(LeerArchivo archivo) throws IOException {
        // ( _ )? ( [A-Z]|[a-z])+ ( _ |.)? ([0-9])*
        // [0-9]+ (.[0-9]+)?
        char caracterPerdido = 0;

        char letraEvaluando;
        String token = "";
        letraEvaluando = archivo.leerCaracter(); //vamos a leer el primer caracter para poder evaluarlo
        int contadorPuntos = 0;

        if (letraEvaluando == 34) { //34 es el numero de las comillas

            char letraSiguiente = 0;
            letraSiguiente = archivo.leerCaracter();
            String expresion = "";

            while (letraSiguiente != 34 && letraEvaluando != 65535) {
                
                //System.out.println("Evaluando --> "+letraEvaluando+" Siguiente--> "+(int)letraSiguiente);
                if(letraEvaluando == ')' && letraSiguiente == 13 ){//si estamos cerrando show
                archivo.setnLlamadas(archivo.getnLlamadas() - 2);//aqui apuntamos a la ultima letra de nuestro token
                return expresion;
                }

                expresion += (char) letraEvaluando;
                letraEvaluando = letraSiguiente;
                letraSiguiente = archivo.leerCaracter();
               /* if (letraEvaluando == 65535 || letraEvaluando == ')') {
                    archivo.setnLlamadas(archivo.getnLlamadas() - 2);//aqui apuntamos a la ultima letra de nuestro token
                    return expresion;
                }*/
            }
            if (letraSiguiente == 34) {
                expresion = expresion + letraEvaluando + (char) letraSiguiente;
                return expresion;
            }

        }

        if (letraEvaluando == ':' || letraEvaluando == '=' || letraEvaluando == '/' || letraEvaluando == '<' || letraEvaluando == '>') {

            char letraSiguiente = archivo.leerCaracter();
            if (letraSiguiente == '=') {
                String tokenCombinado = "" + letraEvaluando + letraSiguiente;
                return tokenCombinado;
            } else {//si no fue un simbolo doble, regresamos el apuntador de letra para que genere token normales
                archivo.setnLlamadas(archivo.getnLlamadas() - 1);//aqui apuntamos a la ultima letra de nuestro token
            }

        }

        while (letraEvaluando == '_' || letraEvaluando == '.' || (((int) letraEvaluando >= 65) && ((int) letraEvaluando <= 122)) || (((int) letraEvaluando >= 48) && ((int) letraEvaluando <= 57))) {

            if (letraEvaluando == '.') {
                contadorPuntos++;
                //System.out.println("Encontre: "+contadorPuntos+" puntos");

                if (contadorPuntos == 1) {
                    boolean siguienteEsNumero = true;

                    //System.out.println("Letra ev: " + letraEvaluando);
                    char letraSiguienteTemporal = archivo.leerCaracter();

                    //System.out.println("Letra sig: " + letraSiguienteTemporal);
                    if (!(((int) letraSiguienteTemporal >= 48) && ((int) letraSiguienteTemporal <= 57))) { //si no es un numero el siguiente
                        siguienteEsNumero = false;
                        //System.out.println("No hay numero despues del punto");
                        //System.out.println("TOKEN temporal: " + token);

                        //caracterPerdido = letraEvaluando;
                    }

                    archivo.setnLlamadas(archivo.getnLlamadas() - 1);//para que se quede donde estaba

                    if (siguienteEsNumero == false) {
                        break;
                    }

                } else if (contadorPuntos > 1) {

                    archivo.setnLlamadas(archivo.getnLlamadas() - 1);
                    // System.out.println();
                    // System.out.println("retorne desde contador de puntos > 1");
                    return token;

                }
            }

            token = token + letraEvaluando;            
            letraEvaluando = archivo.leerCaracter(); //adelantamos a la siguiente letra
            caracterPerdido = letraEvaluando;
        }

        if (caracterPerdido != 0) { //si el ultimo caracter, ya no esta en el while, entonces es caracterPerdido != 0

            archivo.setnLlamadas(archivo.getnLlamadas() - 1);//aqui apuntamos a la ultima letra de nuestro token
            //System.out.println();
            //System.out.println("retorne desde caracter perdido ");
            return token;
        }

        String tokenFinal = token + letraEvaluando;

        if (tokenFinal.charAt(0) == ' ' || letraEvaluando == '\n' || (int) tokenFinal.charAt(0) == 13 || (int) tokenFinal.charAt(0) == 9) { // (int) tokenFinal.charAt(0) == 13 es para retorno de carro y el 9 para tabuladores
            //System.out.println();
            //System.out.println("retorne desde recursivo");
            return generarToken(archivo);
        } else {
            //System.out.println();
            //System.out.println("retorne desde tokenFinal, con ASCII: "+ (int)tokenFinal.charAt(0));
            return tokenFinal;
        }

    }

    public char leerCaracter() throws IOException {
        int caracterLeido;
        char caracter = 0;

        try {

            FileReader archivo = new FileReader(direccion);
            for (int i = 0; i < nLlamadas; i++) {
                caracterLeido = archivo.read();
                caracter = (char) caracterLeido;
            }
            //System.out.println(caracter);
            nLlamadas++;

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex + ""
                    + "\nNo se ha encontrado el archivo",
                    "ADVERTENCIA!!!", JOptionPane.WARNING_MESSAGE);
        }

        return caracter;
    }

    public boolean abrirArchivo() {

        //Llamamos el metodo que permite cargar la ventana
        JFileChooser file = new JFileChooser();
        file.showOpenDialog(file);
        File abre = file.getSelectedFile();

        if (abre != null) {
            direccion = abre.getAbsolutePath();
            System.out.println("Esta leyendo archivo con direccion--> " + direccion);
            return true;
        } else {
            return false;
        }

    }

    public static void guardarTOKENS(LeerArchivo archivo1) throws IOException {
 
        boolean continuar = true;
        int i = 0;

        while (continuar) {
            String recibido = generarToken(archivo1);
            if (tokenIsValido(recibido)) {
                System.out.println("Recibio el token completo--> " + recibido);//+" ASCII: "+(int)recibido.charAt(0)

            } else {
                System.out.println("token invalido--> " + recibido);
            }

            tokens.add(recibido);
            if (65535 == recibido.charAt(0)) {
                tokens.remove(i);
                break;

            }
            i++;

        }
        

    }

    public static void varNum() {
        for (String s : tokens) {

            if ((int) s.charAt(0) >= 48 && (int) s.charAt(0) <= 57 || ( (int) s.charAt(0) == 46 && s.length()>= 2 && (int) s.charAt(1) >= 48 && (int) s.charAt(1) <= 57) ) {

                s = "NUMERO";

            } else {

                s = revisarSiEsSimbolo(s);
            }

            tokensVarNum.add(s);
        }

        System.out.println("*****************************");
        for (int i = 0; i < tokensVarNum.size(); i++) {
            if (tokenIsValido(tokens.get(i))) {
                System.out.println("VALIDO--> "+tokens.get(i) + " >>> " + tokensVarNum.get(i));
            } else {
                System.out.println("NO valido--> "+tokens.get(i) + " >>> " + tokensVarNum.get(i));
            }
        }
    }

    public static String revisarSiEsSimbolo(String tokenEvaluar) {
        boolean isSimbolo = false;
        
        for (int i = 0; i < simbolosValidos.size(); i++) {

            if (tokenEvaluar.equals(simbolosValidos.get(i))) {
               isSimbolo = true;
            } 
        }
        
        if(!isSimbolo && tokenIsValido(tokenEvaluar)){
            tokenEvaluar = "IDENT";
        }

        return tokenEvaluar;
    }
    
    
    // ArrayList<String> tokens  -----------------------------> Lista con TOKENS otiginal 
    // ArrayList<String> tokensVarNum = new  ArrayList<>(); --> Lista con tokens que dicen IDENT y NUMERO
    
    public void programa() { // <Programa>::= Main <Ecabezado> FinishMain 
//        
//        FIRST(Programa) = {“Main”}
        
        String tokenActual = "";
        tokenActual = tokensVarNum.get(contadorTOKENS);//token 0        
        contadorTOKENS++;

        if (!"Main".equals(tokenActual)) {
            funcionError(1, "Main");
        }

        encabezado();

        if (!"FinishMain".equals(tokenActual)) {
            funcionError(1, "FinishMain");
        }
    }

    public void encabezado() { // <AUXencabezado> <INSTRUCCION>
        
// FIRST(Encabezado) = FIRST(AUXencabezado) = { FIRST(AUXnumero) + FIRST(AUXvariable) + FIRST(AUXproceso) +  “ε “ } 
// = { “#” + “%”  + “process” + “ε “  } = { “#” + “%”  + “process”  +  FIRST(INSTRUCCION)  } 
// = { “#” + “%”  + “process”  +    “ifThen”+ “whileThen” + “execute” + “IDENT”}

        auxEncabezado();
        instruccion();

    }

    public void auxEncabezado() {
// <AUXencabezado> ::= <AUXnumero>
// <AUXencabezado> ::= <AUXvariable>  
// <AUXencabezado> ::= <AUXproceso>  
// <AUXencabezado> ::= ε

//FIRST(AUXencabezado) = { FIRST(AUXnumero) + FIRST(AUXvariable) + FIRST(AUXproceso) +  “ε“ } 
//FOLLOW(AUXencabezado) = { FIRST(INSTRUCCION) }  = 
//{ “ifThen”+ “whileThen” + “execute” + “IDENT” + FOLLOW(AUXnumero) + FOLLOW(AUXvariable)  } = 
//{ “ifThen”+ “whileThen” + “execute” + “IDENT”}
        
        //se supone que aqui voy a estar en el token 1, la primera vez
        auxNumero();
        auxVariable();
        auxProceso();  

    }

    public void auxNumero() {
// <AUXnumero> ::= # <IDENT> := <AUXnumID> ; <AUXencabezado>
// FIRST(AUXnumero) = {“#”}
        String tokenActual = "";
        tokenActual = tokensVarNum.get(contadorTOKENS);//token 1 la primera vez, ya pasamos el token 0       
        contadorTOKENS++;

        if ("#".equals(tokenActual)) {
            tokenActual = tokensVarNum.get(contadorTOKENS);
            contadorTOKENS++;
            if ("IDENT".equals(tokenActual)) {
                tokenActual = tokensVarNum.get(contadorTOKENS);
                contadorTOKENS++;
                if (":=".equals(tokenActual)) { //OJOOOOOO >>>>>> contador token estaria en lo que sigue de :=                    
                    auxNumID();

                    //al salir de la funcion, se supone que ya estamos apuntando al ; de nustra regla 
                    tokenActual = tokensVarNum.get(contadorTOKENS);
                    contadorTOKENS++; //se supone que aqui ya apuntamos al token que le sigue al ;
                    //entonces no es necesario hacerlo en el if

                    if (";".equals(tokenActual)) {
                        auxEncabezado();
                    } else {
                        funcionError(0, "#", tokenActual);
                    }
                } else {
                    funcionError(3, tokenActual);
                }
            } else {
                funcionError(2, tokenActual);
            }

        } else {
            funcionError(0, "#", tokenActual);
        }
    }

    public void auxVariable() {

    }

    public void auxProceso() {

    }

    public void auxNumID() {
//<AUXnumID> ::= <NUMERO>
//<AUXnumID> ::= <IDENT>
//FIRST(AUXnumID) = {“IDENT” + “NUMERO”}

        //Como aqui ya se esta apuntando al token, se rescata el token y despues se suma
        String tokenActual = tokensVarNum.get(contadorTOKENS);
        contadorTOKENS++;
        if ("IDENT".equals(tokenActual)) {
            tokenActual = tokensVarNum.get(contadorTOKENS);
            contadorTOKENS++; //apuntamos al token que sigue de IDENT

        } else if ("NUMERO".equals(tokenActual)) {
            tokenActual = tokensVarNum.get(contadorTOKENS);
            contadorTOKENS++; //apuntamos al token que sigue de NUMERO
        } else {

            funcionError(4, tokenActual);
        }
    }

    

    public void instruccion() {

    }
    
    
    
    //AUN NO SE SI ES NECESARIO EL CASE, o si con 3 parametros abarcamos todos los errores de sitaxis
    // en el metodo sobrecargado
    
    public void funcionError(int errorN, String esperado) {

        switch (errorN) {

            case 0:
                break;
            case 1:
                System.out.println("Error de sintaxis se esperaba--> " + esperado);
                break;
            case 2:
                System.out.println("Error de sintaxis se esperaba IDENT y envio " + esperado);
                break;

            case 3:
                System.out.println("Error de sintaxis se esperaba := y envio " + esperado);
                break;
            case 4:
                System.out.println("Error de sintaxis se esperaba IDENT o un NUMERO y envio " + esperado);
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;

            default:
                break;

        }

    }
    
    
    
    public void funcionError(int errorN, String esperado, String recibido) {

        switch (errorN) {

            case 0:
                System.out.println("Error de sintaxis se esperaba--> " + esperado + " y se recibio --> " + recibido);
                break;
            case 1:
                
                break;

            default:
                break;

        }

    }
    
    
    
    
    
    
    
    
    
    
    
    
    

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getnLlamadas() {
        return nLlamadas;
    }

    public void setnLlamadas(int nLlamadas) {
        this.nLlamadas = nLlamadas;
    }

    public static void main(String[] args) throws IOException {

        LeerArchivo archivo1 = new LeerArchivo();
        archivo1.abrirArchivo();
        guardarTOKENS(archivo1);
        varNum();
        archivo1.programa();
        

    }
    
    
    
    
    
    
    
    

}

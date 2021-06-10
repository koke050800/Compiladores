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
import java.util.Arrays;
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
    public static ArrayList<String> simbolosValidos = new ArrayList<>();
    public static ArrayList<String> tokens = new ArrayList<>();
    public static ArrayList<String> tokensVarNum = new ArrayList<>();
    int mensajeMainMostrado = 0;
    int errorSemantico = 0;
     int indexDeclaradas=-1;

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
        simbolosValidos.add("string");
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
        simbolosValidos.add("endIf");
        simbolosValidos.add("endWhile");
        simbolosValidos.add("FinishMain");
        simbolosValidos.add("elseThen");

    }

    public static boolean tokenIsValido(String tokenEvaluar) {
        boolean valido = false;

        if (tokenEvaluar.charAt(0) == 34) {//34 es el numero de las comillas
            //si tiene comillas de inicio y cierre todo lo que tenga adentro es valido
            if (tokenEvaluar.charAt(tokenEvaluar.length() - 1) == 34) {//nos vamos a la ultima pos del tokenIdent
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
                if (letraEvaluando == ')' && letraSiguiente == 13) {//si estamos cerrando show
                    archivo.setnLlamadas(archivo.getnLlamadas() - 2);//aqui apuntamos a la ultima letra de nuestro tokenIdent
                    return expresion;
                }

                expresion += (char) letraEvaluando;
                letraEvaluando = letraSiguiente;
                letraSiguiente = archivo.leerCaracter();
                /* if (letraEvaluando == 65535 || letraEvaluando == ')') {
                    archivo.setnLlamadas(archivo.getnLlamadas() - 2);//aqui apuntamos a la ultima letra de nuestro tokenIdent
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
            } else {//si no fue un simbolo doble, regresamos el apuntador de letra para que genere tokenIdent normales
                archivo.setnLlamadas(archivo.getnLlamadas() - 1);//aqui apuntamos a la ultima letra de nuestro tokenIdent
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
                        //System.out.println("TOKEN temporal: " + tokenIdent);

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

            archivo.setnLlamadas(archivo.getnLlamadas() - 1);//aqui apuntamos a la ultima letra de nuestro tokenIdent
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
                //System.out.println("Recibio el tokenIdent completo--> " + recibido);//+" ASCII: "+(int)recibido.charAt(0)

            } else {
                //System.out.println("tokenIdent invalido--> " + recibido);
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

            if ((int) s.charAt(0) >= 48 && (int) s.charAt(0) <= 57 || ((int) s.charAt(0) == 46 && s.length() >= 2 && (int) s.charAt(1) >= 48 && (int) s.charAt(1) <= 57)) {

                s = "NUMERO";

            } else {

                s = revisarSiEsSimbolo(s);
            }

            tokensVarNum.add(s);
        }

        System.out.println("*****************************");
        for (int i = 0; i < tokensVarNum.size(); i++) {
            if (tokenIsValido(tokens.get(i))) {
                System.out.println("VALIDO--> " + tokens.get(i) + " >>> " + tokensVarNum.get(i));
            } else {
                System.out.println("NO valido--> " + tokens.get(i) + " >>> " + tokensVarNum.get(i));
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

        if (!isSimbolo && tokenIsValido(tokenEvaluar)) {
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
        contadorTOKENS++;//aqui apuntamos ya al primer tokenIdent del encabezado

        if ("Main".equals(tokenActual)) { //aqui el contador de tokens, debe estar apuntando a lo que sigue de MAIN
            encabezado();//al salir de la funcion se supone que ya debemos apuntar a FinishMain con el contadorTOKENS, por lo tanto solo lo asignamos 
            tokenActual = tokensVarNum.get(contadorTOKENS);

            //POR SI PONEMOS UNA INSTRUCCION QUE NO SEA IF ELSE WHILE Y ESO AL FINAL DE LOS PROCESS O INSTRUCCIONES DE ESTE TIPO
            String tokenAnterior = tokensVarNum.get(contadorTOKENS - 1);
            if (";".equals(tokenActual) && ("IDENT".equals(tokenAnterior) || "NUMERO".equals(tokenAnterior))) {
                contadorTOKENS++;
                tokenActual = tokensVarNum.get(contadorTOKENS);
            }

            //aqui no es necesario apuntarlo a otro tokenIdent pq se supone que ya termina
            if ("FinishMain".equals(tokenActual)) {
                System.out.println("---------- PROGRAMA SIN ERRORES DE SINTAXIS ----------");
            } else {
                funcionError(1, "Se esperaba FinishMain", " y se recibio " + tokensVarNum.get(contadorTOKENS));
            }

        } else {
            funcionError(1, "Main");
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
        String tokenActual = "";
        tokenActual = tokensVarNum.get(contadorTOKENS);//ya estamos apuntando al primer tokenIdent de auxNumero, auxVariable, auxProceso, o al epsilon 

        if (!"ifThen".equals(tokenActual) && !"whileThen".equals(tokenActual)
                && !"execute".equals(tokenActual) && !"IDENT".equals(tokenActual)) {

            if ("#".equals(tokenActual)) {
                auxNumero();
            } else if ("%".equals(tokenActual)) {
                auxVariable();
            } else if ("process".equals(tokenActual)) {
                auxProceso();
            } else {
                funcionError(1, "se esperaba '#', '%' o 'process' y envio--> " + tokenActual);
            }

        }

    }

    public void auxNumero() {
// <AUXnumero> ::= # <IDENT> := <AUXnumID> ; <AUXencabezado>
// FIRST(AUXnumero) = {“#”}
        String tokenActual = "";
        tokenActual = tokensVarNum.get(contadorTOKENS);//token 1 la primera vez, ya pasamos el tokenIdent 0       
        contadorTOKENS++;

        if ("#".equals(tokenActual)) {
            tokenActual = tokensVarNum.get(contadorTOKENS);
            contadorTOKENS++;
            if ("IDENT".equals(tokenActual)) {
                tokenActual = tokensVarNum.get(contadorTOKENS);
                contadorTOKENS++;
                if (":=".equals(tokenActual)) { //OJOOOOOO >>>>>> contador tokenIdent estaria en lo que sigue de :=                    
                    auxNumID();

                    //al salir de la funcion, se supone que ya estamos apuntando al ; de nustra regla 
                    tokenActual = tokensVarNum.get(contadorTOKENS);
                    contadorTOKENS++; //se supone que aqui ya apuntamos al tokenIdent que le sigue al ;
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

//<AUXvariable>  ::= % <DATO> <IDENT> <AUX1> ; <AUXencabezado>
//FIRST(AUXvariable) = {“%”}
//<AUX1> ::= ε
//<AUX1>::=  = <AUXnumID>
//FIRST(AUX1) = { “=”+ “ε“}
//FOLLOW(AUX1) = {  “;” }
        String tokenActual = "";
        tokenActual = tokensVarNum.get(contadorTOKENS);//se supone que al entrar, el contadorTOKENS ya apunta %   
        contadorTOKENS++; //para apuntar al dato que esta despues del %

        if ("%".equals(tokenActual)) {
            dato(); //al salir de dato, estamos apuntando ya a <IDENT>
            tokenActual = tokensVarNum.get(contadorTOKENS);//aqui el tokenIdent actual es IDENT 
            contadorTOKENS++; //para apuntar al tokenIdent que esta despues de IDENT, en este caso AUX1

            if ("IDENT".equals(tokenActual)) {
                aux1();//se supone que al salir de aux1, ya estamos apuntando al ;
                tokenActual = tokensVarNum.get(contadorTOKENS);//aqui el tokenIdent actual es IDENT 
                contadorTOKENS++; //para apuntar al tokenIdent que esta despues de ;

                if (";".equals(tokenActual)) {
                    auxEncabezado();
                } else {
                    funcionError(1, ";", tokenActual);
                }

            } else {
                funcionError(1, "IDENT", tokenActual);
            }

        } else {
            funcionError(0, "%", tokenActual);
        }

    }

    public void auxProceso() {//OJOOO, puse FinishMain de follow, para que pueda acabar, despues de un proceso

//<AUXproceso> ::= process <IDENT> { <ENCABEZADO> } <AUXproceso>
//<AUXproceso>::= ε
//FIRST(AUXproceso) = { “process” + “ε “ } 
//FOLLOW(AUXproceso) = {FOLLOW(AUXproceso) + FOLLOW(AUXencabezado) } 
//                   = { “ifThen”+ “whileThen” + “execute” + “IDENT”}
        String tokenActual = "";
        tokenActual = tokensVarNum.get(contadorTOKENS);//se supone que al entrar, el contadorTOKENS ya apunta process o al epsilon

        //aqui no aumentamos el contador, solo si entra al if se aumenta,  para que en caso de que sea un follow, se quede en el tokenIdent correspondiente al follow
        if (!"ifThen".equals(tokenActual) && !"whileThen".equals(tokenActual) && !"execute".equals(tokenActual) && !"IDENT".equals(tokenActual) && !"FinishMain".equals(tokenActual)) {

            contadorTOKENS++; //para apuntar al dato que esta despues de process

            if ("process".equals(tokenActual)) {

                tokenActual = tokensVarNum.get(contadorTOKENS);//aqui el tokenIdent actual es IDENT 
                contadorTOKENS++; //para apuntar al tokenIdent que esta despues de IDENT, en este caso (

                if ("IDENT".equals(tokenActual)) {

                    tokenActual = tokensVarNum.get(contadorTOKENS);//aqui el tokenIdent actual es (
                    contadorTOKENS++; //para apuntar al tokenIdent que esta despues de (, en este caso <ENCABEZADO>

                    if ("{".equals(tokenActual)) {
                        encabezado();//se supone que al salir de encabezado, ya estamos apuntando al )
                        tokenActual = tokensVarNum.get(contadorTOKENS);//aqui el tokenIdent actual es )
                        contadorTOKENS++; //para apuntar al tokenIdent que esta despues de )

                        if ("}".equals(tokenActual)) {
                            auxProceso();
                        } else {
                            funcionError(0, "}", tokenActual);
                        }

                    } else {
                        funcionError(0, "{", tokenActual);
                    }

                } else {
                    funcionError(1, "IDENT", tokenActual);
                }

            } else {
                funcionError(0, "process", tokenActual);
            }
        }//si no entro al if, el tokenIdent actual es { “ifThen”+ “whileThen” + “execute” + “IDENT”}

    }

    public void auxNumID() {
//<AUXnumID> ::= <NUMERO>
//<AUXnumID> ::= <IDENT>
//FIRST(AUXnumID) = {“IDENT” + “NUMERO”}

        //Como aqui ya se esta apuntando al tokenIdent, se rescata el tokenIdent y despues se suma
        String tokenActual = tokensVarNum.get(contadorTOKENS);
        contadorTOKENS++; //apuntamos al tokenIdent que sigue de IDENT o de NUMERO, por ello no es necesario ponerlo en los if
        if ("IDENT".equals(tokenActual)) {

        } else if ("NUMERO".equals(tokenActual)) {

        } else {

            funcionError(4, tokenActual);
        }
    }

    public void dato() {
//<DATO>::= caracter
//<DATO>::= string
//<DATO>::= integer
//<DATO>::= decimal
//<DATO>::= double
//FIRST(DATO) = { “caracter” + “string” + “integer” + “decimal”+ “double”}

        //Como aqui ya se esta apuntando al tokenIdent, se rescata el tokenIdent y despues se suma
        String tokenActual = tokensVarNum.get(contadorTOKENS);
        contadorTOKENS++; //apuntamos al tokenIdent que sigue de <DATO> por lo que ya no es necesario en el if
        if ("caracter".equals(tokenActual) || "string".equals(tokenActual) || "integer".equals(tokenActual) || "decimal".equals(tokenActual) || "double".equals(tokenActual)) {

        } else {
            funcionError(1, "Tipo de dato invalido, se esperaba caracter, string, integer, decimal, double y se recibio--> ", tokenActual);
        }
    }

    public void aux1() {
//<AUX1> ::= ε
//<AUX1>::=  = <AUXnumID>
//FIRST(AUX1) = { “=”+ “ε“}
//FOLLOW(AUX1) = {  “;” }

        //Como aqui ya se esta apuntando al tokenIdent que sigue del epsilon o el de AUXnumID, se rescata el tokenIdent y se compara
        String tokenActual = tokensVarNum.get(contadorTOKENS);

        if (!";".equals(tokenActual)) {
            auxNumID();
        }

    }

    public void instruccion() {
//<INSTRUCCION>::= <AUXinstrucciones>
//FIRST(INSTRUCCION) = { FIRST(AUXinstrucciones) } =  { FIRST(AUXif) + FIRST(AUXwhile) + FIRST(AUXexecute) + FIRST(AUXnewIDENT) } 
//                                                  =  {“ifThen”+ “whileThen” + “execute” + “IDENT”}

        auxInstrucciones();

    }

    public void auxInstrucciones() {//OJOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO, puse un follow de ;, {, FinishMain, pq si no creo que se ciclaria
//<AUXinstrucciones>::= <AUXif> <AUXinstrucciones>
//<AUXinstrucciones>::= <AUXwhile> <AUXinstrucciones>
//<AUXinstrucciones>::= <AUXexecute> <AUXinstrucciones>
//<AUXinstrucciones>::= < AUXnewIDENT> <AUXinstrucciones>
//
//FIRST(AUXinstrucciones)  =  { FIRST(AUXif) + FIRST(AUXwhile) + FIRST(AUXexecute) + FIRST(AUXnewIDENT)    } 
//= {“ifThen”+ “whileThen” + “execute” + “IDENT”} 

        String tokenActual = "";
        tokenActual = tokensVarNum.get(contadorTOKENS);//ya estamos apuntando al primer tokenIdent de <AUXif>, <AUXwhile> , <AUXexecute>, < AUXnewIDENT> 
        if (!";".equals(tokenActual)
                && !"}".equals(tokenActual) && !"FinishMain".equals(tokenActual)//&& !"endIf".equals(tokenActual)&& !"endWhile".equals(tokenActual)
                ) {

            if ("ifThen".equals(tokenActual)) {
                auxIf();
                auxInstrucciones();
            } else if ("whileThen".equals(tokenActual)) {
                auxWhile();
                auxInstrucciones();

            } else if ("execute".equals(tokenActual)) {
                auxExecute();
                auxInstrucciones();

            } else if ("IDENT".equals(tokenActual)) {
                auxNewIDENT();
                auxInstrucciones();
            } else {
                funcionError(1, "se esperaba una instruccionde tipo 'ifThen', 'whileThen', 'execute' o un 'IDENT' y envio--> " + tokenActual);
            }
        }

    }

    public void auxExecute() {

// <AUXexecute> ::= execute <IDENT> ()
//FIRST(AUXexecute) = { “execute” }
        //Como aqui ya se supone que ya esta apuntando al tokenIdent execute
        String tokenActual = tokensVarNum.get(contadorTOKENS);
        contadorTOKENS++; //apuntamos al tokenIdent que sigue de execute

        if ("execute".equals(tokenActual)) {

            tokenActual = tokensVarNum.get(contadorTOKENS); //se supone que aqui recuperamos IDENT
            contadorTOKENS++; //apuntamos al tokenIdent que sigue de IDENT

            if ("IDENT".equals(tokenActual)) {

                tokenActual = tokensVarNum.get(contadorTOKENS); //se supone que aqui recuperamos (
                contadorTOKENS++; //apuntamos al tokenIdent que sigue de (

                if ("(".equals(tokenActual)) {
                    tokenActual = tokensVarNum.get(contadorTOKENS); //se supone que aqui recuperamos )
                    contadorTOKENS++; //apuntamos al tokenIdent que sigue de )
                    if (")".equals(tokenActual)) {

                    } else {
                        funcionError(0, ")", tokenActual);
                    }

                } else {
                    funcionError(0, "(", tokenActual);
                }

            } else {
                funcionError(1, "se esperaba un IDENT y envio ", tokenActual);
            }

        } else {
            funcionError(0, "execute", tokenActual);
        }

    }

    public void auxNewIDENT() {
//<AUXnewIDENT> ::= <IDENT> = <AUXins3>
//FIRST(AUXnewIDENT) = {“IDENT”} 
//
//<AUXins3>:::= <AUXnumID> <AUXinsOtro>
//FIRST(AUXins3) = FIRST(AUXnumID) = {“IDENT” + “NUMERO”}
//<AUXinsOtro>::= <ARITMETICOS> <AUXnumID> <AUXinsOrto>
//<AUXinsOtro>::= ε 
//FIRST(AUXinsOtro) = {FIRST(ARITMETICOS) + ε }  = { “+” + ”-” +  “*” + “/”  + ε }
//FOLLOW(AUXinsOtro) = { FOLLOW(AUXinsOtro) }={ }  

        //Como aqui ya se supone que ya esta apuntando al tokenIdent IDENT
        String tokenActual = tokensVarNum.get(contadorTOKENS);
        contadorTOKENS++; //apuntamos al tokenIdent que sigue de IDENT

        if ("IDENT".equals(tokenActual)) {

            tokenActual = tokensVarNum.get(contadorTOKENS); //se supone que aqui recuperamos =
            contadorTOKENS++; //apuntamos al tokenIdent que sigue de =

            if ("=".equals(tokenActual)) {
                auxIns3();

            } else {
                funcionError(0, "=", tokenActual);
            }

        } else {
            funcionError(1, "se esperaba un IDENT y envio ", tokenActual);
        }

    }

    public void auxIns3() {
//<AUXins3>:::= <AUXnumID> <AUXinsOtro>
//FIRST(AUXins3) = FIRST(AUXnumID) = {“IDENT” + “NUMERO”}
//<AUXinsOtro>::= <ARITMETICOS> <AUXnumID> <AUXinsOrto>
//<AUXinsOtro>::= ε 
//FIRST(AUXinsOtro) = {FIRST(ARITMETICOS) + ε }  = { “+” + ”-” +  “*” + “/”  + ε }
//FOLLOW(AUXinsOtro) = { FOLLOW(AUXinsOtro) }={ }  

        auxNumID();
        auxInsOtro();

    }

    public void auxInsOtro() {//OJOOO se agrego ; como follow

//<AUXinsOtro>::= <ARITMETICOS> <AUXnumID> <AUXinsOtro>
//<AUXinsOtro>::= ε 
//FIRST(AUXinsOtro) = {FIRST(ARITMETICOS) + ε }  = { “+” + ”-” +  “*” + “/”  + ε }
//FOLLOW(AUXinsOtro) = { FOLLOW(AUXinsOtro) }={   “ifThen”+ “whileThen” + “execute” + “IDENT”}  } 
        //Como aqui ya se supone que ya esta apuntando al primer tokeen <ARITMETICOS>
        String tokenActual = tokensVarNum.get(contadorTOKENS);

        if (!"ifThen".equals(tokenActual) && !"whileThen”".equals(tokenActual) && !"execute".equals(tokenActual) && !"IDENT".equals(tokenActual) && !";".equals(tokenActual)) { //OJOOO se agrego ; como follow            
            aritmeticos();
            auxNumID();
            auxInsOtro();
        }

    }

    public void auxIf() {

//<AUXif>::= ifThen  <CONDICION> ifExecute <AUXins1> <AUXifElse> endIf
//<AUXins1>::= <INSTRUCCION> ; <AUXins1> 
//<AUXins1>::= ε
//FIRST(AUXins1) = { FIRST(INSTRUCCIÓN) + “ε “ }
//FOLLOW(AUXins1) = {  FIRST(AUXifElse) + FOLLOW(AUXins1) + FOLLOW(AUXifElse) } = { “elseThen” +  “endIf“  } 
//
//<AUXifElse>::= ε
//<AUXifElse>::= elseThen <AUXins1>
//FIRST(AUXifElse) = { elseThen + “ε “ }
//FIRST(AUXif) = { “ifThen” }
//FOLLOW(AUXifElse) ={“endIF”}
        //Como aqui ya se supone que ya esta apuntando al tokenIdent ifThen
        String tokenActual = tokensVarNum.get(contadorTOKENS);
        contadorTOKENS++; //apuntamos al tokenIdent que sigue de ifThen

        if ("ifThen".equals(tokenActual)) {

            condicion();

            tokenActual = tokensVarNum.get(contadorTOKENS); //se supone que aqui recuperamos ifExecute
            contadorTOKENS++; //apuntamos al tokenIdent que sigue de ifExecute

            if ("ifExecute".equals(tokenActual)) {
                auxIns1();
                auxIfElse();
                tokenActual = tokensVarNum.get(contadorTOKENS); //se supone que aqui recuperamos endIF
                contadorTOKENS++; //apuntamos al tokenIdent que sigue de endIF

                if ("endIf".equals(tokenActual)) {
                    //System.out.println("----- Sali con Exito del endIf -----");
                } else {
                    funcionError(0, "endIf", tokenActual);
                }

            } else {
                funcionError(0, "ifExecute", tokenActual);
            }

        } else {
            funcionError(0, "ifThen", tokenActual);
        }

    }

    public void auxIfElse() {
//<AUXifElse>::= ε
//<AUXifElse>::= elseThen <AUXins1>
//FIRST(AUXifElse) = { elseThen + “ε “ }
//FIRST(AUXif) = { “ifThen” }
//FOLLOW(AUXifElse) ={“endIF”}

        //Como aqui ya se supone que ya esta apuntando al tokenIdent elseThen o epsilon
        String tokenActual = tokensVarNum.get(contadorTOKENS); //recuperamos

        if (!"endIf".equals(tokenActual)) {

            contadorTOKENS++; //apuntamos al tokenIdent que sigue de elseThen

            if ("elseThen".equals(tokenActual)) {
                auxIns1();

            } else {
                funcionError(0, "elseThen", tokenActual);
            }
        }

    }

    public void auxIns1() {//ojo, se puso endWhile como follow
//<AUXins1>::= <INSTRUCCION> ; <AUXins1> 
//<AUXins1>::= ε
//FIRST(AUXins1) = { FIRST(INSTRUCCIÓN) + “ε “ }
//FOLLOW(AUXins1) = {  FIRST(AUXifElse) + FOLLOW(AUXins1) + FOLLOW(AUXifElse) } = { “elseThen” +  “endIf“  } //en este follow tengo duda

        //Como aqui ya se supone que ya esta apuntando al primer  tokenIdent de <INSTRUCCION> o al epsilon
        String tokenActual = tokensVarNum.get(contadorTOKENS); //recuperamos
        if (!"elseThen".equals(tokenActual) && !"endIf".equals(tokenActual) && !"endWhile".equals(tokenActual)) {

            instruccion();

            //Como aqui ya se supone que ya esta apuntando al tokenIdent ;
            tokenActual = tokensVarNum.get(contadorTOKENS); //recuperamos
            contadorTOKENS++; //apuntamos al tokenIdent que sigue de ;

            if (";".equals(tokenActual)) {
                auxIns1();

            } else {
                funcionError(0, "; -- en auxIns1", tokenActual);
            }
        }// si no entra al if, se quedaria apuntando a elseThen o endIf
    }

    public void auxWhile() {
//<AUXwhile>::= whileThen  <CONDICION> whileExecute <AUXins2>  endWhile
//<AUXins2>::= <INSTRUCCION> ; <AUXins2> 
//<AUXins2>::= ε
//FIRST(AUXins2) = { FIRST(INSTRUCCIÓN) + “ε “ }
//FIRST(AUXwhile) = { “whileThen” }
//FOLLOW(AUXins2) = { “endWhile” + FOLLOW(AUXins2)  } 

        //Como aqui ya se supone que ya esta apuntando al tokenIdent whileThen
        String tokenActual = tokensVarNum.get(contadorTOKENS); //recuperamos
        contadorTOKENS++; //apuntamos al tokenIdent que sigue de whileThen

        if ("whileThen".equals(tokenActual)) {
            condicion();

            //Como aqui ya se supone que ya esta apuntando al tokenIdent whileExecute
            tokenActual = tokensVarNum.get(contadorTOKENS); //recuperamos
            contadorTOKENS++; //apuntamos al tokenIdent que sigue de whileExecute

            if ("whileExecute".equals(tokenActual)) {
                auxIns2();

                //Como aqui ya se supone que ya esta apuntando al tokenIdent endWhile
                tokenActual = tokensVarNum.get(contadorTOKENS); //recuperamos
                contadorTOKENS++; //apuntamos al tokenIdent que sigue de endWhile
                if ("endWhile".equals(tokenActual)) {
                    //System.out.println("---- SALI DEL ENDWHILE -----");
                } else {
                    funcionError(0, "endWhile", tokenActual);
                }

            } else {
                funcionError(0, "whileExecute", tokenActual);
            }

        } else {
            funcionError(0, "whileThen", tokenActual);
        }

    }

    public void auxIns2() {
//<AUXins2>::= <INSTRUCCION> ; <AUXins2> 
//<AUXins2>::= ε
//FIRST(AUXins2) = { FIRST(INSTRUCCIÓN) + “ε “ }
//FIRST(AUXwhile) = { “whileThen” }
//FOLLOW(AUXins2) = { “endWhile” + FOLLOW(AUXins2)  } 

        //Como aqui ya se supone que ya esta apuntando al primer tokenIdent <Instruccion> o epsilon
        String tokenActual = tokensVarNum.get(contadorTOKENS); //recuperamos

        if (!"endWhile".equals(tokenActual)) {

            instruccion();

            //Como aqui ya se supone que ya esta apuntando al tokenIdent ;
            tokenActual = tokensVarNum.get(contadorTOKENS); //recuperamos

            contadorTOKENS++; //apuntamos al tokenIdent que sigue de ;

            if (";".equals(tokenActual)) {
                auxIns2();

            } else {
                funcionError(0, ";", tokenActual);
            }
        }

    }

    public void condicion() {
//    <CONDICION>::= <AUXcon1>
//FIRST(CONDICION) = { FIRST(AUXcon1) } = { “(“ }
//<AUXcon1>::= ( <AUXnumID>  <RELACIONALES> <AUXnumID>  ) <AUXcond2>
//FIRST(AUXcon1) = {  “(“  }
//<AUXcon2>::= <BOOLEANOS> <AUXcon1>
//<AUXcon2>::= ε
//FIRST(AUXcon2) = { FIRST(BOOLEANOS) + “ε” } 
//FOLLOW(AUXcon2) = {FOLLOW(AUXcon1)} //ESTA NO SE SI SE DEJARIA ASI PQ AUXcon1 creo no tiene folllows
        auxCondicion1();

    }

    public void auxCondicion1() {
//<AUXcon1>::= ( <AUXnumID>  <RELACIONALES> <AUXnumID>  ) <AUXcond2>
//FIRST(AUXcon1) = {  “(“  }
//<AUXcon2>::= <BOOLEANOS> <AUXcon1>
//<AUXcon2>::= ε
//FIRST(AUXcon2) = { FIRST(BOOLEANOS) + “ε” } 
//FOLLOW(AUXcon2) = {FOLLOW(AUXcon1)} //ESTA NO SE SI SE DEJARIA ASI PQ AUXcon1 creo no tiene folllows

        //Como aqui ya se supone que ya esta apuntando al tokenIdent (
        String tokenActual = tokensVarNum.get(contadorTOKENS);
        contadorTOKENS++; //apuntamos al tokenIdent que sigue
        if ("(".equals(tokenActual)) {
            auxNumID();
            relacionales();//falta programar
            auxNumID();

            tokenActual = tokensVarNum.get(contadorTOKENS); //aqui se supone que apuntamos al )
            contadorTOKENS++; //apuntamos al tokenIdent que sigue

            if (")".equals(tokenActual)) {
                auxCondicion2();

            } else {
                funcionError(0, ")", tokenActual);
            }

        } else {
            funcionError(0, "(", tokenActual);
        }

    }

    private void auxCondicion2() {
//<AUXcon2>::= <BOOLEANOS> <AUXcon1>
//<AUXcon2>::= ε
//FIRST(AUXcon2) = { FIRST(BOOLEANOS) + “ε” } 
//FOLLOW(AUXcon2) = {FOLLOW(AUXcon1)} //ESTA NO SE SI SE DEJARIA ASI PQ AUXcon1 creo no tiene folllows

        String tokenActual = tokensVarNum.get(contadorTOKENS); //aqui se supone que apuntamos al booleano o epsilon
        if (!"ifExecute".equals(tokenActual) && !"whileExecute".equals(tokenActual)) {
            booleanos();
            auxCondicion1();
        }

    }

    public void aritmeticos() {
//        <ARITMETICOS>::= +
//<ARITMETICOS>::= -
//<ARITMETICOS>::= *
//<ARITMETICOS>::= /

        //Como aqui ya se esta apuntando al tokenIdent, se rescata el tokenIdent aritmetico
        String tokenActual = tokensVarNum.get(contadorTOKENS);
        contadorTOKENS++; //apuntamos al tokenIdent que sigue 
        if ("+".equals(tokenActual) || "-".equals(tokenActual) || "*".equals(tokenActual) || "/".equals(tokenActual)) {

        } else {
            funcionError(1, "Este no es un simbolo aritmetico valido--> ", tokenActual);
        }

    }

    public void relacionales() {
//        <RELACIONALES>::= ==
//<RELACIONALES>::= /=
//<RELACIONALES>::= <=
//<RELACIONALES>::= >=
//<RELACIONALES>::= <
//<RELACIONALES>::= >
//FIRST(RELACIONALES) = {“==” + “/=” + “<=” + “>=” + “>” + “<” } 

        //Como aqui ya se esta apuntando al tokenIdent, se rescata el tokenIdent relacional
        String tokenActual = tokensVarNum.get(contadorTOKENS);
        contadorTOKENS++; //apuntamos al tokenIdent que sigue 
        if ("==".equals(tokenActual) || "/=".equals(tokenActual) || "<=".equals(tokenActual) || ">=".equals(tokenActual) || "<".equals(tokenActual) || ">".equals(tokenActual)) {

        } else {
            funcionError(1, "Este no es relacional--> ", tokenActual);
        }

    }

    public void booleanos() {

//     <BOOLEANOS>::= or
//<BOOLEANOS>::= and
//<BOOLEANOS>::= OR
//<BOOLEANOS>::= AND
//FIRST(BOOLEANOS) = { “or”+”and”+ “OR”+“AND” }
        //Como aqui ya se esta apuntando al tokenIdent, se rescata el tokenIdent booleano
        String tokenActual = tokensVarNum.get(contadorTOKENS);
        contadorTOKENS++; //apuntamos al tokenIdent que sigue 
        if ("or".equals(tokenActual) || "OR".equals(tokenActual) || "and".equals(tokenActual) || "AND".equals(tokenActual)) {

        } else {
            funcionError(1, "Este no es un condicional booleano--> ", tokenActual);
        }

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
                System.out.println("Error: " + esperado + recibido);
                break;

            default:
                break;

        }

    }

    public void analizadorSemantico() {

        ArrayList<IdentificadorVariable> identsVariablesMain = new ArrayList<>();
        ArrayList<IdentificadorVariable> identsVariablesLocal = new ArrayList<>();
        System.out.println("\n\n****** Iniciando revision de declaraciones repettidas con bloque MAIN ******");
        separarVariablesLocales(identsVariablesMain, identsVariablesLocal);
        System.out.println("\n\n****** Comprobando que se usen variables hasta que se declaran ******");
        comprobarDeclaracion();
        System.out.println("\n\n****** Comprobando tipos de datos ******");
        comprobarTiposDato2();

        if (errorSemantico == 0) {
            System.out.println("---------- PROGRAMA SIN ERRORES SEMANTICOS ----------");
        } else {
            System.out.println("---------- SE ENCONTRARON ERRORES SEMANTICOS ----------");
        }

    }

    public void comprobarTiposDato2() {

        ArrayList<IdentificadorVariable> declaradas = new ArrayList<>();

        //primero capturamos todas las variables declaradas, aqui ya no nos importa si son locales o no, o si se repiten
        // pq en los dos metodos anteriores verfificamos eso
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (i + 1 < tokens.size()) {
                String valorConstante = null;
                if (token.equals("#")) {
                    valorConstante = tokens.get(i + 3);
                }

                capturaVariableDeclarada(token, declaradas, tokens.get(i + 1), valorConstante);
            }

        }

        for (int i = 0; i < tokensVarNum.size(); i++) {
            String tokenIdent = tokensVarNum.get(i);
            String token = tokens.get(i);

            if (tokenIdent.equals("IDENT") && !tokens.get(i - 1).equals("process")) {
                //System.out.println("\nEVALUANDO>> " + tokenIdent + " nombre: " + token);

                int contadorIDENTSproximos = 0;
                int indexAUX = i;
                while (!tokensVarNum.get(indexAUX).equals(";") && !tokensVarNum.get(indexAUX).equals(")")) {
                    indexAUX++;//para no contar el token actual

                    if ((tokensVarNum.get(indexAUX).equals("IDENT") && !tokensVarNum.get(indexAUX - 1).equals("process")) || tokensVarNum.get(indexAUX).equals("NUMERO")) {

                        contadorIDENTSproximos++;
                    }

                }
                //System.out.println("Hay >> " + contadorIDENTSproximos + " token(s) o numero(s) para evaluar con esta variable");

                if (contadorIDENTSproximos > 0) {
                    IdentificadorVariable variableEvaluando = null;//y guardarlo como una variable aqui
                    
                    //primero debemos de buscar el identificador en nuestra tabla de declarados
                    for (int d = 0; d < declaradas.size(); d++) {//buscamos ese ident en la lista de declarados   

                        if (token.equals(declaradas.get(d).getNombreIdentVar())) {
                            variableEvaluando = declaradas.get(d); //encontramos el ident para saber el tipo y todo eso
                            break;
                        }

                    }

                    //como sabemos la cantidad de tipos de datos que vamos a evaluar
                    //esa condicion ponemos en el while                  
                   // System.out.println("token antes del ciclo: " + tokens.get(i));
                    int indexAUX2 = i+1;//para que no se cuente a si mismo el token
                    int verificados = 0;
                    while (verificados < contadorIDENTSproximos) {
                        String tokenIdentAUX = tokensVarNum.get(indexAUX2);
                        String tokenAUX = tokens.get(indexAUX2);
                        IdentificadorVariable variableAUX = null;
                        if (tokenIdentAUX.equals("NUMERO")) {
                            //debemos saber si es un entero o un numero con punto
                            boolean tienePunto = false;
                            char[] ch = new char[tokenAUX.length()];
                            for (int v = 0; v < tokenAUX.length(); v++) {
                                ch[v] = tokenAUX.charAt(v);
                            }

                            for (char c : ch) {
                                if (c == '.') {
                                    tienePunto = true;
                                    break;
                                }
                            }

                            if (tienePunto) {
                                if (variableEvaluando.getTipoDato() == IdentificadorVariable.tipoInteger || 
                                        variableEvaluando.getTipoDato() == IdentificadorVariable.tipoCaracter ) {
                                    System.out.println("Error: La variable " + variableEvaluando.getNombreIdentVar()
                                            + " y el numero " + tokenAUX + " no son el mismo tipo de dato o no es compatible con el codigo ASCII.");
                                    errorSemantico++;
                                }

                            }

                            verificados++;
                            //System.out.println("NUMERO>> " + tokenAUX);
                        } else if (tokenIdentAUX.equals("IDENT")) {

                            //tenemos que buscar la variable que corresponde al identificador
                            //primero debemos de buscar el identificador en nuestra tabla de declarados
                            for (int d1 = 0; d1 < declaradas.size(); d1++) {//buscamos ese ident en la lista de declarados   

                                if (tokenAUX.equals(declaradas.get(d1).getNombreIdentVar())) {
                                    variableAUX = declaradas.get(d1); //encontramos el ident para saber el tipo y todo eso
                                    break;
                                }

                            }
                            if (variableEvaluando.getTipoDato() == variableAUX.getTipoDato()) {
                                //aqui no hacemos nada
                            }else if(variableEvaluando.getTipoDato() == IdentificadorVariable.tipoInteger && variableAUX.getTipoDato() == IdentificadorVariable.tipoConstanteSinPunto ){
                            //no hacemos nada
                            }else if(variableEvaluando.getTipoDato() == IdentificadorVariable.tipoDouble && variableAUX.getTipoDato() == IdentificadorVariable.tipoConstantePunto ){
                            //no hacemos nada
                            }else if(variableEvaluando.getTipoDato() == IdentificadorVariable.tipoDecimal && variableAUX.getTipoDato() == IdentificadorVariable.tipoConstantePunto ){
                            //no hacemos nada
                            }else if(variableEvaluando.getTipoDato() == IdentificadorVariable.tipoCaracter && variableAUX.getTipoDato() == IdentificadorVariable.tipoConstanteSinPunto ){
                            //no hacemos nada
                            }else if(variableEvaluando.getTipoDato() == IdentificadorVariable.tipoString && variableAUX.getTipoDato() == IdentificadorVariable.tipoConstanteSinPunto ){
                            //no hacemos nada
                            }else if(variableEvaluando.getTipoDato() == IdentificadorVariable.tipoString && variableAUX.getTipoDato() == IdentificadorVariable.tipoConstantePunto ){
                            //no hacemos nada                            
                            }else if(variableEvaluando.getTipoDato() == IdentificadorVariable.tipoConstantePunto && variableAUX.getTipoDato() == IdentificadorVariable.tipoDouble){
                            //no hacemos nada
                            }else if(variableEvaluando.getTipoDato() == IdentificadorVariable.tipoConstantePunto && variableAUX.getTipoDato() == IdentificadorVariable.tipoDecimal){
                            //no hacemos nada
                            }else if(variableEvaluando.getTipoDato() == IdentificadorVariable.tipoConstanteSinPunto && variableAUX.getTipoDato() == IdentificadorVariable.tipoInteger){
                            //no hacemos nada                            
                            }else{
                                System.out.println("Error: La variable " + variableEvaluando.getNombreIdentVar()+" "+variableEvaluando.getTipoDato()
                                                + " y la variable " + variableAUX.getNombreIdentVar() + " "+variableAUX.getTipoDato()+ " no son el mismo tipo de dato.");
                                errorSemantico++;
                            }
                            verificados++;
                            //System.out.println("IDENT>> " + tokenAUX);
                        }
                        indexAUX2++;

                    }

                    //System.out.println("Hay >> " + contadorIDENTSproximos + " token(s) o numero(s) para evaluar " + variableEvaluando.getNombreIdentVar() + "\n");
                }

            }

        }

    }

    public void comprobarTiposDato() {

        ArrayList<IdentificadorVariable> declaradas = new ArrayList<>();

        //primero capturamos todas las variables declaradas, aqui ya no nos importa si son locales o no, o si se repiten
        // pq en los dos metodos anteriores verfificamos eso
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (i + 1 < tokens.size()) {
                String valorConstante = null;
                if (token.equals("#")) {
                    valorConstante = tokens.get(i + 3);
                }

                capturaVariableDeclarada(token, declaradas, tokens.get(i + 1), valorConstante);
            }

        }

//        for(IdentificadorVariable aux: declaradas){
//            System.out.println("Nombre>> "+aux.getNombreIdentVar()+"  tipo>> "+aux.getTipoDato());
//        }

        IdentificadorVariable variableEvaluando = null;
        //Ahora si, comprobamos que plan con cada identificador
        for (int i = 0; i < tokensVarNum.size(); i++) {
            String tokenIdent = tokensVarNum.get(i);
            String token = tokens.get(i);

            if (tokenIdent.equals("IDENT") && !tokens.get(i-1).equals("process") ) {//si encontramos un IDENT y no es ident de process              
                System.out.println("IDENT>> "+tokenIdent + " nombre: "+token);
                
                for (int x = 0; x < declaradas.size(); x++) {//buscamos ese ident en la lista de declarados   

                    if (token.equals(declaradas.get(x).getNombreIdentVar())) {
                        variableEvaluando = declaradas.get(x);//encontramos el ident para saber el tipo y todo eso
                        break;
                    }

                }
               
                //aqui vemos que estamos 
               // System.out.println("\nEVALUANDOOOO>> "+variableEvaluando.getNombreIdentVar() +" tipo "+variableEvaluando.getTipoDato());
                
                
                
                //sacamos cuantos tokens hay hasta el punto y coma
                while (!tokenIdent.equals(";") && !tokenIdent.equals(")")) {
                    
                  
                    tokenIdent = tokensVarNum.get(i);
                    
                    if (tokenIdent.equals("IDENT") || tokenIdent.equals("NUMERO") ) {
                        
                        IdentificadorVariable variableTemporal;

                        //if (tokenIdent.equals("IDENT")) {
                            //buscamos ese ident con el que se esta haciendo la operacion en la lista de declarados   

                            for (int x = 0; x < declaradas.size(); x++) {

                                if (token.equals(declaradas.get(x).getNombreIdentVar())) {
                                    variableTemporal = declaradas.get(x);//encontramos el ident para saber el tipo y todo eso
                                    //System.out.println(variableTemporal.getNombreIdentVar()+" tipo>> "+variableTemporal.getTipoDato());
//                                    if (variableEvaluando.getTipoDato() == variableTemporal.getTipoDato()) {
//                                        //si son el mismo tipo de dato ya no hacemos nada 
//
//                                    } else { //puede darse el caso de que sea un tipo de dato con una constante
//                                        if (variableEvaluando.getTipoDato() == IdentificadorVariable.tipoConstantePunto) {
//                                            //si tiene punto decimal es compatible con todo
//                                            // menos con enteros
//                                            //aqui ya no hacemos nada
//                                        } else if (variableEvaluando.getTipoDato() == IdentificadorVariable.tipoConstanteSinPunto) { //si es una constante entera
//                                            if (variableTemporal.getTipoDato() == IdentificadorVariable.tipoConstanteSinPunto || variableTemporal.getTipoDato() == IdentificadorVariable.tipoInteger) {
//                                                //si se cumple, no hacemos nada
//                                            } else {
//                                                System.out.println("Error: La variable " + variableEvaluando.getNombreIdentVar() + " y la variable " + variableTemporal.getNombreIdentVar() + " no son el mismo tipo de dato");
//                                            }
//
//                                        }
//
//                                    }

                                }

                            }
                            

//                        } else {//si es un numero
//                            //debemos sacar si tiene punto o no
//                            String numero = tokens.get(i);
//                            char[] ch = new char[numero.length()];
//                            for (int k = 0; k < numero.length(); k++) {
//                                ch[k] = numero.charAt(k);
//                            }
//
//                            for (char c : ch) {
//                                if (c == '.') {
//                                    if(variableEvaluando.getTipoDato() == IdentificadorVariable.tipoConstanteSinPunto){
//                                        System.out.println("Error: La variable " + variableEvaluando.getNombreIdentVar() + " y el numero " + numero + " no son el mismo tipo de dato");
//                                    }
//                                }
//                            }
//
//                        }
                   }
                    
                    i++;
                    

                }
                //System.out.println("--------------------------------------------");

            }

        }

    }

    public void comprobarDeclaracion() {
        ArrayList<IdentificadorVariable> declaradas = new ArrayList<>();
        boolean isLocal = false;

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            String tokenIdent = tokensVarNum.get(i);

            if (token.equals("process")) {
                isLocal = true;
            }

            if (token.equals("}") && isLocal) {

                //aqui borramos las variables locales pq ya no las nesecitamos
                if (declaradas.size() > 0) {
                    for (int x = 0; x < declaradas.size(); x++) {
                        if (declaradas.get(x).isIsLocal()) {
                            declaradas.remove(x);
                        }

                    }
                }

                isLocal = false;

            }

            if (i + 1 < tokens.size()) {
                String valorConstante = null;
                if (token.equals("#")) {
                    valorConstante = tokens.get(i + 3);
                }

                capturaVariableDeclarada(token, declaradas, tokens.get(i + 1), isLocal, valorConstante);
            }

            if (tokenIdent.equals("IDENT") && !tokensVarNum.get(i - 1).equals("process")) {//si es un identificador
                

                //buscamos la variable
                if (declaradas.size() > 0) {
                    IdentificadorVariable variableActual;
                    boolean encontrada = false;

                    for (int j = 0; j < declaradas.size(); j++) {
                        variableActual = declaradas.get(j);

                        if (token.equals(variableActual.getNombreIdentVar())) { //si el tokenIdent es una variable
                            encontrada = true;
                            break;//si la encontramos salimos del for
                        }

                    }

                    if (!encontrada) {
                        System.out.println("Error: La variable " + token + " no ha sido declarada en este bloque");
                        errorSemantico++;
                    }

                }

            }

        }
    }

    

        public void capturaVariableDeclarada(String token, ArrayList<IdentificadorVariable> declaradas, String nombre,  String valorConstante) {

        if (token.equals("#") || token.equals("integer") || token.equals("decimal") || token.equals("double") || token.equals("caracter") || token.equals("string")) {

            int tipo = 10;
            switch (token) {
                case "#":
                    tipo = IdentificadorVariable.tipoConstanteSinPunto;

                    char[] ch = new char[valorConstante.length()];
                    for (int i = 0; i < valorConstante.length(); i++) {
                        ch[i] = valorConstante.charAt(i);
                    }

                    for (char c : ch) {
                        if (c == '.') {
                            tipo = IdentificadorVariable.tipoConstantePunto;
                            break;
                        }
                    }

                    break;

                case "integer":
                    tipo = IdentificadorVariable.tipoInteger;
                    break;

                case "decimal":
                    tipo = IdentificadorVariable.tipoDecimal;
                    break;

                case "double":
                    tipo = IdentificadorVariable.tipoDouble;
                    break;

                case "caracter":
                    tipo = IdentificadorVariable.tipoCaracter;
                    break;

                case "string":
                    tipo = IdentificadorVariable.tipoString;
                    break;

            }
            IdentificadorVariable nuevoIdentVar = new IdentificadorVariable(nombre, tipo, false);
            declaradas.add(nuevoIdentVar);
            indexDeclaradas++;

        }
    }

    public void capturaVariableDeclarada(String token, ArrayList<IdentificadorVariable> declaradas, String nombre, boolean isLocal, String valorConstante) {

        if (token.equals("#") || token.equals("integer") || token.equals("decimal") || token.equals("double") || token.equals("caracter") || token.equals("string")) {

            int tipo = 10;
            switch (token) {
                case "#":
                    tipo = IdentificadorVariable.tipoConstanteSinPunto;

                    char[] ch = new char[valorConstante.length()];
                    for (int i = 0; i < valorConstante.length(); i++) {
                        ch[i] = valorConstante.charAt(i);
                    }

                    for (char c : ch) {
                        if (c == '.') {
                            tipo = IdentificadorVariable.tipoConstantePunto;
                            break;
                        }
                    }

                    break;

                case "integer":
                    tipo = IdentificadorVariable.tipoInteger;
                    break;

                case "decimal":
                    tipo = IdentificadorVariable.tipoDecimal;
                    break;

                case "double":
                    tipo = IdentificadorVariable.tipoDouble;
                    break;

                case "caracter":
                    tipo = IdentificadorVariable.tipoCaracter;
                    break;

                case "string":
                    tipo = IdentificadorVariable.tipoString;
                    break;

            }
            IdentificadorVariable nuevoIdentVar = new IdentificadorVariable(nombre, tipo, isLocal);
            declaradas.add(nuevoIdentVar);
            indexDeclaradas++;

        }
    }

    private void separarVariablesLocales(ArrayList<IdentificadorVariable> identsVariablesMain, ArrayList<IdentificadorVariable> identsVariablesLocal) {

//        for (String aux : tokens) {
//
//            System.out.println(aux);
//        }
        boolean isUnProcess = false;
        for (int i = 0; i < tokens.size(); i++) {
            String tokensito = tokens.get(i);

            if (tokensito.equals("process")) {

                //se supone que las variables del Main ya estan declaradas
                if (identsVariablesMain.size() > 0) {
                    ArrayList<IdentificadorVariable> identsVariablesAUXMain = (ArrayList<IdentificadorVariable>) identsVariablesMain.clone();
                    revisarDeclaracionesRepetidasBloque(identsVariablesAUXMain, 1);//revisamos que en el Main no se repitan las variables
                }

                String nombreProcess;
                nombreProcess = tokens.get(i + 1);
                System.out.println("\n****** Revision de declaraciones repettidas process " + nombreProcess + "******");
                isUnProcess = true;
                //cada proces ponemos la lista nueva
                identsVariablesLocal.removeAll(identsVariablesLocal);
                identsVariablesLocal.clear();
            }

            if (isUnProcess && tokensito.equals("}")) {
                isUnProcess = false;
                if (identsVariablesLocal.size() > 0) {

//                    for (IdentificadorVariable aux : identsVariablesLocal) {
//                        System.out.println(aux.getNombreIdentVar() + " \\ " + aux.getTipoDato() + " \\ " + aux.isDeclarado() + " \\ " + aux.isIsLocal());
//                    }
                    revisarDeclaracionesRepetidasBloque(identsVariablesLocal, 0);
                    revisarDeclaracionesRepetidasMainBloque(identsVariablesLocal, identsVariablesMain);
                }
            }

            if (!isUnProcess && (tokensito.equals("#") || tokensito.equals("integer") || tokensito.equals("decimal") || tokensito.equals("double") || tokensito.equals("caracter") || tokensito.equals("string"))) {
                String nombreVarIdent = tokens.get(i + 1);
                int tipo = 10;
                switch (tokensito) {
                    case "#":
                        tipo = IdentificadorVariable.tipoConstanteSinPunto;
                        break;

                    case "integer":
                        tipo = IdentificadorVariable.tipoInteger;
                        break;

                    case "decimal":
                        tipo = IdentificadorVariable.tipoDecimal;
                        break;

                    case "double":
                        tipo = IdentificadorVariable.tipoDouble;
                        break;

                    case "caracter":
                        tipo = IdentificadorVariable.tipoCaracter;
                        break;

                    case "string":
                        tipo = IdentificadorVariable.tipoString;
                        break;

                }
                IdentificadorVariable nuevoIdentVar = new IdentificadorVariable(nombreVarIdent, tipo, false);
                identsVariablesMain.add(nuevoIdentVar);

            } else if (isUnProcess && (tokensito.equals("#") || tokensito.equals("integer") || tokensito.equals("decimal") || tokensito.equals("double") || tokensito.equals("caracter") || tokensito.equals("string"))) {
                String nombreVarIdent = tokens.get(i + 1);
                int tipo = 10;
                switch (tokensito) {
                    case "#":
                        tipo = IdentificadorVariable.tipoConstanteSinPunto;
                        break;

                    case "integer":
                        tipo = IdentificadorVariable.tipoInteger;
                        break;

                    case "decimal":
                        tipo = IdentificadorVariable.tipoDecimal;
                        break;

                    case "double":
                        tipo = IdentificadorVariable.tipoDouble;
                        break;

                    case "caracter":
                        tipo = IdentificadorVariable.tipoCaracter;
                        break;

                    case "string":
                        tipo = IdentificadorVariable.tipoString;
                        break;

                }

                IdentificadorVariable nuevoIdentVar = new IdentificadorVariable(nombreVarIdent, tipo, true);
                identsVariablesLocal.add(nuevoIdentVar);
            }
        }

    }

    public void revisarDeclaracionesRepetidasMainBloque(ArrayList<IdentificadorVariable> bloqueLocal, ArrayList<IdentificadorVariable> bloqueMain) {
//        System.out.println("\nLocales");
//        for (IdentificadorVariable aux : bloqueLocal) {
//
//            System.out.println(aux.getNombreIdentVar());
//        }
//        System.out.println("\nMain");
//        for (IdentificadorVariable aux : bloqueMain) {
//
//            System.out.println(aux.getNombreIdentVar());
//        }

        for (int i = 0; i < bloqueLocal.size(); i++) {
            String evaluando = bloqueLocal.get(i).getNombreIdentVar();

            for (int j = 0; j < bloqueMain.size(); j++) {

                if (evaluando.equals(bloqueMain.get(j).getNombreIdentVar())) {
                    System.out.println("ERROR: Usted tiene la variable \"" + evaluando + "\" ya declarada en el bloque Main");
                    System.out.println("Recuerde que solo puede declarar la variable o constante una vez en el bloque");
                    errorSemantico++;
                }

            }

        }

    }

    public void revisarDeclaracionesRepetidasBloque(ArrayList<IdentificadorVariable> identsVariablesRecibido, int mainSelected) {
        ArrayList<IdentificadorVariable> identsVariables = (ArrayList<IdentificadorVariable>) identsVariablesRecibido.clone();
        int contador = 0;

        for (int i = 0; i < identsVariables.size(); i++) {
            String evaluando = identsVariables.get(i).getNombreIdentVar();

            for (int j = 0; j < identsVariables.size(); j++) {
                IdentificadorVariable aux = identsVariables.get(j);
                if (evaluando.equals(aux.getNombreIdentVar())) {
                    contador++;
                    if (contador > 1) {
                        identsVariables.remove(j);
                    }
                }
            }

            if (contador > 1 && mainSelected == 0) {
                System.out.println("ERROR: Usted tiene la variable \"" + evaluando + "\" declarada " + contador + " veces");
                System.out.println("Recuerde que solo puede declarar la variable o constante una vez en el bloque");
                errorSemantico++;

            } else if (contador > 1 && mainSelected == 1) {
                if (mensajeMainMostrado < 1) {
                    System.out.println("\nERROR: Usted tiene la variable \"" + evaluando + "\" declarada " + contador + " veces");
                    System.out.println("Recuerde que solo puede declarar la variable o constante una vez en el bloque MAIN");
                    errorSemantico++;
                }
                mensajeMainMostrado++;
            }
            contador = 0;//para que la siguiente palabra el contador este en 0
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
        archivo1.analizadorSemantico();

    }

}

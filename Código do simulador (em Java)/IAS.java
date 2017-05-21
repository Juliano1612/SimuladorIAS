package ias;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Juliano e Kevin
 */
public class IAS {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    static String vlr, IR, IBR, auxJMP;
    static int m, i, MAR, PC, auxm, verMem;
    static Memoria[] memoria = new Memoria[1024];
    static float AC, MQ;
    static Instrucao instrEsq = new Instrucao();
    static Instrucao instrDir = new Instrucao();
    static Registrador MBR = new Registrador(instrEsq, instrDir);
    static boolean jumpReturn, execDir = false, execEsq = true;
    static String endereco;

    public static void main(String[] args) throws IOException {
        Scanner scanf = new Scanner(System.in);
        System.out.println("==============SIMULADOR IAS==============\nDesenvolvido por : Juliano C. e Kevin L.\n");
        System.out.println("Digite o endereco do arquivo com o código: ");
        endereco = scanf.nextLine();

        iniciaArrayMemoria();
        carregaMemoria();
        imprimeMemoria();

        auxJMP = "";
        jumpReturn = true;
        m = 0;
        execDir = false;
        execEsq = true;
        String auxEsq = null;
        imprimeRegistradores();
        char[] arrayMBREsq = null, arrayMBRDir = null;

        while (m >= 0 && m < 1024) {
            PC = m + 1;

            if (execEsq) {
                carregaMBR();   //carregar MBR
                arrayMBREsq = MBR.getInstrEsq().getOpcode().toCharArray();
                auxEsq = MBR.getInstrEsq().getOpcode();
                arrayMBRDir = MBR.getInstrDir().getOpcode().toCharArray();
            }
            auxm = m;
            do {
                System.out.println("Gostaria de ver a memória?\n[1] - Sim\t[0] - Não ");
                verMem = scanf.nextInt();
            } while (verMem != 0 && verMem != 1);
            if (verMem == 1) {
                imprimeMemoria();
            }
            scanf.nextLine();
            m = auxm;

            if (arrayMBREsq.length >= 4 && auxEsq.charAt(0) == 'L' && auxEsq.charAt(1) == 'O' && auxEsq.charAt(2) == 'A' && auxEsq.charAt(3) == 'D' && execEsq) {
                loadEsq();
                imprimeRegistradores();
                execDir = true;
                execEsq = false;
                arrayMBREsq = "teste".toCharArray();

            }
            if (arrayMBRDir.length >= 4 && arrayMBRDir[0] == 'L' && arrayMBRDir[1] == 'O' && arrayMBRDir[2] == 'A' && arrayMBRDir[3] == 'D' && execDir) {
                loadDir();
                m++;
                imprimeRegistradores();
                carregaMBR();
                execDir = false;
                execEsq = true;
                limpaMBR();
                arrayMBRDir = "teste".toCharArray();

            }
            if (arrayMBREsq.length >= 4 && arrayMBREsq[0] == 'S' && arrayMBREsq[1] == 'T' && arrayMBREsq[2] == 'O' && arrayMBREsq[3] == 'R' && execEsq) {
                storEsq();
                imprimeRegistradores();
                execDir = true;
                execEsq = false;
                System.out.println("Memoria[" + MAR + "].Valor = " + memoria[MAR].getDado());
                arrayMBREsq = "teste".toCharArray();

            }
            if (arrayMBRDir.length >= 4 && arrayMBRDir[0] == 'S' && arrayMBRDir[1] == 'T' && arrayMBRDir[2] == 'O' && arrayMBRDir[3] == 'R' && execDir) {
                storDir();
                imprimeRegistradores();
                System.out.println("Memoria[" + MAR + "].Valor = " + memoria[MAR].getDado());
                execDir = false;
                execEsq = true;
                limpaMBR();
                m++;
                arrayMBRDir = "teste".toCharArray();
            }
            if (arrayMBREsq.length >= 3 && arrayMBREsq[0] == 'A' && arrayMBREsq[1] == 'D' && arrayMBREsq[2] == 'D' && execEsq) {
                addEsq();
                imprimeRegistradores();
                execDir = true;
                execEsq = false;
                arrayMBREsq = "teste".toCharArray();

            }
            if (arrayMBRDir.length >= 3 && arrayMBRDir[0] == 'A' && arrayMBRDir[1] == 'D' && arrayMBRDir[2] == 'D' && execDir) {
                addDir();
                imprimeRegistradores();
                execDir = false;
                execEsq = true;
                m++;
                limpaMBR();
                arrayMBRDir = "teste".toCharArray();

            }
            if (arrayMBREsq.length >= 3 && arrayMBREsq[0] == 'S' && arrayMBREsq[1] == 'U' && arrayMBREsq[2] == 'B' && execEsq) {
                subEsq();
                imprimeRegistradores();
                execDir = true;
                execEsq = false;
                arrayMBREsq = "teste".toCharArray();

            }
            if (arrayMBRDir.length >= 3 && arrayMBRDir[0] == 'S' && arrayMBRDir[1] == 'U' && arrayMBRDir[2] == 'B' && execDir) {
                subDir();
                imprimeRegistradores();
                execDir = false;
                execEsq = true;
                m++;
                limpaMBR();
                arrayMBRDir = "teste".toCharArray();

            }
            if (arrayMBREsq.length >= 3 && arrayMBREsq[0] == 'M' && arrayMBREsq[1] == 'U' && arrayMBREsq[2] == 'L' && execEsq) {
                mulEsq();
                imprimeRegistradores();
                execDir = true;
                execEsq = false;
                arrayMBREsq = "teste".toCharArray();

            }
            if (arrayMBRDir.length >= 3 && arrayMBRDir[0] == 'M' && arrayMBRDir[1] == 'U' && arrayMBRDir[2] == 'L' && execDir) {
                mulDir();
                imprimeRegistradores();
                execDir = false;
                execEsq = true;
                m++;
                limpaMBR();
                arrayMBRDir = "teste".toCharArray();

            }
            if (arrayMBREsq.length >= 3 && arrayMBREsq[0] == 'D' && arrayMBREsq[1] == 'I' && arrayMBREsq[2] == 'V' && execEsq) {
                divEsq();
                imprimeRegistradores();
                execDir = true;
                execEsq = false;
                arrayMBREsq = "teste".toCharArray();

            }
            if (arrayMBRDir.length >= 3 && arrayMBRDir[0] == 'D' && arrayMBRDir[1] == 'I' && arrayMBRDir[2] == 'V' && execDir) {
                divDir();
                imprimeRegistradores();
                execDir = false;
                execEsq = true;
                m++;
                limpaMBR();
                arrayMBRDir = "teste".toCharArray();

            }
            if (arrayMBREsq.length >= 4 && arrayMBREsq[0] == 'L' && arrayMBREsq[1] == 'S' && arrayMBREsq[2] == 'H' && execEsq) {
                AC = AC * 2;
                imprimeRegistradores();
                execDir = true;
                execEsq = false;
                arrayMBREsq = "teste".toCharArray();

            }
            if (arrayMBRDir.length >= 4 && arrayMBRDir[0] == 'L' && arrayMBRDir[1] == 'S' && arrayMBRDir[2] == 'H' && execDir) {
                AC = AC * 2;
                imprimeRegistradores();
                execDir = false;
                execEsq = true;
                m++;
                limpaMBR();
                arrayMBRDir = "teste".toCharArray();

            }
            if (arrayMBREsq.length >= 3 && arrayMBREsq[0] == 'R' && arrayMBREsq[1] == 'S' && arrayMBREsq[2] == 'H' && execEsq) {
                AC = AC / 2;
                imprimeRegistradores();
                execDir = true;
                execEsq = false;
                arrayMBREsq = "teste".toCharArray();

            }
            if (arrayMBRDir.length >= 3 && arrayMBRDir[0] == 'R' && arrayMBRDir[1] == 'S' && arrayMBRDir[2] == 'H' && execDir) {
                AC = AC / 2;
                imprimeRegistradores();
                execDir = false;
                execEsq = true;
                m++;
                limpaMBR();
                arrayMBRDir = "teste".toCharArray();

            }
            if (arrayMBREsq.length >= 4 && arrayMBREsq[0] == 'J' && arrayMBREsq[1] == 'U' && arrayMBREsq[2] == 'M' && arrayMBREsq[3] == 'P' && arrayMBREsq[4] == 'M' && execEsq) {
                jumpEsq();
                imprimeRegistradores();
                arrayMBREsq = "teste".toCharArray();
            }
            if (arrayMBRDir.length >= 4 && arrayMBRDir[0] == 'J' && arrayMBRDir[1] == 'U' && arrayMBRDir[2] == 'M' && arrayMBRDir[3] == 'P' && arrayMBRDir[4] == 'M' && execDir) {
                jumpDir();
                imprimeRegistradores();
                arrayMBRDir = "teste".toCharArray();

            }
            if (arrayMBREsq.length >= 5 && arrayMBREsq[0] == 'J' && arrayMBREsq[1] == 'U' && arrayMBREsq[2] == 'M' && arrayMBREsq[3] == 'P' && arrayMBREsq[4] == '+' && execEsq) {
                if (AC >= 0) {
                    jumpEsq();
                } else {
                    execDir = true;
                    execEsq = false;
                }
                imprimeRegistradores();
                arrayMBREsq = "teste".toCharArray();
            }
            if (arrayMBRDir.length >= 5 && arrayMBRDir[0] == 'J' && arrayMBRDir[1] == 'U' && arrayMBRDir[2] == 'M' && arrayMBRDir[3] == 'P' && arrayMBRDir[4] == '+' && execDir) {
                if (AC >= 0) {
                    jumpDir();
                } else {
                    execDir = false;
                    execEsq = true;
                    m++;
                }
                imprimeRegistradores();
                arrayMBRDir = "teste".toCharArray();

            }
            if (arrayMBREsq.length >= 3 && arrayMBREsq[0] == 'E' && arrayMBREsq[1] == 'X' && arrayMBREsq[2] == 'I' && arrayMBREsq[3] == 'T') {
                System.out.println("Execução Finalizada");
                break;
            }

            if (arrayMBRDir.length >= 3 && arrayMBRDir[0] == 'E' && arrayMBRDir[1] == 'X' && arrayMBRDir[2] == 'I' && arrayMBRDir[3] == 'T') {
                System.out.println("Execução Finalizada");
                break;
            }
            System.out.println("Digite Enter para o próximo ciclo de clock");
            String msg = scanf.nextLine();
        }
    }

    public static void iniciaArrayMemoria() {
        for (int k = 0; k < memoria.length; k++) {
            Memoria memoria1 = new Memoria();
            memoria[k] = memoria1;
        }
    }

    public static void carregaMemoria() throws IOException {
        m = 0;
        char[] instArray;
        try (FileReader arq = new FileReader(endereco)) {
            //instanciar objeto que lê arquivo
            BufferedReader lerArq = new BufferedReader(arq);
            //ler linha
            String linha = lerArq.readLine(); //lê a primeira linha
            while (linha != null) {
                String instrucao;
                //System.out.println(linha);
                instrucao = linha.trim();
                instrucao = instrucao.replace(" ", "");
                instArray = instrucao.toCharArray();
                if (instArray[0] == '.') {
                    if (instArray[1] == 'o') {
                        StringBuilder sBuild = new StringBuilder(instArray.length);
                        for (int k = 4; k < instArray.length; k++) {
                            sBuild.append(instArray[k]);
                        }
                        vlr = sBuild.toString();
                        m = Integer.parseInt(vlr);

                    } else if (instArray[1] == 'w') {
                        String aux;
                        StringBuilder sBuild = new StringBuilder(instArray.length);
                        for (int k = 5; k < instArray.length; k++) {
                            sBuild.append(instArray[k]);
                        }
                        aux = sBuild.toString();
                        memoria[m].setDado(Integer.parseInt(aux));
                        memoria[m].setDir("");
                        memoria[m].setEsq("");
                        m++;
                    }
                } else if (memoria[m].getEsq().equals("")) {
                    memoria[m].setEsq(instrucao);
                } else {
                    memoria[m].setDir(instrucao);
                    m++;
                }
                linha = lerArq.readLine(); //lê da segunda a ultima linha
            }
            arq.close();
        }

    }

    public static void imprimeMemoria() {
        for (m = 0; m < 1024; m++) {
            if ((memoria[m].getDado() != 0) || (!memoria[m].getEsq().equals("")) || (!memoria[m].getDir().equals(""))) {
                System.out.println("\n");
                System.out.println("Posicao de Memoria:" + m);
                System.out.println("Dado..............:" + memoria[m].getDado());
                System.out.println("Lado esq..........:" + memoria[m].getEsq());
                System.out.println("Lado dir..........:" + memoria[m].getDir());
            }
        }
    }

    public static void carregaMBR() {
        while ((memoria[m].getEsq() == null || memoria[m].getDir() == null) && m < 1023) {
            m++;
        }
        if (!(memoria[m].getEsq() == null)) {
            i = 0;
            char[] instArray;
            instArray = memoria[m].getEsq().toCharArray();
            if (!(memoria[m].getEsq() == null)) {
                while (instArray[i] != '(') {
                    i++;
                }
                i--;

                StringBuilder sBuild = new StringBuilder(i);
                for (int k = 0; k <= i; k++) {
                    sBuild.append(instArray[k]);
                }
                MBR.getInstrEsq().setOpcode(sBuild.toString());

                sBuild = new StringBuilder(4);
                for (int k = i + 2; k <= memoria[m].getEsq().length() - 1; k++) {
                    sBuild.append(instArray[k]);
                }
                if (!sBuild.toString().equals("")) {

                    sBuild = new StringBuilder(4);
                    for (int k = 0; k <= 3; k++) {
                        sBuild.append(instArray[k]);
                    }
                    if (!sBuild.toString().equals("JUMP")) {
                        sBuild = new StringBuilder();
                        for (int k = i + 2; k < memoria[m].getEsq().length() - 1; k++) {
                            sBuild.append(instArray[k]);
                        }
                        MBR.getInstrEsq().setOp(Integer.parseInt(sBuild.toString()));
                    }
                    sBuild = new StringBuilder();
                    for (int k = memoria[m].getEsq().length() - 4; k <= memoria[m].getEsq().length() - 1; k++) {
                        sBuild.append(instArray[k]);
                    }
                    if (sBuild.toString().equals("0:19")) {
                        auxJMP = "0:19";
                        sBuild = new StringBuilder();
                        for (int k = i + 2; k <= memoria[m].getEsq().length() - 6; k++) {
                            sBuild.append(instArray[k]);
                        }
                        MBR.getInstrEsq().setOp(Integer.parseInt(sBuild.toString()));
                    }

                    sBuild = new StringBuilder();
                    for (int k = memoria[m].getEsq().length() - 5; k <= memoria[m].getEsq().length() - 1; k++) {
                        sBuild.append(instArray[k]);
                    }

                    if (sBuild.toString().equals("20:39")) {
                        auxJMP = "20:39";
                        sBuild = new StringBuilder();
                        for (int k = i + 2; k <= memoria[m].getEsq().length() - 7; k++) {
                            sBuild.append(instArray[k]);
                        }
                        MBR.getInstrEsq().setOp(Integer.parseInt(sBuild.toString()));
                    }
                }
                jumpReturn = true;
            }
        }
        if (!(memoria[m].getDir() == null)) {
            i = 0;
            char[] instArray;
            instArray = memoria[m].getDir().toCharArray();
            if (instArray.length > 0) {
                while (instArray[i] != '(') {
                    i++;
                }
                i--;

                StringBuilder sBuild = new StringBuilder(i);
                for (int k = 0; k <= i; k++) {
                    sBuild.append(instArray[k]);
                }
                MBR.getInstrDir().setOpcode(sBuild.toString());

                sBuild = new StringBuilder(4);
                for (int k = i + 2; k <= memoria[m].getDir().length() - 1; k++) {
                    sBuild.append(instArray[k]);
                }
                if (!sBuild.toString().equals("")) {

                    sBuild = new StringBuilder(4);
                    for (int k = 0; k <= 3; k++) {
                        sBuild.append(instArray[k]);
                    }
                    if (!sBuild.toString().equals("JUMP")) {
                        sBuild = new StringBuilder();
                        for (int k = i + 2; k < memoria[m].getDir().length() - 1; k++) {
                            sBuild.append(instArray[k]);
                        }
                        MBR.getInstrDir().setOp(Integer.parseInt(sBuild.toString()));
                    }
                    sBuild = new StringBuilder();
                    for (int k = memoria[m].getDir().length() - 4; k <= memoria[m].getDir().length() - 1; k++) {
                        sBuild.append(instArray[k]);
                    }
                    if (sBuild.toString().equals("0:19")) {
                        auxJMP = "0:19";
                        sBuild = new StringBuilder();
                        for (int k = i + 2; k <= memoria[m].getDir().length() - 6; k++) {
                            sBuild.append(instArray[k]);
                        }
                        MBR.getInstrDir().setOp(Integer.parseInt(sBuild.toString()));
                    }

                    sBuild = new StringBuilder();
                    for (int k = memoria[m].getDir().length() - 5; k <= memoria[m].getDir().length() - 1; k++) {
                        sBuild.append(instArray[k]);
                    }

                    if (sBuild.toString().equals("20:39")) {
                        auxJMP = "20:39";
                        sBuild = new StringBuilder();
                        for (int k = i + 2; k <= memoria[m].getDir().length() - 7; k++) {
                            sBuild.append(instArray[k]);
                        }
                        MBR.getInstrDir().setOp(Integer.parseInt(sBuild.toString()));
                    }
                }
                jumpReturn = true;
            }
        }
    }

    public static void imprimeRegistradores() {
        System.out.println("MQ.......:" + MQ);
        System.out.println("AC.......:" + AC);
        System.out.println("PC.......:" + PC);
        System.out.println("MAR......:" + MAR);
        System.out.println("IR.......:" + IR);
        System.out.println("IBR......:" + IBR);
        System.out.println("");
        System.out.println("MBR");
        System.out.println("Dado.....:" + MBR.getValor());
        System.out.println("Esq......:" + MBR.getInstrEsq().getOpcode());
        System.out.println("Dir......:" + MBR.getInstrDir().getOpcode());
        System.out.println("");
    }

    public static void limpaMBR() {
        MBR.getInstrEsq().setOpcode("");
        MBR.getInstrDir().setOpcode("");
    }

    public static void storEsq() {
        IR = MBR.getInstrEsq().getOpcode();
        IBR = MBR.getInstrDir().getOpcode();
        MAR = MBR.getInstrEsq().getOp();
        MBR.setValor(AC);
        memoria[MAR].setDado((int) MBR.getValor());
    }

    public static void storDir() {
        IR = MBR.getInstrEsq().getOpcode();
        IBR = MBR.getInstrDir().getOpcode();
        MAR = MBR.getInstrDir().getOp();
        MBR.setValor(AC);
        memoria[MAR].setDado((int) MBR.getValor());
    }

    public static void jumpEsq() {
        char[] instArray;
        instArray = memoria[m].getEsq().toCharArray();
        int a = 0;
        while (instArray[a] != '(') {
            a++;
        }
        a++;
        int b = a;
        while (instArray[b] != ',') {
            b++;
        }
        b--;
        StringBuilder sBuild = new StringBuilder();
        for (int k = a; k <= b; k++) {
            sBuild.append(instArray[k]);
        }
        MAR = Integer.parseInt(sBuild.toString());
        sBuild = new StringBuilder();
        for (int k = b + 2; k <= instArray.length - 2; k++) {
            sBuild.append(instArray[k]);
        }
        auxJMP = sBuild.toString();
        if (auxJMP.equals("0:19")) {
            m = MAR;
            carregaMBR();
            auxJMP = "";
            execDir = false;
            execEsq = true;
        }
        if (auxJMP.equals("20:39")) {
            m = MAR;
            carregaMBR();
            auxJMP = "";
            execDir = true;
            execEsq = false;
        }
    }

    public static void jumpDir() {
        char[] instArray;
        instArray = memoria[m].getDir().toCharArray();
        int a = 0;
        while (instArray[a] != '(') {
            a++;
        }
        a++;
        int b = a;
        while (instArray[b] != ',') {
            b++;
        }
        b--;
        StringBuilder sBuild = new StringBuilder();
        for (int k = a; k <= b; k++) {
            sBuild.append(instArray[k]);
        }
        MAR = Integer.parseInt(sBuild.toString());
        sBuild = new StringBuilder();
        for (int k = 10; k <= instArray.length - 2; k++) {
            sBuild.append(instArray[k]);
        }
        auxJMP = sBuild.toString();
        if (auxJMP.equals("0:19")) {
            m = MAR;
            carregaMBR();
            auxJMP = "";
            execDir = false;
            execEsq = true;
        }
        if (auxJMP.equals("20:39")) {
            m = MAR;
            carregaMBR();
            auxJMP = "";
            execDir = true;
            execEsq = false;
        }
    }

    public static void mulEsq() {
        IR = MBR.getInstrEsq().getOpcode();
        IBR = MBR.getInstrDir().getOpcode();
        MAR = MBR.getInstrEsq().getOp();
        MBR.setValor(memoria[MAR].getDado());
        AC = MQ * MBR.getValor();
    }

    public static void mulDir() {
        IR = IBR;
        IBR = "";
        MAR = MBR.getInstrDir().getOp();
        MBR.setValor(memoria[MAR].getDado());
        AC = MQ * MBR.getValor();
    }

    public static void divEsq() {
        IR = MBR.getInstrEsq().getOpcode();
        IBR = MBR.getInstrDir().getOpcode();
        MAR = MBR.getInstrEsq().getOp();
        MBR.setValor(memoria[MAR].getDado());
        MQ = (int) (AC / MBR.getValor());
        AC = (AC % MBR.getValor());
    }

    public static void divDir() {
        IR = IBR;
        IBR = "";
        MAR = MBR.getInstrDir().getOp();
        MBR.setValor(memoria[MAR].getDado());
        MQ = (int) (AC / MBR.getValor());
        AC = (AC % MBR.getValor());
    }

    public static void addEsq() {
        IR = MBR.getInstrEsq().getOpcode();
        IBR = MBR.getInstrDir().getOpcode();
        MAR = MBR.getInstrEsq().getOp();
        MBR.setValor(memoria[MAR].getDado());
        char[] auxMBR;
        auxMBR = MBR.getInstrEsq().getOpcode().toCharArray();
        if (auxMBR.length >= 4 && auxMBR[3] == 'M') {
            AC = AC + MBR.getValor();
        }
        if (auxMBR.length >= 5 && auxMBR[3] == '|' && auxMBR[4] == 'M') {
            AC = AC + Math.abs(MBR.getValor());
        }
    }

    public static void addDir() {
        IR = MBR.getInstrEsq().getOpcode();
        IBR = MBR.getInstrDir().getOpcode();
        MAR = MBR.getInstrDir().getOp();
        MBR.setValor(memoria[MAR].getDado());
        char[] auxMBR;
        auxMBR = MBR.getInstrDir().getOpcode().toCharArray();
        if (auxMBR[3] == 'M') {
            AC = AC + MBR.getValor();
        }
        if (auxMBR[3] == '|' && auxMBR[4] == 'M') {
            AC = AC + Math.abs(MBR.getValor());
        }
    }

    public static void subEsq() {
        IR = MBR.getInstrEsq().getOpcode();
        IBR = MBR.getInstrDir().getOpcode();
        MAR = MBR.getInstrEsq().getOp();
        MBR.setValor(memoria[MAR].getDado());
        char[] auxMBR;
        auxMBR = MBR.getInstrEsq().getOpcode().toCharArray();
        if (auxMBR[3] == 'M') {
            AC = AC - MBR.getValor();
        }
        if (auxMBR[3] == '|' && auxMBR[4] == 'M') {
            AC = AC - Math.abs(MBR.getValor());
        }
    }

    public static void subDir() {
        IR = IBR;
        IBR = "";
        MAR = MBR.getInstrDir().getOp();
        MBR.setValor(memoria[MAR].getDado());
        char[] auxMBR;
        auxMBR = MBR.getInstrDir().getOpcode().toCharArray();
        if (auxMBR[3] == 'M') {
            AC = AC - MBR.getValor();
        }
        if (auxMBR[3] == '|' && auxMBR[4] == 'M') {
            AC = AC - Math.abs(MBR.getValor());
        }
    }

    public static void loadEsq() {
        IR = MBR.getInstrEsq().getOpcode();
        IBR = MBR.getInstrDir().getOpcode();
        MAR = MBR.getInstrEsq().getOp();
        MBR.setValor(memoria[MAR].getDado());
        char[] auxMBR;
        auxMBR = MBR.getInstrEsq().getOpcode().toCharArray();
        if (auxMBR.length >= 7 && auxMBR[4] == 'M' && auxMBR[5] == 'Q' && auxMBR[6] != ',') {
            AC = MQ;
        } else if (auxMBR.length >= 8 && auxMBR[4] == 'M' && auxMBR[5] == 'Q' && auxMBR[6] == ',' && auxMBR[7] == 'M') {
            MQ = MBR.getValor();
        } else if (auxMBR.length >= 5 && auxMBR[4] == 'M') {
            AC = MBR.getValor();
        } else if (auxMBR.length >= 6 && auxMBR[4] == '-' && auxMBR[5] == 'M') {
            AC = MBR.getValor() * -1;
        } else if (auxMBR.length >= 7 && auxMBR[4] == '|' && auxMBR[5] == 'M' && auxMBR[6] == '(') {
            AC = Math.abs(MBR.getValor());
        }
    }

    public static void loadDir() {
        IR = IBR;
        IBR = "";
        MAR = MBR.getInstrDir().getOp();
        MBR.setValor(memoria[MAR].getDado());
        char[] auxMBR;
        auxMBR = MBR.getInstrDir().getOpcode().toCharArray();
        if (auxMBR.length >= 7 && auxMBR[4] == 'M' && auxMBR[5] == 'Q' && auxMBR[6] != ',') {
            AC = MQ;
        } else if (auxMBR.length >= 8 && auxMBR[4] == 'M' && auxMBR[5] == 'Q' && auxMBR[6] == ',' && auxMBR[7] == 'M') {
            MQ = MBR.getValor();
        } else if (auxMBR.length >= 6 && auxMBR[4] == 'M' && auxMBR[5] != 'Q') {
            AC = MBR.getValor();
        } else if (auxMBR.length >= 6 && auxMBR[4] == '-' && auxMBR[5] == 'M') {
            AC = MBR.getValor() * -1;
        } else if (auxMBR.length >= 7 && auxMBR[4] == '|' && auxMBR[5] == 'M' && auxMBR[6] == '(') {
            AC = Math.abs(MBR.getValor());
        }
    }
}

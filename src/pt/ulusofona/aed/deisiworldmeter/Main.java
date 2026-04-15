package pt.ulusofona.aed.deisiworldmeter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    static ArrayList<Paises> pais = new ArrayList<>();
    static ArrayList<Cidade> cidades = new ArrayList<>();
    static ArrayList<Populacao> populacoes = new ArrayList<>();
    static ArrayList<InputsInvalidos> inputsInv = new ArrayList<>();

    static boolean parseFiles(File folder) {
        pais.clear();
        cidades.clear();
        populacoes.clear();
        inputsInv.clear();

        File filePaises = new File(folder, "paises.csv");
        File fileCidades = new File(folder, "cidades.csv");
        File filePopulacao = new File(folder, "populacao.csv");

        boolean leuPais = lerPaises(filePaises);
        boolean leuCidades = lerCidades(fileCidades);
        boolean leuPopulacao = lerPopulacao(filePopulacao);

        // Para IDs > 700, contar linhas de população desse país
        for (Paises p : pais) {
            if (p.id > 700) {
                int nLinhas = 0;
                for (Populacao pop : populacoes) {
                    if (pop.id == p.id) {
                        nLinhas++;
                    }
                }
                p.linhasId = nLinhas;
            }
        }

        return leuPais && leuCidades && leuPopulacao;
    }

    static boolean lerPaises(File file) {
        Scanner scanner;
        int nLinhasOK = 0;
        int nLinhasNOK = 0;
        int primeiraNOK = -1; // -1 caso não exista nenhuma inválida
        int linhaAtual = 0;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return false;
        }

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            linhaAtual++;

            // Ignorar cabeçalho
            if (linhaAtual == 1) {
                continue;
            }

            String[] partes = linha.split(",");

            boolean valida = false;
            if (partes.length == 4 && verificarInteiro(partes[0])) {
                int id = Integer.parseInt(partes[0]);
                valida = repetidosPaises(id);
                if (valida) {
                    String alfa2 = partes[1];
                    String alfa3 = partes[2];
                    String nome = partes[3];
                    pais.add(new Paises(id, alfa2, alfa3, nome, 0));
                }
            }

            if (valida) {
                nLinhasOK++;
            } else {
                nLinhasNOK++;
                if (primeiraNOK == -1){
                    primeiraNOK = linhaAtual;
                }
            }
        }

        if (primeiraNOK == -1){
            primeiraNOK = 0;
        }
        inputsInv.add(new InputsInvalidos(nLinhasOK, "paises.csv", nLinhasNOK, primeiraNOK));
        return true;
    }

    static boolean lerCidades(File file) {
        Scanner scanner;
        int nLinhasOK = 0;
        int nLinhasNOK = 0;
        int primeiraNOK = -1;
        int linhaAtual = 0;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return false;
        }

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            linhaAtual++;

            // Ignorar cabeçalho
            if (linhaAtual == 1){
                continue;
            }

            String[] partes = linha.split(",");

            boolean valida = false;
            if (partes.length == 6 &&
                    !partes[0].isEmpty() &&
                    !partes[1].isEmpty() &&
                    verificarInteiro(partes[2]) &&
                    verificarDouble(partes[3]) &&
                    verificarDouble(partes[4]) &&
                    verificarDouble(partes[5]) &&
                    cidadeNoPais(partes[0])) {

                String alfa2 = partes[0];
                String nome = partes[1];
                String regiao = partes[2];
                double populacao = Double.parseDouble(partes[3]);
                String latitude = partes[4];
                String longitude = partes[5];

                cidades.add(new Cidade(alfa2, nome, regiao, populacao, latitude, longitude));
                valida = true;
            }

            if (valida) {
                nLinhasOK++;
            } else {
                nLinhasNOK++;
                if (primeiraNOK == -1) {
                    primeiraNOK = linhaAtual;
                }
            }
        }

        if (primeiraNOK == -1) {
            primeiraNOK = 0;
        }
        inputsInv.add(new InputsInvalidos(nLinhasOK, "cidades.csv", nLinhasNOK, primeiraNOK));
        return true;
    }

    static boolean lerPopulacao(File file) {
        Scanner scanner;
        int nLinhasOK = 0;
        int nLinhasNOK = 0;
        int primeiraNOK = -1;
        int linhaAtual = 0;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return false;
        }

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            linhaAtual++;

            // Ignorar cabeçalho
            if (linhaAtual == 1){
                continue;
            }

            String[] partes = linha.split(",");

            boolean valida = false;
            if (partes.length == 5 &&
                    idContidoPaises(partes[0]) &&
                    verificarInteiro(partes[1]) &&
                    verificarInteiro(partes[2]) &&
                    verificarInteiro(partes[3]) &&
                    verificarDouble(partes[4])) {

                int id = Integer.parseInt(partes[0]);
                int ano = Integer.parseInt(partes[1]);
                int popMasc = Integer.parseInt(partes[2]);
                int popFem = Integer.parseInt(partes[3]);
                double densidade = Double.parseDouble(partes[4]);

                populacoes.add(new Populacao(id, ano, popMasc, popFem, densidade));
                valida = true;
            }

            if (valida) {
                nLinhasOK++;
            } else {
                nLinhasNOK++;
                if (primeiraNOK == -1) {
                    primeiraNOK = linhaAtual;
                }
            }
        }

        if (primeiraNOK == -1){
            primeiraNOK = 0;
        }
        inputsInv.add(new InputsInvalidos(nLinhasOK, "populacao.csv", nLinhasNOK, primeiraNOK));
        return true;
    }

    static ArrayList getObjects(TipoEntidade tipo) {
        return switch (tipo) {
            case PAIS -> pais;
            case CIDADE -> cidades;
            case INPUT_INVALIDO -> inputsInv;
        };
    }

    static boolean repetidosPaises(int id) {
        for (Paises p : pais) {
            if (p.id == id){
                return false;
            }
        }
        return true;
    }

    static boolean verificarInteiro(String valor) {
        try {
            Integer.parseInt(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static boolean verificarDouble(String valor) {
        try {
            Double.parseDouble(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static boolean idContidoPaises(String id) {
        if (!verificarInteiro(id)) {
            return false;
        }
        int idValido = Integer.parseInt(id);
        for (Paises p : pais) {
            if (p.id == idValido) {
                return true;
            }
        }
        return false;
    }

    static boolean cidadeNoPais(String alfa2) {
        for (Paises p : pais) {
            if (Objects.equals(p.alfa2, alfa2.toUpperCase())){
                return true;
            }
        }
        return false;
    }


// ... (restante do código)

        public static void main(String[] args) {
            System.out.println("Bem-Vindo ao DEISI World Meter");

            long start = System.currentTimeMillis();
            boolean parseOk = parseFiles(new File("test-files"));
            if (!parseOk) {
                System.out.println("Erro na leitura dos ficheiros!!");
                return;
            }
            long end = System.currentTimeMillis();
            System.out.println("Ficheiros foram lidos com sucesso em " + (end - start) + " ms.");

            inputsInv = getObjects(TipoEntidade.INPUT_INVALIDO);
            System.out.println();
            System.out.println("Informacoes sobre a leitura dos ficheiros:");

            if (!inputsInv.isEmpty()) {
                System.out.println("nome | linhas OK | linhas NOK | primeira linha NOK");
                System.out.println(inputsInv.get(0));
                System.out.println(inputsInv.get(1));
                System.out.println(inputsInv.get(2));
            } else {
                System.out.println("Nenhuma informacao disponivel sobre inputs invalidos.");
            }

            pais = getObjects(TipoEntidade.PAIS);
            System.out.println();
            System.out.println("Alguns paises:");

            if (!pais.isEmpty()) {
                System.out.println(pais.get(0));
                System.out.println(pais.get(1));
                System.out.println(pais.get(2));
                System.out.println(pais.get(3));
                System.out.println(pais.get(4));
            } else {
                System.out.println("Nenhum pais disponivel.");
            }

            cidades = getObjects(TipoEntidade.CIDADE);
            System.out.println();
            System.out.println("Algumas cidades:");

            if (!cidades.isEmpty()) {
                System.out.println(cidades.get(0));
                System.out.println(cidades.get(1));
                System.out.println(cidades.get(2));
                System.out.println(cidades.get(3));
                System.out.println(cidades.get(4));
                System.out.println(cidades.get(5));
                System.out.println(cidades.get(6));
                System.out.println(cidades.get(7));
                System.out.println(cidades.get(8));
                System.out.println(cidades.get(9));
            } else {
                System.out.println("Nenhuma cidade disponivel.");
            }
        }}



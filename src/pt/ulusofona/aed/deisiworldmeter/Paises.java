package pt.ulusofona.aed.deisiworldmeter;

public class Paises {
    public int linhasId;
    int id;
    String alfa2;
    String alfa3;
    String nome;
    int maiorQue700;

    public Paises(int id, String alfa2, String alfa3, String nome, int maiorQue700) {
        this.id = id;
        this.alfa2 = alfa2.toUpperCase();
        this.alfa3 = alfa3.toUpperCase();
        this.nome = nome;
        this.linhasId = maiorQue700; // ou renomeia parâmetro
    }



    @Override
    public String toString() {
        if (id > 700) {
            return nome + " | " + id + " | " + alfa2.toUpperCase() + " | " + alfa3.toUpperCase() + " | " + linhasId;
        } else {
            return nome + " | " + id + " | " + alfa2.toUpperCase() + " | " + alfa3.toUpperCase();
        }
    }
}
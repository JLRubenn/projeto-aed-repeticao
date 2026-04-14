package pt.ulusofona.aed.deisiworldmeter;

public class InputsInvalidos {
    String nome;
    int numLinhasCorretas;
    int numLinhasMal;
    int primeiraLinhaMal;

    public InputsInvalidos(int numLinhasCorretas, String nome, int numLinhasMal, int primeiraLinhaMal) {
        this.numLinhasCorretas = numLinhasCorretas;
        this.nome = nome;
        this.numLinhasMal = numLinhasMal;
        this.primeiraLinhaMal = primeiraLinhaMal;
    }

    @Override
    public String toString() {
        return nome + " | " + numLinhasCorretas + " | " + numLinhasMal + " | " + primeiraLinhaMal;
    }
}

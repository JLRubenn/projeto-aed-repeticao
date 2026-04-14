package pt.ulusofona.aed.deisiworldmeter;

public class Populacao {
    int id;
    int ano;
    int populacaoMasculina;
    int populacaoFeminina;
    double densidade;

    public Populacao(int id, int ano, int populacaoMasculina, int populacaoFeminina, double densidade) {
        this.id = id;
        this.ano = ano;
        this.populacaoMasculina = populacaoMasculina;
        this.populacaoFeminina = populacaoFeminina;
        this.densidade = densidade;
    }

    @Override
    public String toString() {
        return "Populacao{" +
                "id=" + id +
                ", ano=" + ano +
                ", populacaoMasculina=" + populacaoMasculina +
                ", populacaoFeminina=" + populacaoFeminina +
                ", densidade=" + densidade +
                '}';
    }
}

package pt.ulusofona.aed.deisiworldmeter;


    public class Cidade {
        String alfa2;
        String nome;
        String regiao; // keep as String to preserve leading zeros like "07"
        double populacao;
        double latitude;
        double longitude;

        public Cidade(String alfa2, String nome, String regiao, double populacao, String latitude, String longitude) {
            this.alfa2 = alfa2.toUpperCase();
            this.nome = nome;
            this.regiao = regiao; // store the original string (preserves leading zeros)
            this.populacao = populacao;
            this.latitude = Double.parseDouble(latitude);
            this.longitude = Double.parseDouble(longitude);
        }


        public Object getAlfa2() {
            return alfa2;
        }

        public Object getNome() {
            return nome;
        }
        @Override
        public String toString() {
            return nome + " | " + alfa2.toUpperCase() + " | " + regiao + " | " + (int) populacao + " | (" + latitude + "," + longitude + ")";
        }
    }

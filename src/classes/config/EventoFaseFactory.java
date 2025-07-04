package src.classes.config;

import src.classes.config.EventoFaseFactory.EventoFase;

/**
 * Fábrica de eventos para fases do jogo.
 * Define a interface EventoFase e implementações concretas.
 */
public class EventoFaseFactory {

    public interface EventoFase {
        long getTempo();             // Quando o evento deve acontecer
        boolean isDisparado();
        void setDisparado(boolean valor);  // Marcar como executado
        String getTipo();            // INIMIGO, POWERUP, etc.
        int getSubtipo();            // Tipo específico (ex: inimigo 1 ou 2)
        double getX();
        double getY();
    }

    public static class EventoInimigo implements EventoFase {
        private int tipo;
        private long tempo;
        private double x, y;
        private boolean disparado = false;

        public EventoInimigo(int tipo, long tempo, double x, double y) {
            this.tipo = tipo;
            this.tempo = tempo;
            this.x = x;
            this.y = y;
        }

        public long getTempo() { return tempo; }
        public boolean isDisparado() { return disparado; }
        public void setDisparado(boolean valor) { disparado = valor; }

        public String getTipo() { return "INIMIGO"; }
        public int getSubtipo() { return tipo; }
        public double getX() { return x; }
        public double getY() { return y; }
    }

    public static class EventoChefe implements EventoFase {
        private int tipo;
        private long tempo;
        private double x, y;
        private boolean disparado = false;

        public EventoChefe(int tipo, long tempo, double x, double y) {
            this.tipo = tipo;
            this.tempo = tempo;
            this.x = x;
            this.y = y;
        }

        public long getTempo() { return tempo; }
        public boolean isDisparado() { return disparado; }
        public void setDisparado(boolean valor) { disparado = valor; }

        public String getTipo() { return "INIMIGO"; }
        public int getSubtipo() { return tipo; }
        public double getX() { return x; }
        public double getY() { return y; }
    }

    public static class EventoPowerUp implements EventoFase {
        private int tipo;
        private long tempo;
        private double x, y;
        private boolean disparado = false;

        public EventoPowerUp(int tipo, long tempo, double x, double y) {
            this.tipo = tipo;
            this.tempo = tempo;
            this.x = x;
            this.y = y;
        }

        public long getTempo() { return tempo; }
        public boolean isDisparado() { return disparado; }
        public void setDisparado(boolean valor) { disparado = valor; }

        public String getTipo() { return "INIMIGO"; }
        public int getSubtipo() { return tipo; }
        public double getX() { return x; }
        public double getY() { return y; }
    }

    public static EventoFase criarEvento(String linha) {
        String[] partes = linha.split(" ");
        switch (partes[0]) {
            case "INIMIGO":
                if (partes.length == 5) {
                    int tipo = Integer.parseInt(partes[1]);
                    long tempo = Long.parseLong(partes[2]);
                    double x = Double.parseDouble(partes[3]);
                    double y = Double.parseDouble(partes[4]);
                    return new EventoInimigo(tipo, tempo, x, y);
                }
                break;
    
            case "POWERUP":
                if (partes.length == 5) {
                    int tipo = Integer.parseInt(partes[1]);// Ex: "velocity" ou "multipleProjectiles"
                    long tempo = Long.parseLong(partes[2]);
                    double x = Double.parseDouble(partes[3]);
                    double y = Double.parseDouble(partes[4]);
                    return new EventoPowerUp(tipo, tempo, x, y);
                }
                break;
    
            case "CHEFE":
                if (partes.length == 6) {
                    int tipo = Integer.parseInt(partes[1]);
                    int vida = Integer.parseInt(partes[2]);
                    long tempo = Long.parseLong(partes[3]);
                    double x = Double.parseDouble(partes[4]);
                    double y = Double.parseDouble(partes[5]);
                    return new EventoChefe(tipo, vida, tempo, x, y);
                }
                break;
        }
    }
}




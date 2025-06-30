package src.classes.config;

import java.io.*;
import java.util.*;

public class Config {

    private double vidaJogador;
    private int numeroDeFases;
    private List<String> arquivosFases;

    public Config(String caminhoArquivo) throws IOException {
        arquivosFases = new ArrayList<>();
        lerConfiguracao(caminhoArquivo);
    }

    private void lerConfiguracao(String caminho) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(caminho));

        // Lê pontos de vida
        vidaJogador = Integer.parseInt(br.readLine().trim());

        // Lê número de fases
        numeroDeFases = Integer.parseInt(br.readLine().trim());

        // Lê os nomes dos arquivos de cada fase
        for (int i = 0; i < numeroDeFases; i++) {
            String nomeArquivoFase = br.readLine().trim();
            arquivosFases.add(nomeArquivoFase);
        }

        br.close();
    }

    public double getVidaJogador() {
        return vidaJogador;
    }

    public int getNumeroDeFases() {
        return numeroDeFases;
    }

    public List<String> getArquivosFases() {
        return arquivosFases;
    }

}

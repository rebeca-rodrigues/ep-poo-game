package src.classes.config;

import java.io.*;
import java.util.*;

public class Fase {

    private List<EventoFaseFactory.EventoFase> eventos;

    public Fase(String caminhoArquivo) throws IOException {
        eventos = new ArrayList<>();
        carregarEventos(caminhoArquivo);
    }

    private void carregarEventos(String caminho) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(caminho));
        String linha;
        while ((linha = br.readLine()) != null) {
            EventoFaseFactory.EventoFase evento = EventoFaseFactory.criarEvento(linha);
            if (evento != null)
                eventos.add(evento);
        }
        br.close();
    }

    public List<EventoFaseFactory.EventoFase> getEventos() {
        return eventos;
    }
}


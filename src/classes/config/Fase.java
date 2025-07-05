package src.classes.config;

import java.io.*;
import java.util.*;

public class Fase {

    private List<EventoFactory.Evento> eventos;

    public Fase(String caminhoArquivo) throws IOException {
        eventos = new ArrayList<>();
        carregarEventos(caminhoArquivo);
    }

    private void carregarEventos(String caminho) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(caminho));
        String linha;
        while ((linha = br.readLine()) != null) {
            EventoFactory.Evento evento = EventoFactory.criarEvento(linha);
            if (evento != null)
                eventos.add(evento);
        }
        br.close();
    }

    public List<EventoFactory.Evento> getEventos() {
        return eventos;
    }
}


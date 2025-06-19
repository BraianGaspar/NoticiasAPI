package br.blogibge.util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.blogibge.model.Usuario;

public class JsonManager {
    private static final String FILE_NAME = "usuario.json";
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public static void salvar(Usuario usuario) {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(usuario, writer);
        } catch (IOException e) {
            System.out.println("Erro ao salvar JSON: " + e.getMessage());
        }
    }

    public static Usuario carregar() {
        try (Reader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, Usuario.class);
        } catch (IOException e) {
            return null;
        }
    }
}

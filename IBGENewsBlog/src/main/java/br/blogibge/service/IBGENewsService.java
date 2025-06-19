package br.blogibge.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.blogibge.model.Noticia;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class IBGENewsService {
    private static final HttpUrl URL = HttpUrl.parse("https://servicodados.ibge.gov.br/api/v3/noticias/?qtd=10");
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public List<Noticia> buscarNoticias() {
        List<Noticia> lista = new ArrayList<>();
        Request request = new Request.Builder().url(URL).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json;
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    json = responseBody.string();
                } else {
                    System.out.println("Resposta da API sem corpo.");
                    return lista;
                }

                JsonObject jsonObj = JsonParser.parseString(json).getAsJsonObject();
                JsonArray items = jsonObj.getAsJsonArray("items");

                for (JsonElement item : items) {
                    JsonObject obj = item.getAsJsonObject();
                    Noticia noticia = new Noticia(
                            obj.get("id").getAsInt(),
                            obj.get("titulo").getAsString(),
                            obj.get("introducao").getAsString(),
                            obj.get("data_publicacao").getAsString(),
                            obj.get("link").getAsString(),
                            obj.get("tipo").getAsString()
                    );
                    lista.add(noticia);
                }
            } else {
                System.out.println("Erro na API: status " + response.code());
            }
        } catch (IOException e) {
            System.out.println("Erro na conexão com a API: " + e.getMessage());
        }
        return lista;
    }
    public Gson getGson() {
      return gson;
  }
}

package com.nilsedgar.dockerexercise;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WebController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/pokemon/{name}")
    @ResponseBody
    public String getPokemon(@PathVariable String name) {
        try {
            String apiUrl = "https://pokeapi.co/api/v2/pokemon/" + name;
            String response = restTemplate.getForObject(apiUrl, String.class);

            // Parsa JSON för att få ut den information vi vill ha
            JsonNode rootNode = objectMapper.readTree(response);
            String pokemonName = rootNode.path("name").asText();
            int height = rootNode.path("height").asInt();
            int weight = rootNode.path("weight").asInt();
            String imageUrl = rootNode.path("sprites").path("front_default").asText();
            String type = rootNode.path("types").path(0).path("type").path("name").asText();

            // Skapa en enkel HTML-sida med informationen
            return String.format(
                    "<html><body style='font-family: Arial; max-width: 500px; margin: 0 auto; padding: 20px;'>" +
                            "<h1 style='text-transform: capitalize;'>%s</h1>" +
                            "<img src='%s' alt='%s' style='width: 200px; height: 200px;'>" +
                            "<div style='background-color: #f5f5f5; padding: 15px; border-radius: 8px;'>" +
                            "<p><strong>Typ:</strong> %s</p>" +
                            "<p><strong>Höjd:</strong> %.1f m</p>" +
                            "<p><strong>Vikt:</strong> %.1f kg</p>" +
                            "</div>" +
                            "</body></html>",
                    pokemonName, imageUrl, pokemonName, type, height/10.0, weight/10.0
            );
        } catch (Exception e) {
            return "<h1>Kunde inte hitta Pokémon: " + name + "</h1>";
        }
    }


    @GetMapping("/hello")
    public String hello(){
        return "<h1>Hello!</h1>";
    }



}

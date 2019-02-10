package br.com.casadocodigo.integracao.bookserver;

import java.net.URI;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.casadocodigo.integracao.model.Livro;

@Service
public class BookserverService {

    public List<Livro> livros(String token) throws UsuarioSemAutorizacaoException {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + token);

        String endpoint = "http://localhost:8080/api/v2/livros";

        RequestEntity<Object> request = new RequestEntity<>(
            headers,
            HttpMethod.GET,
            URI.create(endpoint)
        );

        try {
            ResponseEntity<List<Livro>> resposta = restTemplate.exchange(request, new ParameterizedTypeReference<List<Livro>>() {});
            if (resposta.getStatusCode().is2xxSuccessful()) {
                return resposta.getBody();
            } else {
                throw new RuntimeException("sem sucesso");
            }
        } catch (HttpClientErrorException e) {
            throw new UsuarioSemAutorizacaoException("não foi possível obter os livros do usuário");
        }

    }
}

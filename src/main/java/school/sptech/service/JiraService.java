package school.sptech.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import school.sptech.dto.jira.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

public class JiraService {
    private final String apiEndpoint;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public JiraService() {
        apiEndpoint = "/rest/api/3/issue";

        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();

        objectMapper = new ObjectMapper();
    }

    public String createIssue(
            String baseUrl,
            String email,
            String apiToken,
            String projectKey,
            String summary,
            String textDescription,
            String issueType
    ) throws IOException, InterruptedException {
        DescriptionDTO description = buildDescription(textDescription);
        FieldsDTO fields = new FieldsDTO(
                new ProjectDTO(projectKey),
                summary,
                description,
                new IssueTypeDTO(issueType)
        );

        IssueRequestDTO issueRequest = new IssueRequestDTO(fields);
        String jsonBody = objectMapper.writeValueAsString(issueRequest);

        String authString = "%s:%s".formatted(email, apiToken);
        String authHeader = "Basic %s".formatted(
                Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8))
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + apiEndpoint))
                .timeout(Duration.ofSeconds(60))
                .header("Authorization", authHeader)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();
        if (status >= 200 && status < 300) {
            return response.body();
        }

        throw new RuntimeException("\n[ ERRO ] Requisição HTTP para o Jira falhou.\nStatus: %d\nError: %s\n".formatted(status, response.body()));
    }

    private DescriptionDTO buildDescription(String description) {
        TextNodeDTO textNode = new TextNodeDTO("text", description);
        ContentBlockDTO paragraph = new ContentBlockDTO("paragraph", List.of(textNode));
        return new DescriptionDTO("doc", 1, List.of(paragraph));
    }
}

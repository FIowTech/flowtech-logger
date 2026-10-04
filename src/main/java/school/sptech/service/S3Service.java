package school.sptech.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import school.sptech.config.S3Config;
import school.sptech.dto.captura.CapturaDTO;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class S3Service {
    private final String bucketPrefix;
    private final S3Client s3Client;
    private final ObjectMapper objectMapper;

    public S3Service() {
        S3Config s3Config = new S3Config();

        bucketPrefix = "03-gold";
        s3Client = s3Config.getS3Client();
        objectMapper = new ObjectMapper();
    }

    public List<CapturaDTO> lerCapturasHoraAtual(String bucketName) {
        List<CapturaDTO> captures = new ArrayList<>();
        String prefix = gerarPrefixoHoraAtual();

        System.out.println("[ LOG ] Buscando Capturas em: " + prefix);

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);
        for (S3Object content : response.contents()) {
            if(content.key().endsWith(".json")) {
                CapturaDTO capture = lerJsonParaObjeto(bucketName, content.key());
                if(capture != null) captures.add(capture);
            }
        }

        return captures;
    }

    private CapturaDTO lerJsonParaObjeto(String bucketName, String keyName) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        try(ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(request)) {
            return objectMapper.readValue(inputStream, CapturaDTO.class);
        } catch(Exception e) {
            System.out.printf("[ ERRO ] Falha ao ler/processar o arquivo \"%s\": %s\n", keyName, e.getMessage());
            return null;
        }
    }

    private String gerarPrefixoHoraAtual() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

        return String.format("%s/ano=%04d/mes=%02d/dia=%02d/hora=%02d/",
                bucketPrefix,
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour()
        );
    }
}

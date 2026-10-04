package school.sptech.dto.jira;

import java.util.List;

public record DescriptionDTO(
        String type,
        Integer version,
        List<ContentBlockDTO> content
) {}

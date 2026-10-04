package school.sptech.dto.jira;

import java.util.List;

public record ContentBlockDTO(
        String type,
        List<TextNodeDTO> content
) {}

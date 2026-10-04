package school.sptech.dto.jira;

public record FieldsDTO(
        ProjectDTO project,
        String summary,
        DescriptionDTO description,
        IssueTypeDTO issuetype
) {}

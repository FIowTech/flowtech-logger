package school.sptech.model;

import java.time.LocalDateTime;

public class Empresa {
    private Integer idEmpresa;
    private Integer enderecoId;

    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String email;

    private String jiraEmail;
    private String jiraBaseUrl;
    private String jiraProjectKey;
    private String jiraApiKey;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Empresa() {
    }

    public Empresa(Integer idEmpresa, Integer enderecoId, String cnpj, String razaoSocial, String nomeFantasia, String email, String jiraEmail, String jiraBaseUrl, String jiraProjectKey, String jiraApiKey, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.idEmpresa = idEmpresa;
        this.enderecoId = enderecoId;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.email = email;
        this.jiraEmail = jiraEmail;
        this.jiraBaseUrl = jiraBaseUrl;
        this.jiraProjectKey = jiraProjectKey;
        this.jiraApiKey = jiraApiKey;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Integer getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Integer enderecoId) {
        this.enderecoId = enderecoId;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJiraEmail() {
        return jiraEmail;
    }

    public void setJiraEmail(String jiraEmail) {
        this.jiraEmail = jiraEmail;
    }

    public String getJiraBaseUrl() {
        return jiraBaseUrl;
    }

    public void setJiraBaseUrl(String jiraBaseUrl) {
        this.jiraBaseUrl = jiraBaseUrl;
    }

    public String getJiraProjectKey() {
        return jiraProjectKey;
    }

    public void setJiraProjectKey(String jiraProjectKey) {
        this.jiraProjectKey = jiraProjectKey;
    }

    public String getJiraApiKey() {
        return jiraApiKey;
    }

    public void setJiraApiKey(String jiraApiKey) {
        this.jiraApiKey = jiraApiKey;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "idEmpresa=" + idEmpresa +
                ", enderecoId=" + enderecoId +
                ", cnpj='" + cnpj + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", nomeFantasia='" + nomeFantasia + '\'' +
                ", email='" + email + '\'' +
                ", jiraEmail='" + jiraEmail + '\'' +
                ", jiraBaseUrl='" + jiraBaseUrl + '\'' +
                ", jiraProjectKey='" + jiraProjectKey + '\'' +
                ", jiraApiKey='" + jiraApiKey + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}

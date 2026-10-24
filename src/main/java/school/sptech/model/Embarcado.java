package school.sptech.model;

import java.time.LocalDateTime;

public class Embarcado {
    private Integer idEmbarcado;
    private Integer empresaId;
    private Integer enderecoId;
    private String enderecoMac;
    private String apelido;
    private String Modelo;
    private String status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Embarcado() {}

    public Embarcado(Integer idEmbarcado, Integer empresaId, Integer enderecoId, String enderecoMac, String apelido, String modelo, String status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.idEmbarcado = idEmbarcado;
        this.empresaId = empresaId;
        this.enderecoId = enderecoId;
        this.enderecoMac = enderecoMac;
        this.apelido = apelido;
        Modelo = modelo;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Integer getIdEmbarcado() {
        return idEmbarcado;
    }

    public void setIdEmbarcado(Integer idEmbarcado) {
        this.idEmbarcado = idEmbarcado;
    }

    public Integer getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Integer empresaId) {
        this.empresaId = empresaId;
    }

    public Integer getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Integer enderecoId) {
        this.enderecoId = enderecoId;
    }

    public String getEnderecoMac() {
        return enderecoMac;
    }

    public void setEnderecoMac(String enderecoMac) {
        this.enderecoMac = enderecoMac;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "Embarcado{" +
                "idEmbarcado=" + idEmbarcado +
                ", empresaId=" + empresaId +
                ", enderecoId=" + enderecoId +
                ", enderecoMac='" + enderecoMac + '\'' +
                ", apelido='" + apelido + '\'' +
                ", Modelo='" + Modelo + '\'' +
                ", status='" + status + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}

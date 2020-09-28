package br.com.patrickwm.flash.entity;

import javax.enterprise.inject.Model;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Model
@Entity
@NamedQueries({
    @NamedQuery(name = Encomenda.FIND_BY_CODIGO_RASTREIO, query = "SELECT e FROM Encomenda e WHERE e.codigoRastreio = :codigoRastreio")
})
public class Encomenda {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "codigo_rastreio", nullable = false)
    private String codigoRastreio;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEncomenda status;
    @Column(name = "data_hora_postagem")
    private LocalDateTime dataHoraPostagem;
    @Column(name = "cidade_origem")
    private String cidadeOrigem;
    @Column(name = "cidade_destino")
    private String cidadeDestino;
    @OneToMany(mappedBy = "encomenda")
    private List<DetalheRastreio> detalhes;

    public static final String FIND_BY_CODIGO_RASTREIO = "Encomenda.findByCodigoRastreio";

    public Encomenda() {
    }

    public Encomenda(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    public Long getId() {
        return id;
    }

    public void setCodigoRastreio(String codigoRastreio) {
        this.codigoRastreio = codigoRastreio;
    }

    public String getCodigoRastreio() {
        return codigoRastreio;
    }

    public StatusEncomenda getStatus() {
        return status;
    }

    public LocalDateTime getDataHoraPostagem() {
        return dataHoraPostagem;
    }

    public String getCidadeOrigem() {
        return cidadeOrigem;
    }

    public String getCidadeDestino() {
        return cidadeDestino;
    }
}

package br.com.patrickwm.flash.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "detalhes_rastreio")
@NamedQueries({
    @NamedQuery(name = DetalheRastreio.FIND_BY_ENCOMENDA, query = "SELECT d FROM DetalheRastreio d JOIN d.encomenda e WHERE e.id = :idEncomenda")
})
public class DetalheRastreio {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "encomenda_id")
    private Encomenda encomenda;
    private String descricao;
    @Column(name = "data_hora")
    private LocalDateTime dataHora;
    @Column(name = "cidade_origem")
    private String cidadeOrigem;
    @Column(name = "cidade_destino")
    private String cidadeDestino;

    public final static String FIND_BY_ENCOMENDA = "DetalheRastreio.findByEncomenda";

    public DetalheRastreio() {
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCidadeOrigem() {
        return cidadeOrigem;
    }

    public String getCidadeDestino() {
        return cidadeDestino;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }
}

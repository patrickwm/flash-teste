package br.com.patrickwm.flash.services;

import br.com.patrickwm.flash.entity.DetalheRastreio;
import br.com.patrickwm.flash.entity.Encomenda;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class EncomendaService {
    @PersistenceContext
    EntityManager em;

    public List<Encomenda> findByCodigoRastreio(Encomenda encomenda) {
        return em.createNamedQuery(Encomenda.FIND_BY_CODIGO_RASTREIO, Encomenda.class)
            .setParameter("codigoRastreio", encomenda.getCodigoRastreio())
            .getResultList();
    }

    public List<DetalheRastreio> findDetalhesById(Encomenda encomenda) {
        return em.createNamedQuery(DetalheRastreio.FIND_BY_ENCOMENDA, DetalheRastreio.class)
            .setParameter("idEncomenda", encomenda.getId())
            .getResultList();
    }

}

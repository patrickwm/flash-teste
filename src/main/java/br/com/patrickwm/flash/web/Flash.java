package br.com.patrickwm.flash.web;

import br.com.patrickwm.flash.FlashUI;
import br.com.patrickwm.flash.entity.DetalheRastreio;
import br.com.patrickwm.flash.entity.Encomenda;
import br.com.patrickwm.flash.services.EncomendaService;
import br.com.patrickwm.flash.ui.ActionsBarGenerator;
import br.com.patrickwm.flash.ui.Dialog;
import br.com.patrickwm.flash.util.LocalDateTimeFormatter;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public abstract class Flash extends UI {
    private Grid<Encomenda> encomendaGrid;
    protected VerticalLayout layout;

    @Inject
    protected EncomendaService encomendaService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        layout = new VerticalLayout();

        var logo = new Embedded(null, new ExternalResource("https://jall.com.br/web/imagens/login.png"));

        montaGrid();

        layout.addComponents(logo);
        layout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);

        setContent(layout);
    }

    protected void mudarVisibilidadeGrid(boolean visible) {
        if (visible) {
            layout.addComponent(encomendaGrid);
            layout.setComponentAlignment(encomendaGrid, Alignment.MIDDLE_CENTER);
        } else {
            layout.removeComponent(encomendaGrid);
        }
    }

    private void montaGrid() {
        encomendaGrid = new Grid<>();
        encomendaGrid.setWidth(75, Unit.PERCENTAGE);
        encomendaGrid.addColumn(e -> LocalDateTimeFormatter.toDDMMYYYY_HHMM(e.getDataHoraPostagem())).setWidth(200).setCaption("Horário Postagem");
        encomendaGrid.addColumn(Encomenda::getCodigoRastreio).setWidth(150).setCaption("Codigo Rastreio");
        encomendaGrid.addColumn(Encomenda::getStatus).setCaption("Status");
        encomendaGrid.addColumn(Encomenda::getCidadeOrigem).setCaption("Origem");
        encomendaGrid.addColumn(Encomenda::getCidadeDestino).setCaption("Destino");
        var actions = new ActionsBarGenerator<Encomenda>();
        actions.addAction(VaadinIcons.SEARCH, "Visualizar Detalhes", this::abrirDialogDetalhes);
        encomendaGrid.addColumn(actions::getComponent, new ComponentRenderer()).setCaption("Opções").setWidth(150);
    }

    protected void verificarEncomendas(List<Encomenda> encomendas) throws ValidationException {
        if (encomendas.isEmpty()) {
            showNotificacaoWarning("Código de rastreio não encontrado");
            return;
        }

        if (encomendas.size() == 1) {
            abrirDialogDetalhes(encomendas.get(0));
            mudarVisibilidadeGrid(false);
            encomendaGrid.setItems(new ArrayList<>());
        } else {
            encomendaGrid.setItems(encomendas);
            mudarVisibilidadeGrid(true);
        }
    }

    protected void abrirDialogDetalhes(Encomenda encomenda) {
        var detalhesDialog = new DetalhesDialog(encomenda);
        detalhesDialog.show();
    }

    protected void showNotificacaoWarning(String texto) {
        Notification.show(texto, Notification.Type.WARNING_MESSAGE);
    }

    protected class DetalhesDialog extends Dialog {
        private final Grid<DetalheRastreio> detalheRastreioGrid;
        private static final long serialVersionUID = 1L;
        private final Encomenda encomenda;


        public DetalhesDialog(Encomenda encomenda) {
            this.setCaption("Detalhes do envio: " + encomenda.getCodigoRastreio());
            this.encomenda = encomenda;
            var mainLayout = new VerticalLayout();
            mainLayout.setWidth(1000, Unit.PIXELS);
            mainLayout.setMargin(true);
            mainLayout.setSpacing(true);
            this.setContent(mainLayout);

            detalheRastreioGrid = new Grid<>();
            detalheRastreioGrid.setSizeFull();
            detalheRastreioGrid.setSelectionMode(Grid.SelectionMode.NONE);
            montarColunasGrid();
            mainLayout.addComponent(detalheRastreioGrid);
            buscaDetalhesRastreio();
        }

        private void buscaDetalhesRastreio() {
            var detalhes = encomendaService.findDetalhesById(encomenda);
            detalheRastreioGrid.setItems(detalhes);
        }

        private void montarColunasGrid() {
            detalheRastreioGrid.addColumn(d -> LocalDateTimeFormatter.toDDMMYYYY_HHMM(d.getDataHora())).setCaption("Data").setWidth(175);
            detalheRastreioGrid.addColumn(d -> d.getEncomenda().getCodigoRastreio()).setCaption("Código Rastreio").setWidth(150);
            detalheRastreioGrid.addColumn(DetalheRastreio::getDescricao).setCaption("Movimentação").setWidth(348);
            detalheRastreioGrid.addColumn(DetalheRastreio::getCidadeOrigem).setCaption("Cidade Origem").setWidth(150);
            detalheRastreioGrid.addColumn(DetalheRastreio::getCidadeDestino).setCaption("Cidade Destino").setWidth(150);
        }
    }
}

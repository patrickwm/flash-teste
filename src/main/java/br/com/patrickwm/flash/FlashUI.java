package br.com.patrickwm.flash;

import br.com.patrickwm.flash.entity.DetalheRastreio;
import br.com.patrickwm.flash.entity.Encomenda;
import br.com.patrickwm.flash.services.EncomendaService;
import br.com.patrickwm.flash.ui.ActionsBarGenerator;
import br.com.patrickwm.flash.ui.Dialog;
import br.com.patrickwm.flash.util.LocalDateTimeFormatter;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.cdi.CDIUI;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Title("Flash :: Teste")
@Theme("mytheme")
@CDIUI("")
public class FlashUI extends UI {
    private TextField txCodigoRastreio;
    private Binder<Encomenda> binder;
    private Grid<Encomenda> encomendaGrid;
    private VerticalLayout layout;

    @Inject
    private Encomenda encomenda;

    @Inject
    EncomendaService encomendaService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        layout = new VerticalLayout();

        var logo = new Embedded(null, new ExternalResource("https://jall.com.br/web/imagens/login.png"));

        txCodigoRastreio = new TextField();
        txCodigoRastreio.setCaption("Digite o código de rastreio:");
        initBinder();

        var button = new Button("Pesquisar");
        button.addClickListener(e -> {
            try {
                pesquisar();
            } catch (ValidationException validationException) {
                showNotificacaoWarning("Estamos com problemas internos, tente novamente mais tarde");
            }
        });

        montaGrid();

        layout.addComponents(logo, txCodigoRastreio, button);
        layout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(txCodigoRastreio, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);

        setContent(layout);
    }

    private void mudarVisibilidadeGrid(boolean visible) {
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

    private void initBinder() {
        binder = new Binder<>(Encomenda.class);

        binder.forField(txCodigoRastreio)
            .asRequired()
            .bind(Encomenda::getCodigoRastreio, Encomenda::setCodigoRastreio);

        binder.readBean(encomenda);
    }

    private void pesquisar() throws ValidationException {
        if (binder.isValid()) {
            verificarEncomendas();
        } else {
            showNotificacaoWarning("Digite uma AR válida");
        }
    }

    private void verificarEncomendas() throws ValidationException {
        binder.writeBean(encomenda);
        var encomendas = encomendaService.findByCodigoRastreio(encomenda);
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

    private void abrirDialogDetalhes(Encomenda encomenda) {
        var detalhesDialog = new DetalhesDialog(encomenda);
        detalhesDialog.show();
    }

    private void showNotificacaoWarning(String texto) {
        Notification.show(texto, Notification.Type.WARNING_MESSAGE);
    }

    private class DetalhesDialog extends Dialog {
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

package br.com.patrickwm.flash;

import br.com.patrickwm.flash.entity.DetalheRastreio;
import br.com.patrickwm.flash.entity.Encomenda;
import br.com.patrickwm.flash.services.EncomendaService;
import br.com.patrickwm.flash.ui.ActionsBarGenerator;
import br.com.patrickwm.flash.ui.Dialog;
import br.com.patrickwm.flash.util.LocalDateTimeFormatter;
import br.com.patrickwm.flash.web.Flash;
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
public class FlashUI extends Flash {
    private TextField txCodigoRastreio;
    private Binder<Encomenda> binder;

    @Inject
    private Encomenda encomenda;

    @Inject
    EncomendaService encomendaService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        super.init(vaadinRequest);

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

        layout.addComponents(txCodigoRastreio, button);
        layout.setComponentAlignment(txCodigoRastreio, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(button, Alignment.MIDDLE_CENTER);
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
            binder.writeBean(encomenda);
            var encomendas = encomendaService.findByCodigoRastreio(encomenda);
            verificarEncomendas(encomendas);
        } else {
            showNotificacaoWarning("Digite uma AR válida");
        }
    }
}

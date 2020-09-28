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
import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Title("Flash :: Detalhes - Teste")
@Theme("mytheme")
@CDIUI("Detalhe")
public class FlashDetalheUI extends Flash {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        super.init(vaadinRequest);
        var codigo = (String) vaadinRequest.getParameter("codigo");
        var encomendas = encomendaService.findByCodigoRastreio(new Encomenda(codigo));
        try {
            verificarEncomendas(encomendas);
        } catch (Exception e) {
            var label = new Label(e.getMessage());
            layout.addComponent(label);
            layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        }
    }
}

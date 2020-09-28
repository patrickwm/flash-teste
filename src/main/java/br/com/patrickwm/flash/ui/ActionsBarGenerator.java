package br.com.patrickwm.flash.ui;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionsBarGenerator<T> {
    private Map<VaadinIcons, Action> actions = new LinkedHashMap<>();

    public ActionsBarGenerator<T> addAction(VaadinIcons icon, String description, Consumer<T> actionFunction) {
        return addAction(icon, description, actionFunction, e -> true);
    }

    public ActionsBarGenerator<T> addAction(VaadinIcons icon, String description, Consumer<T> actionFunction, Predicate<T> filter) {
        actions.put(icon, new ClickAction(icon, description, actionFunction, filter));

        return this;
    }

    public Component getComponent(T item) {
        HorizontalLayout mainLayout = new HorizontalLayout();

        actions.values().stream()
            .filter(a -> a.isVisibleFor(item))
            .forEach(a -> mainLayout.addComponent(a.generateButton(item)));

        return mainLayout;
    }

    private abstract class Action {

        private VaadinIcons icon;
        private String description;
        private Predicate<T> filter;

        public Action(VaadinIcons icon, String description, Predicate<T> filter) {
            this.icon = icon;
            this.description = description;
            this.filter = filter;
        }

        public boolean isVisibleFor(T item) {
            return filter.test(item);
        }

        public abstract Component generateButton(T item);

        protected VaadinIcons getIcon() {
            return icon;
        }

        protected String getDescription() {
            return description;
        }
    }

    private class ClickAction extends Action {

        private Consumer<T> actionFunction;

        private ClickAction(VaadinIcons icon, String description, Consumer<T> actionFunction, Predicate<T> filter) {
            super(icon, description, filter);
            this.actionFunction = actionFunction;
        }

        public Button generateButton(T item) {
            Button b = new Button(getIcon());
            b.setDescription(getDescription());
            b.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.BUTTON_BORDERLESS_COLORED);
            b.addClickListener(e -> actionFunction.accept(item));

            return b;
        }

    }
}

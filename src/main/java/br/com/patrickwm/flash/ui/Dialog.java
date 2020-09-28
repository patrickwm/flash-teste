package br.com.patrickwm.flash.ui;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public abstract class Dialog extends Window {
	private static final long serialVersionUID = -8953587458458098089L;

	public Dialog() {
		super();
		initDialog();
	}

	public Dialog(String caption) {
		super(caption);
		initDialog();
	}

	private void initDialog() {
		this.setModal(true);
		this.center();
		this.setResizable(false);
	}

	public void show() {
		UI.getCurrent().addWindow(this);
	}
}

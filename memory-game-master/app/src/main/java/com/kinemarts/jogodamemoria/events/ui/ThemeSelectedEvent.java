package com.kinemarts.jogodamemoria.events.ui;

import com.kinemarts.jogodamemoria.events.AbstractEvent;
import com.kinemarts.jogodamemoria.events.EventObserver;
import com.kinemarts.jogodamemoria.themes.Theme;

public class ThemeSelectedEvent extends AbstractEvent {

	public static final String TYPE = ThemeSelectedEvent.class.getName();
	public final Theme theme;

	public ThemeSelectedEvent(Theme theme) {
		this.theme = theme;
	}

	@Override
	protected void fire(EventObserver eventObserver) {
		eventObserver.onEvent(this);
	}

	@Override
	public String getType() {
		return TYPE;
	}

}

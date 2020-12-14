package com.kinemarts.jogodamemoria.events.ui;

import com.kinemarts.jogodamemoria.events.AbstractEvent;
import com.kinemarts.jogodamemoria.events.EventObserver;

/**
 * When the 'back to menu' was pressed.
 */
public class ResetBackgroundEvent extends AbstractEvent {

	public static final String TYPE = ResetBackgroundEvent.class.getName();

	@Override
	protected void fire(EventObserver eventObserver) {
		eventObserver.onEvent(this);
	}

	@Override
	public String getType() {
		return TYPE;
	}

}

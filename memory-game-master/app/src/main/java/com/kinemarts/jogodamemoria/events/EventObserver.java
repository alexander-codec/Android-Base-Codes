package com.kinemarts.jogodamemoria.events;

import com.kinemarts.jogodamemoria.events.engine.FlipDownCardsEvent;
import com.kinemarts.jogodamemoria.events.engine.GameWonEvent;
import com.kinemarts.jogodamemoria.events.engine.HidePairCardsEvent;
import com.kinemarts.jogodamemoria.events.ui.BackGameEvent;
import com.kinemarts.jogodamemoria.events.ui.DifficultySelectedEvent;
import com.kinemarts.jogodamemoria.events.ui.FlipCardEvent;
import com.kinemarts.jogodamemoria.events.ui.NextGameEvent;
import com.kinemarts.jogodamemoria.events.ui.ResetBackgroundEvent;
import com.kinemarts.jogodamemoria.events.ui.StartEvent;
import com.kinemarts.jogodamemoria.events.ui.ThemeSelectedEvent;


public interface EventObserver {

	void onEvent(FlipCardEvent event);

	void onEvent(DifficultySelectedEvent event);

	void onEvent(HidePairCardsEvent event);

	void onEvent(FlipDownCardsEvent event);

	void onEvent(StartEvent event);

	void onEvent(ThemeSelectedEvent event);

	void onEvent(GameWonEvent event);

	void onEvent(BackGameEvent event);

	void onEvent(NextGameEvent event);

	void onEvent(ResetBackgroundEvent event);

}

package com.kinemarts.jogodamemoria.fragments;

import android.support.v4.app.Fragment;

import com.kinemarts.jogodamemoria.events.EventObserver;
import com.kinemarts.jogodamemoria.events.engine.FlipDownCardsEvent;
import com.kinemarts.jogodamemoria.events.engine.GameWonEvent;
import com.kinemarts.jogodamemoria.events.engine.HidePairCardsEvent;
import com.kinemarts.jogodamemoria.events.ui.BackGameEvent;
import com.kinemarts.jogodamemoria.events.ui.FlipCardEvent;
import com.kinemarts.jogodamemoria.events.ui.NextGameEvent;
import com.kinemarts.jogodamemoria.events.ui.ResetBackgroundEvent;
import com.kinemarts.jogodamemoria.events.ui.ThemeSelectedEvent;
import com.kinemarts.jogodamemoria.events.ui.DifficultySelectedEvent;
import com.kinemarts.jogodamemoria.events.ui.StartEvent;

public class BaseFragment extends Fragment implements EventObserver {

	@Override
	public void onEvent(FlipCardEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(DifficultySelectedEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(HidePairCardsEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(FlipDownCardsEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(StartEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(ThemeSelectedEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(GameWonEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(BackGameEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(NextGameEvent event) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onEvent(ResetBackgroundEvent event) {
		throw new UnsupportedOperationException();
	}

}

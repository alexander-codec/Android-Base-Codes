package com.kinemarts.modern.music.ui.local.all;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import com.kinemarts.modern.music.data.model.Song;
import com.kinemarts.modern.music.ui.base.BasePresenter;
import com.kinemarts.modern.music.ui.base.BaseView;

import java.util.List;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/13/16
 * Time: 8:32 PM
 * Desc: LocalMusicContract
 */
/* package */ interface LocalMusicContract {

    interface View extends BaseView<Presenter> {

        LoaderManager getLoaderManager();

        Context getContext();

        void showProgress();

        void hideProgress();

        void emptyView(boolean visible);

        void handleError(Throwable error);

        void onLocalMusicLoaded(List<Song> songs);
    }

    interface Presenter extends BasePresenter {

        void loadLocalMusic();
    }
}

package com.liwy.happy.controllers.joke.gifjoke;


import com.liwy.easylibrary.base.view.IView;
import com.liwy.happy.adapter.ImgJokeAdapter;
import com.liwy.happy.entity.Joke;

public interface GifJokeView extends IView {
    public void updateNext(Joke joke);
}

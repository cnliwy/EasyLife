package com.liwy.happy.controllers.joke.textjoke;


import com.liwy.easylibrary.base.view.IView;
import com.liwy.happy.adapter.TextJokeAdapter;

public interface TextJokeView extends IView {
    public void setAdapter(TextJokeAdapter adapter);
    public void finishRefresh();
}

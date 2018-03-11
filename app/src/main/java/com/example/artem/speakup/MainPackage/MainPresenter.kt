package com.example.artem.speakup.MainPackage

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView

@InjectViewState
class MainPresenter: MvpPresenter<MainPresenter.MainPresenterInterface>() {
    init {
        presenterLaunch()
    }

    fun presenterLaunch() {
        viewState.updateTabs()
    }

    interface MainPresenterInterface: MvpView {
        fun updateTabs()
    }
}


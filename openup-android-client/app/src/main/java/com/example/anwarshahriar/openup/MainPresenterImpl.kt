package com.example.anwarshahriar.openup

class MainPresenterImpl : MainPresenter {
    private var view: MainView? = null

    override fun addNewDevice() {
        view?.openQRScanner()
    }

    override fun setView(view: MainView) {
        this.view = view
    }

    override fun clearView() {
        view = null
    }

}

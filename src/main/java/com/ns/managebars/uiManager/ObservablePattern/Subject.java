package com.ns.managebars.uiManager.ObservablePattern;

public interface Subject {

    void registerObserver(Observer o,int index);
    void removeObserver(Observer o);
    void notifyObservers();


}
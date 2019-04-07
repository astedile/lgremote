/*
 * Copyright 2019 Alexander Stedile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package astedile.lgremote.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Workaround since sub-classes can not extend both, JPanel and Observable.
 */
abstract class ObservablePanel extends JPanel implements GuiObservable {
    private AccessibleObservable observable = new AccessibleObservable();

    ObservablePanel() {
    }

    ObservablePanel(LayoutManager layout) {
        super(layout);
    }

    @Override
    public void addObserver(Observer o) {
        observable.addObserver(o);
    }

    void deleteObserver(Observer o) {
        observable.deleteObserver(o);
    }

    int countObservers() {
        return observable.countObservers();
    }

    void notifyObservers() {
        observable.notifyObservers();
    }

    void notifyObservers(Object arg) {
        observable.notifyObservers(arg);
    }

    boolean hasChanged() {
        return observable.hasChanged();
    }

    void setChanged() {
        observable.setChanged();
    }

    void clearChanged() {
        observable.clearChanged();
    }

    /**
     * {@link Observable} with methods {@link #setChanged()} and {@link #clearChanged()}
     * made accessible.
     */
    private static class AccessibleObservable extends Observable {
        @Override
        protected synchronized void setChanged() {
            super.setChanged();
        }

        @Override
        protected synchronized void clearChanged() {
            super.clearChanged();
        }
    }
}

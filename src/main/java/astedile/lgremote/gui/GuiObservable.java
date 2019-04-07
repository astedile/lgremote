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

import java.util.Observer;

/**
 * Replacement for java.util.{@link java.util.Observable} which is a class instead of an interface.
 * The observables in this package must extend e.g. {@link javax.swing.JPanel} and cannot extend {@link java.util.Observable}.
 */
interface GuiObservable {
    void addObserver(Observer o);
}

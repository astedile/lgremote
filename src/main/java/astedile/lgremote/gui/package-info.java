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

/**
 * Swing GUI, see {@link astedile.lgremote.gui.Gui} and {@link astedile.lgremote.gui.GuiController}.
 * The observer pattern for notification from GUI components to the controller is implemented by
 * {@link java.util.Observer}, {@link astedile.lgremote.gui.GuiObservable},
 * and {@link astedile.lgremote.gui.ObservablePanel}.
 */
package astedile.lgremote.gui;
/*
 *  Copyright (C) 2013 lee
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lee.echo360.control.controllers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.lee.echo360.control.ControllerAction;
import org.lee.echo360.control.ControllerListener;
import org.lee.echo360.model.ActionResult;

/**
 *
 * @author lee
 */
public class DefaultControllerLinker implements ControllerListener {

    private final ExecutorService runService = Executors.newSingleThreadExecutor();
    private final MainController c;

    public DefaultControllerLinker(MainController c) {
        this.c = c;
    }

    public void commenceLogin() {
        runService.submit(new Runnable() {
            @Override
            public void run() {
                c.getLoginController().doLogin();
            }
        });
    }

    @Override
    public void start(ControllerAction action) {
        // NO Op
    }

    @Override
    public void finished(final ControllerAction action, final ActionResult ar) {
        if (ar.isSuccess()) {
            runService.submit(new Runnable() {
                @Override
                public void run() {
                    switch (action) {
                        case LOGIN:
                            c.getSubjectController().getSubjects();
                            break;
                        case COURSES:
                            c.getLectureController().getAllLectures();
                            break;
                        default:
                    }
                }
            });
        }
    }

    @Override
    public void error(Throwable thrwbl) {
    }
}

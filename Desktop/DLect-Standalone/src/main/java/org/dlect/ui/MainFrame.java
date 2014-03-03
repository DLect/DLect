/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import org.dlect.controller.event.ControllerEvent;
import org.dlect.controller.event.ControllerState;
import org.dlect.controller.event.ControllerType;
import org.dlect.controller.helper.Controller;
import org.dlect.controller.worker.ErrorDisplayable;
import org.dlect.controller.worker.SubjectWorker;
import org.dlect.events.Event;
import org.dlect.events.EventListener;
import org.dlect.events.wrapper.Wrappers;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.ui.CoursesScreen;
import org.dlect.ui.decorator.BusyPainterUI;
import org.dlect.ui.heler.LayerUIUtil;
import org.dlect.ui.login.LoginPanel;
import org.jdesktop.jxlayer.JXLayer;
import org.dlect.controller.GUIController;

/**
 *
 * java.util.ResourceBundle.getBundle("org/lee/echo360/ui/ui").getString("NEXT")
 * internationalization
 *
 * @author lee
 */
public class MainFrame extends javax.swing.JFrame implements EventListener, ErrorDisplayable {

    private JXLayer<JComponent> loginPanelLayer;
    private BusyPainterUI loginBusyUI;
    private CoursesScreen courseScreen;
    private BusyPainterUI courseBusyUI;
    private JXLayer<JComponent> coursePanel;
    private final GUIController controller;
    private LoginPanel loginPanel;

    /**
     * Creates new form MainFrame
     *
     * @param c
     */
    public MainFrame(GUIController c) {
        controller = c;
        initComponents();
        initLayeredPane();
        Wrappers.addSwingListenerTo(this, c, Controller.class);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panel = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/dlect/ui/ui"); // NOI18N
        setTitle(bundle.getString("TITLE")); // NOI18N
        setMinimumSize(new java.awt.Dimension(450, 300));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        controller.closeApplication();
    }//GEN-LAST:event_formWindowClosing
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane panel;
    // End of variables declaration//GEN-END:variables

    private void addLoginPanel() {
        if (loginPanelLayer == null) {
            loginPanel = new LoginPanel(controller) {
                private static final long serialVersionUID = 1L;

                @Override
                public void cancel() {
                    controller.closeApplication();
                }

                @Override
                public void complete() {
                }
            };
            loginBusyUI = createBusyPainter(false);
            loginPanelLayer = new JXLayer<>(loginPanel, loginBusyUI);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1;
            c.weighty = 1;
            panel.add(loginPanelLayer, c);
            panel.setLayer(loginPanelLayer, 100);
        }
    }

    private BusyPainterUI createBusyPainter(boolean allowConfetti) {
        BusyPainterUI painterUi = new BusyPainterUI(allowConfetti);
        painterUi.setLockedEffects(LayerUIUtil.blur());
        return painterUi;
    }

    private void initLayeredPane() {
        panel.setLayout(new GridBagLayout());
        addLoginPanel();
        addCoursePanel();
    }

    private void addCoursePanel() {
        if (coursePanel == null) {
            courseScreen = new CoursesScreen(controller);
            courseBusyUI = createBusyPainter(true);
            coursePanel = new JXLayer<>(courseScreen, courseBusyUI);
            courseBusyUI.setLocked(true);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1;
            c.weighty = 1;
            c.fill = GridBagConstraints.BOTH;
            panel.add(coursePanel, c);
            panel.setLayer(coursePanel, 50);
        }
    }

    public void setCoursesLocked(boolean b) {
        courseBusyUI.setLocked(b);
    }

    public void setLoginLocked(boolean b) {
        loginBusyUI.setLocked(b);
    }

    public void setLoginVisible(boolean visible) {
        if (visible) {
            panel.setLayer(loginPanelLayer, 100);
            courseBusyUI.setLocked(true);
        } else {
            panel.setLayer(loginPanelLayer, 20);
        }
    }

    public CoursesScreen getCourseScreen() {
        return courseScreen;
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

//
//    @Override
//    public void start(final ControllerAction action) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                switch (action) {
//                    case STARTUP:
//                        setLoginVisible(true);
//                        setLoginLocked(true);
//                        setCoursesLocked(true);
//                        break;
//                    case LOGIN:
//                        final PropertiesController pc = controller.getPropertiesController();
//                        loginPanel.setUsername(pc.getBlackboard().getUsername());
//                        loginPanel.setPassword(pc.getBlackboard().getPassword());
//                        loginPanel.setProvider(pc.getProvider());
//                        setLoginVisible(true);
//                        setLoginLocked(true);
//                        setCoursesLocked(true);
//                        break;
//                    case COURSES:
//                        setLoginVisible(false);
//                        setLoginLocked(false);
//                        setCoursesLocked(true);
//                        break;
//                    case LECTURES:
//                        setLoginVisible(false);
//                        setLoginLocked(false);
//                        setCoursesLocked(false);
//                        break;
//                    default:
//                }
//            }
//        });
//    }
//
//    private int length(String nullableString) {
//        return (nullableString == null ? 0 : nullableString.length());
//    }
//
//    @Override
//    public void finished(final ControllerAction action, final ActionResult r) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                PropertiesController c = controller.getPropertiesController();
//                final Blackboard b = c.getBlackboard();
//                switch (action) {
//                    case STARTUP:
//                        setLoginVisible(true);
//                        setLoginLocked(false);
//                        setCoursesLocked(true);
//                        if (c.getBlackboard() != null) {
//                            loginPanel.setUsername(b.getUsername());
//                            loginPanel.setPassword(b.getPassword());
//                        }
//                        if (c.getProvider() != null) {
//                            loginPanel.setProvider(c.getProvider());
//                            if (length(b.getUsername()) * length(b.getPassword()) > 0) {
//                                setLoginLocked(true);
//                                loginPanel.doLogin();
//                            }
//                        }
//                        break;
//                    case LOGIN:
//                        setCoursesLocked(true);
//                        if (r == ActionResult.SUCCEDED) {
//                            setLoginVisible(false);
//                            repaint();
//                        } else {
//                            setLoginVisible(true);
//                        }
//                        setLoginLocked(false);
//                        break;
//                    case COURSES:
//                        setLoginVisible(false);
//                        setLoginLocked(false);
//                        setCoursesLocked(false);
//                        break;
//                    case LECTURES:
//                        setLoginVisible(false);
//                        setLoginLocked(false);
//                        setCoursesLocked(false);
//                        break;
//                    default:
//                }
//            }
//        });
//    }
//
//    @Override
//    public void error(Throwable thrwbl) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                if (URLUtil.isOffline()) {
//                    loginPanel.showErrorBox(ActionResult.NOT_CONNECTED);
//                } else {
//                    loginPanel.showErrorBox(ActionResult.FAILED);
//                }
//                setLoginVisible(true);
//                setLoginLocked(false);
//            }
//        });
//    }
    public void initLoginPanel() {
        // TODO init login panel's username/password/provider.

    }

    @Override
    public void processEvent(Event e) {
        if (e instanceof ControllerEvent) {
            ControllerEvent ce = (ControllerEvent) e;
            switch (ce.getEventID()) {
                case LOGIN:
                    processLogin(ce.getAfter());
                    break;
                case LECTURE:
                    break;
                case SUBJECT:
                    break;
            }

        }
    }

    protected void processLogin(ControllerState state) {
        setLoginVisible(true);
        switch (state) {
            case STARTED:
                setLoginLocked(true);
                setCoursesLocked(true);
                break;
            case COMPLETED:
                setLoginVisible(false);
                new SubjectWorker(this, controller).execute();

            case FAILED:
                setCoursesLocked(true);
                setLoginLocked(false);
        }
    }

    @Override
    public void showErrorBox(ControllerType type, Object parameter, DLectExceptionCause get) {

    }
}

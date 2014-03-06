package org.dlect.ui;

import org.dlect.controller.MainController;
import org.dlect.model.Subject;
import org.dlect.ui.layout.AnimatedHideLayoutManager;
import org.dlect.ui.panel.CourseDetailPanel;
import org.dlect.ui.panel.CourseHeader;

/**
 *
 * @author lee
 */
public class CoursePane extends javax.swing.JLayeredPane {

    private static final long serialVersionUID = 1L;

    private Subject subject;
    private final MainController controller;

    /**
     * Creates new form CoursePane
     *
     * @param controller
     */
    public CoursePane(MainController controller) {
        this.controller = controller;
        initComponents();
    }

    public void animateDown() {
        lm.animateDown();
    }

    public void animateUp() {
        lm.animateUp();
    }

    public void animateToggle() {
        lm.animateToggle();
    }

    public void setSubject(Subject c) {
        this.subject = c;
        courseDetailPanel1.setSubject(subject);
        courseHeader1.setSubject(subject);
    }

    private void initComponents() {
        courseHeader1 = new CourseHeader(controller);
        courseDetailPanel1 = new CourseDetailPanel(controller);

        lm = new AnimatedHideLayoutManager(courseHeader1, courseDetailPanel1, this);

        courseHeader1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                animateToggle();
            }
        });

        this.setLayout(lm);
        this.add(courseHeader1);
        this.add(courseDetailPanel1);
    }

    // Variables declaration - do not modify
    private AnimatedHideLayoutManager lm;
    private CourseDetailPanel courseDetailPanel1;
    private CourseHeader courseHeader1;

    // End of variables declaration
    public void loadLectures() {
        courseDetailPanel1.loadLectures();
    }
}

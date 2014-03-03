package org.dlect.ui;

import org.dlect.controller.MainController;
import org.dlect.model.Subject;
import org.dlect.ui.panel.CourseDetailPanel;
import org.dlect.ui.panel.CourseHeader;
import org.dlect.ui.layout.AnimatedHideLayoutManager;

/**
 *
 * @author lee
 */
public class CoursePane extends javax.swing.JLayeredPane {

    private static final long serialVersionUID = 1L;

    private Subject course;
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
        this.course = c;
        courseDetailPanel1.setSubject(course);
        courseHeader1.setSubject(course);
    }

    private void initComponents() {
        courseHeader1 = new CourseHeader(controller);
        courseDetailPanel1 = new CourseDetailPanel(controller);

        lm = new AnimatedHideLayoutManager(courseHeader1, courseDetailPanel1, this);

        courseHeader1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lm.animateToggle();
            }
        });

        this.setLayout(lm);
    }

    // Variables declaration - do not modify
    private AnimatedHideLayoutManager lm;
    private CourseDetailPanel courseDetailPanel1;
    private CourseHeader courseHeader1;
    // End of variables declaration
}

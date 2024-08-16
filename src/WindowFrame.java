import java.awt.*;
import javax.swing.*;

public class WindowFrame extends JFrame {

    private static final long serialVersionUID = -4280772482528512323L;
    private JPanel container;
    private GridPanel gridPanel;
    private ControlsPanel controlsPanel;
    private JMenuBar menuBar;
    private JLabel statusBar;

    public static final int INITIAL_WIDTH = 800;
    public static final int INITIAL_HEIGHT = 600;

    public WindowFrame() {
        setupFrame();
        setupMenuBar();
        setupComponents();
        setupLayout();
        setupStatusBar();
        finalizeSetup();
    }

    private void setupFrame() {
        setTitle("Path Finding Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));
        setMinimumSize(new Dimension(600, 400));
    }

    private void setupMenuBar() {
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");

        fileMenu.add(new JMenuItem("New"));
        fileMenu.add(new JMenuItem("Save"));
        fileMenu.add(new JMenuItem("Exit"));

        editMenu.add(new JMenuItem("Undo"));
        editMenu.add(new JMenuItem("Redo"));

        JCheckBoxMenuItem darkModeItem = new JCheckBoxMenuItem("Dark Mode");
        darkModeItem.addActionListener(e -> toggleDarkMode());
        viewMenu.add(darkModeItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
    }

    private void setupComponents() {
        container = new JPanel();
        Grid grid = new Grid(calculateRows(), calculateCols());
        gridPanel = new GridPanel(INITIAL_WIDTH - 200, INITIAL_HEIGHT, grid);
        controlsPanel = new ControlsPanel(200, INITIAL_HEIGHT, gridPanel);
    }

    private void setupLayout() {
        container.setLayout(new BorderLayout());
        container.add(controlsPanel, BorderLayout.WEST);
        container.add(gridPanel, BorderLayout.CENTER);
        setContentPane(container);
    }

    private void setupStatusBar() {
        statusBar = new JLabel("Ready");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        container.add(statusBar, BorderLayout.SOUTH);
    }

    private void finalizeSetup() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private int calculateRows() {
        return (int) Math.floor(INITIAL_HEIGHT / Node.size);
    }

    private int calculateCols() {
        return (int) Math.floor((INITIAL_WIDTH - 200) / Node.size);
    }

    private void toggleDarkMode() {
        // Implement dark mode logic here
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        updateGridSize();
    }

    private void updateGridSize() {
		if (gridPanel != null) {
			int newSize = Math.min(
				(getHeight() - statusBar.getHeight()) / calculateRows(),
				(getWidth() - 200) / calculateCols()
			);
			gridPanel.updateGrid(newSize);
			controlsPanel.setSize(200, getHeight());
			revalidate();
			repaint();
		}
	}

    // Keyboard shortcuts
    
}
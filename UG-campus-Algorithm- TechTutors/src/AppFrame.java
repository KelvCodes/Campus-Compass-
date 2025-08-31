import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class AppFrame extends JFrame implements ActionListener {
    private JLabel startLabel;
    private JLabel stopLabel;
    private JLabel routeLabel; // kept to match your class fields
    private JButton findRouteButton;
    private JButton clearButton;
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel resultPanel;
    private JComboBox<String> startComboBox;
    private JComboBox<String> endComboBox;
    private JTextArea resultArea;
    private JScrollPane scrollPane;
    private Graph graph;
    private Map<String, Nodes> locationNodes;

    public AppFrame() {
        applyDarkThemeUIDefaults(); // design-only change
        initializeGraph();
        setupUI();
    }

    /** ---- DESIGN: Apply dark theme defaults (no logic change) ---- */
    private void applyDarkThemeUIDefaults() {
        // Global dark palette
        Color bg = new Color(18, 18, 18);
        Color panel = new Color(22, 22, 22);
        Color surface = new Color(28, 28, 28);
        Color text = new Color(224, 224, 224);
        Color subtle = new Color(160, 160, 160);
        Color border = new Color(56, 56, 56);

        // Common UI defaults
        UIManager.put("Panel.background", panel);
        UIManager.put("OptionPane.background", panel);
        UIManager.put("OptionPane.messageForeground", text);
        UIManager.put("Button.background", surface);
        UIManager.put("Button.foreground", text);
        UIManager.put("Label.foreground", text);
        UIManager.put("ComboBox.background", surface);
        UIManager.put("ComboBox.foreground", text);
        UIManager.put("ComboBox.selectionBackground", new Color(38, 38, 38));
        UIManager.put("ComboBox.selectionForeground", text);
        UIManager.put("TextArea.background", bg);
        UIManager.put("TextArea.foreground", text);
        UIManager.put("TextArea.caretForeground", text);
        UIManager.put("ScrollPane.background", panel);
        UIManager.put("TitledBorder.titleColor", subtle);
        UIManager.put("TabbedPane.contentAreaColor", bg);
        UIManager.put("ToolTip.background", new Color(40, 40, 40));
        UIManager.put("ToolTip.foreground", text);

        // Improve default font a bit
        Font base = new Font("Segoe UI", Font.PLAIN, 13);
        UIManager.put("Label.font", base);
        UIManager.put("Button.font", base);
        UIManager.put("ComboBox.font", base);
        UIManager.put("TextArea.font", new Font("Consolas", Font.PLAIN, 13));
    }

    private void initializeGraph() {
        graph = new Graph(true);
        locationNodes = new HashMap<>();

        Nodes engineeringSchool = new Nodes(0, "Engineering School");
        Nodes csDept = new Nodes(1, "CS Department");
        Nodes lawFaculty = new Nodes(2, "Law Faculty");
        Nodes jqb = new Nodes(3, "JQB");
        Nodes mainGate = new Nodes(4, "Main Gate");
        Nodes performingArts = new Nodes(5, "School of Performing Arts");
        Nodes mathDept = new Nodes(6, "Math Department");
        Nodes balmeLibrary = new Nodes(7, "Balme Library");
        Nodes ugcs = new Nodes(8, "UGCS");
        Nodes businessSchool = new Nodes(9, "Business School");
        Nodes voltaHall = new Nodes(10, "Volta Hall");
        Nodes commonwealth = new Nodes(11, "Commonwealth");
        Nodes greatHall = new Nodes(12, "Great Hall");
        Nodes akuafoHall = new Nodes(13, "Akuafo Hall");
        Nodes legonHall = new Nodes(14, "Legon Hall");
        Nodes bushCanteen = new Nodes(15, "Bush Canteen");
        Nodes sarbahPark = new Nodes(16, "Sarbah Park");
        Nodes fireStation = new Nodes(17, "Fire Station");
        Nodes bankingSquare = new Nodes(18, "Banking Square");
        Nodes nightMarket = new Nodes(19, "Night Market");
        Nodes basicSchool = new Nodes(20, "Basic School");
        Nodes diasporaHalls = new Nodes(21, "Diaspora Halls");

        locationNodes.put("Engineering School", engineeringSchool);
        locationNodes.put("CS Department", csDept);
        locationNodes.put("Law Faculty", lawFaculty);
        locationNodes.put("JQB", jqb);
        locationNodes.put("Main Gate", mainGate);
        locationNodes.put("School of Performing Arts", performingArts);
        locationNodes.put("Math Department", mathDept);
        locationNodes.put("Balme Library", balmeLibrary);
        locationNodes.put("UGCS", ugcs);
        locationNodes.put("Business School", businessSchool);
        locationNodes.put("Volta Hall", voltaHall);
        locationNodes.put("Commonwealth", commonwealth);
        locationNodes.put("Great Hall", greatHall);
        locationNodes.put("Akuafo Hall", akuafoHall);
        locationNodes.put("Legon Hall", legonHall);
        locationNodes.put("Bush Canteen", bushCanteen);
        locationNodes.put("Sarbah Park", sarbahPark);
        locationNodes.put("Fire Station", fireStation);
        locationNodes.put("Banking Square", bankingSquare);
        locationNodes.put("Night Market", nightMarket);
        locationNodes.put("Basic School", basicSchool);
        locationNodes.put("Diaspora Halls", diasporaHalls);

        addEdgesToGraph();
    }

    private void addEdgesToGraph() {
        graph.addEdge(locationNodes.get("Engineering School"), locationNodes.get("CS Department"), 270.12);
        graph.addEdge(locationNodes.get("Engineering School"), locationNodes.get("Law Faculty"), 420.88);
        graph.addEdge(locationNodes.get("Engineering School"), locationNodes.get("JQB"), 502.43);
        graph.addEdge(locationNodes.get("CS Department"), locationNodes.get("Law Faculty"), 346.45);
        graph.addEdge(locationNodes.get("Law Faculty"), locationNodes.get("JQB"), 289.39);
        graph.addEdge(locationNodes.get("CS Department"), locationNodes.get("Math Department"), 208.65);
        graph.addEdge(locationNodes.get("Math Department"), locationNodes.get("UGCS"), 653.88);
        graph.addEdge(locationNodes.get("UGCS"), locationNodes.get("Business School"), 407.81);
        graph.addEdge(locationNodes.get("Business School"), locationNodes.get("Volta Hall"), 346.82);
        graph.addEdge(locationNodes.get("Volta Hall"), locationNodes.get("Commonwealth"), 536.69);
        graph.addEdge(locationNodes.get("Commonwealth"), locationNodes.get("Great Hall"), 586.81);
        graph.addEdge(locationNodes.get("Main Gate"), locationNodes.get("School of Performing Arts"), 50.00);
        graph.addEdge(locationNodes.get("School of Performing Arts"), locationNodes.get("Balme Library"), 992.04);
        graph.addEdge(locationNodes.get("UGCS"), locationNodes.get("Balme Library"), 269.71);
        graph.addEdge(locationNodes.get("Balme Library"), locationNodes.get("Akuafo Hall"), 316.59);
        graph.addEdge(locationNodes.get("Balme Library"), locationNodes.get("Commonwealth"), 520);
        graph.addEdge(locationNodes.get("School of Performing Arts"), locationNodes.get("Akuafo Hall"), 701.74);
        graph.addEdge(locationNodes.get("Balme Library"), locationNodes.get("Legon Hall"), 586.81);
        graph.addEdge(locationNodes.get("Legon Hall"), locationNodes.get("Akuafo Hall"), 100);
        graph.addEdge(locationNodes.get("Legon Hall"), locationNodes.get("Basic School"), 1015.00);
        graph.addEdge(locationNodes.get("Legon Hall"), locationNodes.get("Sarbah Park"), 500.00);
        graph.addEdge(locationNodes.get("Akuafo Hall"), locationNodes.get("Sarbah Park"), 200.00);
        graph.addEdge(locationNodes.get("Basic School"), locationNodes.get("Night Market"), 591.36);
        graph.addEdge(locationNodes.get("Night Market"), locationNodes.get("Diaspora Halls"), 645.28);
        graph.addEdge(locationNodes.get("Night Market"), locationNodes.get("Banking Square"), 957.14);
        graph.addEdge(locationNodes.get("Bush Canteen"), locationNodes.get("Fire Station"), 122.85);
        graph.addEdge(locationNodes.get("Fire Station"), locationNodes.get("Banking Square"), 957.14);

        graph.addEdge(locationNodes.get("Main Gate"), locationNodes.get("Engineering School"), 800.00);
        graph.addEdge(locationNodes.get("Main Gate"), locationNodes.get("JQB"), 750.00);
        graph.addEdge(locationNodes.get("JQB"), locationNodes.get("Math Department"), 400.00);
        graph.addEdge(locationNodes.get("Great Hall"), locationNodes.get("Akuafo Hall"), 300.00);
        graph.addEdge(locationNodes.get("Great Hall"), locationNodes.get("Legon Hall"), 400.00);
        graph.addEdge(locationNodes.get("Sarbah Park"), locationNodes.get("Bush Canteen"), 350.00);
        graph.addEdge(locationNodes.get("Diaspora Halls"), locationNodes.get("Basic School"), 800.00);
        graph.addEdge(locationNodes.get("Banking Square"), locationNodes.get("Bush Canteen"), 600.00);
    }

    private void setupUI() {
        frame = new JFrame("UG Navigate — Campus Route Finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 640);
        frame.setLocationRelativeTo(null);

        Color bg = new Color(18, 18, 18);
        Color panel = new Color(22, 22, 22);
        Color surface = new Color(28, 28, 28);
        Color text = new Color(224, 224, 224);
        Color border = new Color(56, 56, 56);
        Color accent = new Color(64, 145, 108);   // green accent
        Color accentDanger = new Color(176, 60, 60);

        mainPanel = new JPanel(new BorderLayout(14, 14));
        mainPanel.setBackground(panel);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        // Header
        JLabel header = new JLabel("UG Navigate — Campus Route Finder", SwingConstants.LEFT);
        header.setForeground(text);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JPanel headerWrap = new JPanel(new BorderLayout());
        headerWrap.setBackground(surface);
        headerWrap.setBorder(new LineBorder(border, 1, true));
        headerWrap.add(header, BorderLayout.WEST);
        headerWrap.setPreferredSize(new Dimension(0, 50));
        headerWrap.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(border, 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12))
        );

        JPanel inputPanel = createInputPanel(panel, surface, text, border, accent, accentDanger);
        resultPanel = createResultPanel(panel, surface, text, border);

        mainPanel.add(headerWrap, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(resultPanel, BorderLayout.CENTER);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createInputPanel(Color panel, Color surface, Color text, Color border, Color accent, Color accentDanger) {
        JPanel box = new JPanel();
        box.setLayout(new GridBagLayout());
        box.setBackground(surface);
        box.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(new LineBorder(border, 1, true), "Advanced Route Selection",
                        TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 13), new Color(185, 185, 185)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        box.setPreferredSize(new Dimension(360, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        String[] locations = {"Select a location", "Engineering School", "CS Department", "Math Department",
                "Law Faculty", "JQB", "Main Gate", "School of Performing Arts", "Balme Library",
                "UGCS", "Business School", "Volta Hall", "Commonwealth", "Great Hall",
                "Akuafo Hall", "Legon Hall", "Bush Canteen", "Sarbah Park", "Fire Station",
                "Banking Square", "Night Market", "Basic School", "Diaspora Halls"};

        String[] accessibilityTypes = {"Standard", "Wheelchair", "Elderly", "Visually Impaired", "Mobility Impaired"};

        startLabel = new JLabel("Starting Location:");
        startLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        startLabel.setForeground(text);
        startComboBox = new JComboBox<>(locations);
        styleCombo(startComboBox);

        stopLabel = new JLabel("Destination:");
        stopLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stopLabel.setForeground(text);
        endComboBox = new JComboBox<>(locations);
        styleCombo(endComboBox);

        JLabel accessibilityLabel = new JLabel("Accessibility Type:");
        accessibilityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accessibilityLabel.setForeground(text);
        JComboBox<String> accessibilityComboBox = new JComboBox<>(accessibilityTypes);
        styleCombo(accessibilityComboBox);

        JLabel timeLabel = new JLabel("Departure Time:");
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeLabel.setForeground(text);
        JComboBox<String> timeComboBox = new JComboBox<>(new String[]{
                "Current Time", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM",
                "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM"
        });
        styleCombo(timeComboBox);

        findRouteButton = new JButton("Find Optimal Route");
        stylePrimaryButton(findRouteButton, accent);
        clearButton = new JButton("Clear Results");
        styleDangerButton(clearButton, accentDanger);

        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        box.add(startLabel, gbc);
        gbc.gridy = 1;
        box.add(startComboBox, gbc);

        gbc.gridy = 2;
        box.add(stopLabel, gbc);
        gbc.gridy = 3;
        box.add(endComboBox, gbc);

        gbc.gridy = 4;
        box.add(accessibilityLabel, gbc);
        gbc.gridy = 5;
        box.add(accessibilityComboBox, gbc);

        gbc.gridy = 6;
        box.add(timeLabel, gbc);
        gbc.gridy = 7;
        box.add(timeComboBox, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(surface);
        buttonPanel.add(findRouteButton);
        buttonPanel.add(clearButton);

        gbc.gridy = 8; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        box.add(buttonPanel, gbc);

        findRouteButton.addActionListener(this);
        clearButton.addActionListener(this);

        addButtonHoverEffects(); // keeps your original hover logic (colors adjusted below)
        return box;
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setPreferredSize(new Dimension(250, 34));
        combo.setBorder(new LineBorder(new Color(56, 56, 56), 1, true));
        combo.setBackground(new Color(28, 28, 28));
        combo.setForeground(new Color(224, 224, 224));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setMaximumRowCount(10);
    }

    private void stylePrimaryButton(JButton btn, Color accent) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(accent.darker());
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(accent, 1, true));
        btn.setPreferredSize(new Dimension(160, 36));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Compute the optimal campus route");
        btn.addChangeListener(e -> {
            ButtonModel m = btn.getModel();
            if (m.isRollover()) btn.setBackground(accent);
            else btn.setBackground(accent.darker());
        });
    }

    private void styleDangerButton(JButton btn, Color danger) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(danger.darker());
        btn.setForeground(Color.WHITE);
        btn.setBorder(new LineBorder(danger, 1, true));
        btn.setPreferredSize(new Dimension(140, 36));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Clear the selection and results");
        btn.addChangeListener(e -> {
            ButtonModel m = btn.getModel();
            if (m.isRollover()) btn.setBackground(danger);
            else btn.setBackground(danger.darker());
        });
    }

    private JPanel createResultPanel(Color panel, Color surface, Color text, Color border) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(surface);
        box.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(new LineBorder(border, 1, true), "Route Analysis Results",
                        TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 13), new Color(185, 185, 185)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(new Color(15, 15, 15));
        resultArea.setForeground(text);
        resultArea.setCaretColor(text);
        resultArea.setSelectionColor(new Color(50, 90, 120));
        resultArea.setSelectedTextColor(Color.WHITE);
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(resultArea);
        scrollPane.getViewport().setBackground(new Color(15, 15, 15));
        scrollPane.setBorder(new LineBorder(border, 1, true));
        scrollPane.setPreferredSize(new Dimension(700, 400));

        box.add(scrollPane, BorderLayout.CENTER);
        return box;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == findRouteButton) {
            findRoute();
        } else if (e.getSource() == clearButton) {
            clearResults();
        }
    }

    private void findRoute() {
        try {
            String startLocation = startComboBox.getSelectedItem().toString();
            String endLocation = endComboBox.getSelectedItem().toString();

            if (startLocation.equals("Select a location") || endLocation.equals("Select a location")) {
                JOptionPane.showMessageDialog(frame,
                        "Please select both starting and destination locations.",
                        "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (startLocation.equals(endLocation)) {
                JOptionPane.showMessageDialog(frame,
                        "Starting and destination locations must be different.",
                        "Invalid Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Nodes startNode = locationNodes.get(startLocation);
            Nodes endNode = locationNodes.get(endLocation);

            if (startNode == null || endNode == null) {
                JOptionPane.showMessageDialog(frame, "Invalid location selected.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            RouteOptimizer.RouteAnalysis analysis =
                    RouteOptimizer.findOptimalRoutes(graph, startNode, endNode, new ArrayList<>());
            displayResults(analysis, startLocation, endLocation);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                    "An error occurred while finding the route: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void displayResults(RouteOptimizer.RouteAnalysis analysis, String startLocation, String endLocation) {
        StringBuilder result = new StringBuilder();
        result.append("UG CAMPUS ADVANCED ROUTE ANALYSIS\n");
        result.append("From: ").append(startLocation).append("\n");
        result.append("To: ").append(endLocation).append("\n");
        result.append("Analysis Time: ").append(java.time.LocalTime.now().toString()).append("\n\n");

        if (analysis.optimalRoute != null) {
            result.append(" OPTIMAL ROUTE:\n");
            result.append("Algorithm: ").append(analysis.optimalRoute.algorithm).append("\n");
            result.append("Path: ").append(String.join(" → ", analysis.optimalRoute.path)).append("\n");
            result.append("Distance: ").append(String.format("%.2f", analysis.optimalRoute.distance)).append(" meters\n");
            result.append("Base Time: ").append(String.format("%.1f", analysis.optimalRoute.time)).append(" seconds\n");
            result.append("Base Time: ").append(String.format("%.1f", analysis.optimalRoute.time / 60)).append(" minutes\n\n");

            TrafficSimulator.TimeBasedRoute trafficRoute = TrafficSimulator.optimizeForTime(
                    analysis.optimalRoute.path, analysis.optimalRoute.distance, analysis.optimalRoute.time,
                    java.time.LocalTime.now());
            result.append(" TRAFFIC-ADJUSTED TIME: ")
                    .append(String.format("%.1f", trafficRoute.trafficAdjustedTime)).append(" seconds\n");
            result.append(" RECOMMENDATION: ").append(trafficRoute.recommendedTime).append("\n\n");

            WeatherIntegration.WeatherAdjustedRoute weatherRoute =
                    WeatherIntegration.adjustRouteForWeather(
                            analysis.optimalRoute.path, analysis.optimalRoute.distance, analysis.optimalRoute.time);
            result.append(" WEATHER-ADJUSTED TIME: ")
                    .append(String.format("%.1f", weatherRoute.weatherAdjustedTime)).append(" seconds\n");
            result.append(" WEATHER IMPACT: ").append(weatherRoute.weatherImpact).append("\n\n");

            AccessibilityFeatures.AccessibilityRoute accessibleRoute =
                    AccessibilityFeatures.createAccessibleRoute(
                            analysis.optimalRoute.path, analysis.optimalRoute.distance,
                            analysis.optimalRoute.time, "standard");
            result.append(" ACCESSIBILITY-ADJUSTED TIME: ")
                    .append(String.format("%.1f", accessibleRoute.time)).append(" seconds\n");
            result.append(" ACCESSIBILITY SCORE: ")
                    .append(String.format("%.2f", accessibleRoute.accessibilityScore)).append("\n\n");
        } else {
            result.append("❌ No route found between the selected locations.\n\n");
        }

        if (!analysis.routes.isEmpty()) {
            result.append(" ALTERNATIVE ROUTES:\n");
            for (int i = 0; i < Math.min(3, analysis.routes.size()); i++) {
                SortingAlgorithms.Route route = analysis.routes.get(i);
                result.append(i + 1).append(". ").append(route.algorithm).append(":\n");
                result.append("   Path: ").append(String.join(" → ", route.path)).append("\n");
                result.append("   Distance: ").append(String.format("%.2f", route.distance)).append("m\n");
                result.append("   Time: ").append(String.format("%.1f", route.time)).append("s\n\n");
            }
        }

        if (!analysis.algorithmPerformance.isEmpty()) {
            result.append("⚡ ALGORITHM PERFORMANCE ANALYSIS:\n");
            for (Map.Entry<String, Double> entry : analysis.algorithmPerformance.entrySet()) {
                result.append("• ").append(entry.getKey()).append(": ").append(entry.getValue()).append("ms\n");
            }
            result.append("\n");
        }

        if (analysis.optimalRoute != null) {
            result.append(" WEATHER ANALYSIS:\n");
            result.append(WeatherIntegration.generateWeatherReport(analysis.optimalRoute.path));

            result.append("♿ ACCESSIBILITY FEATURES:\n");
            AccessibilityFeatures.AccessibilityRoute accessibleRoute =
                    AccessibilityFeatures.createAccessibleRoute(
                            analysis.optimalRoute.path, analysis.optimalRoute.distance,
                            analysis.optimalRoute.time, "standard");
            for (String feature : accessibleRoute.features) {
                result.append("• ").append(feature).append("\n");
            }
            result.append("\n");

            result.append("🚦 TRAFFIC ANALYSIS:\n");
            result.append(TrafficSimulator.generateTrafficReport(
                    analysis.optimalRoute.path, java.time.LocalTime.now()));
        }

        result.append(" CAMPUS LANDMARKS & FACILITIES:\n");
        if (analysis.optimalRoute != null) {
            for (String location : analysis.optimalRoute.path) {
                if (location.contains("Library")) {
                    result.append(" ").append(location).append(" (Study Area)\n");
                } else if (location.contains("Canteen")) {
                    result.append(" ").append(location).append(" (Food & Refreshments)\n");
                } else if (location.contains("Park")) {
                    result.append(" ").append(location).append(" (Recreation Area)\n");
                } else if (location.contains("Bank")) {
                    result.append(" ").append(location).append(" (Financial Services)\n");
                } else if (location.contains("Market")) {
                    result.append(" ").append(location).append(" (Shopping Area)\n");
                } else if (location.contains("Station")) {
                    result.append(" ").append(location).append(" (Emergency Services)\n");
                } else if (location.contains("Hall")) {
                    result.append(" ").append(location).append(" (Student Accommodation)\n");
                } else if (location.contains("School") || location.contains("Department")) {
                    result.append(" ").append(location).append(" (Academic Building)\n");
                }
            }
        }

        result.append("\n SMART RECOMMENDATIONS:\n");
        if (analysis.optimalRoute != null) {
            List<String> weatherRecs =
                    WeatherIntegration.getWeatherRecommendations(analysis.optimalRoute.path);
            List<String> accessibilityRecs =
                    AccessibilityFeatures.getAccessibilityRecommendations(analysis.optimalRoute.path, "standard");

            for (String rec : weatherRecs) {
                result.append("• ").append(rec).append("\n");
            }
            for (String rec : accessibilityRecs) {
                result.append("• ").append(rec).append("\n");
            }
        }

        resultArea.setText(result.toString());
        resultArea.setCaretPosition(0);
    }

    private void clearResults() {
        resultArea.setText("");
        startComboBox.setSelectedIndex(0);
        endComboBox.setSelectedIndex(0);
    }

    private void addButtonHoverEffects() {
        // Keep your hover behavior but align with new palette
        findRouteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // handled by ChangeListener in stylePrimaryButton for smoother effect
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // handled by ChangeListener
            }
        });

        clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // handled by ChangeListener in styleDangerButton
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // handled by ChangeListener
            }
        });
    }

    /** Optional launcher (safe to keep; remove if you already launch elsewhere) */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppFrame::new);
    }
}

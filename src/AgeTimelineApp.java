import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgeTimelineApp {
    private static final int START_YEAR = 2024;
    private static final int YEAR_RANGE = 30;

    public static void main(String[] args) {
        List<Character> characters = DataLoader.loadCharacters("data/characters.json");
        List<Event> events = DataLoader.loadEvents("data/events.json");

        JFrame frame = new JFrame("Возраст персонажей и события");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 550);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel dateLabel = new JLabel("", SwingConstants.CENTER);
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        Integer[] years = new Integer[YEAR_RANGE];
        for (int i = 0; i < YEAR_RANGE; i++) years[i] = START_YEAR + i;
        JComboBox<Integer> yearSelector = new JComboBox<>(years);

        JSlider monthSlider = new JSlider(1, 12, 1);
        monthSlider.setMajorTickSpacing(1);
        monthSlider.setPaintTicks(true);
        monthSlider.setPaintLabels(true);

        String[] monthNames = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        Map<Integer, JLabel> monthLabels = new HashMap<>();
        for (int i = 0; i < 12; i++) monthLabels.put(i + 1, new JLabel(monthNames[i]));
        monthSlider.setLabelTable(new java.util.Hashtable<>(monthLabels));

        JPanel timelinePanel = new JPanel() {
            private Event hoveredEvent = null;

            {
                addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(java.awt.event.MouseEvent e) {
                        hoveredEvent = null;
                        int width = getWidth();
                        int sliderX = 50;
                        int sliderWidth = width - 100;
                        int yEvent = 23;
                        int selectedYear = (Integer) yearSelector.getSelectedItem();

                        for (Event event : events) {
                            if (event.getDate().getYear() == selectedYear) {
                                int month = event.getDate().getMonthValue();
                                int x = sliderX + (month - 1) * sliderWidth / 11;
                                if (Math.abs(e.getX() - x) < 6 && Math.abs(e.getY() - yEvent) < 6) {
                                    hoveredEvent = event;
                                    break;
                                }
                            }
                        }
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int sliderX = 50;
                int sliderWidth = width - 100;
                int yLine = 25;
                int yEvent = yLine - 2;

                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(sliderX, yLine, sliderX + sliderWidth, yLine);

                // подписи месяцев и точки
                g.setFont(new Font("SansSerif", Font.PLAIN, 12));
                for (int i = 0; i < 12; i++) {
                    int x = sliderX + i * sliderWidth / 11;
                    g.setColor(Color.GRAY);
                    g.fillRect(x - 1, yLine - 5, 2, 10);
                    g.setColor(Color.BLACK);
                    g.drawString(monthNames[i], x - 10, yLine + 25);

                    int selectedYear = (Integer) yearSelector.getSelectedItem();
                    for (Event event : events) {
                        if (event.getDate().getYear() == selectedYear && event.getDate().getMonthValue() == i + 1) {
                            g.setColor(new Color(200, 50, 50));
                            g.fillOval(x - 4, yEvent, 8, 8);

                            if (event.equals(hoveredEvent)) {
                                g.setColor(Color.RED);
                                g.drawString(event.getName(), x - 30, yEvent - 10);
                            }
                        }
                    }
                }
            }
        };
        timelinePanel.setPreferredSize(new Dimension(700, 70));
        timelinePanel.setBackground(Color.WHITE);

        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        sliderPanel.add(monthSlider, BorderLayout.CENTER);

        controlPanel.add(yearSelector, BorderLayout.NORTH);
        controlPanel.add(sliderPanel, BorderLayout.CENTER);

        JTextArea output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 14));

        Runnable update = () -> {
            int month = monthSlider.getValue();
            int year = (Integer) yearSelector.getSelectedItem();
            LocalDate currentDate = LocalDate.of(year, month, 1);

            dateLabel.setText(String.format("%s %d", monthLabels.get(month).getText(), year));

            StringBuilder sb = new StringBuilder();
            for (Character c : characters) {
                sb.append(c.getName())
                        .append(": ")
                        .append(c.getAgeAt(currentDate))
                        .append("\n");
            }
            output.setText(sb.toString());
            timelinePanel.repaint();
        };

        monthSlider.addChangeListener(e -> update.run());
        yearSelector.addActionListener(e -> update.run());
        update.run();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(dateLabel, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.CENTER);
        topPanel.add(timelinePanel, BorderLayout.SOUTH);

        frame.add(topPanel, BorderLayout.CENTER);
        frame.add(new JScrollPane(output), BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}

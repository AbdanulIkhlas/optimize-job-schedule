package greedy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JobView extends JFrame {
    private List<Job> jobs;
    private JTextArea outputArea;

    public JobView() {
        jobs = new ArrayList<>();
        
        setTitle("Job Scheduling");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));

        JLabel startTimeLabel = new JLabel("Start Time:");
        JTextField startTimeField = new JTextField();
        JLabel endTimeLabel = new JLabel("End Time:");
        JTextField endTimeField = new JTextField();
        JLabel profitLabel = new JLabel("Profit:");
        JTextField profitField = new JTextField();

        inputPanel.add(startTimeLabel);
        inputPanel.add(startTimeField);
        inputPanel.add(endTimeLabel);
        inputPanel.add(endTimeField);
        inputPanel.add(profitLabel);
        inputPanel.add(profitField);

        JButton addButton = new JButton("Add Job");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int startTime = Integer.parseInt(startTimeField.getText());
                int endTime = Integer.parseInt(endTimeField.getText());
                int profit = Integer.parseInt(profitField.getText());

                Job job = new Job(startTime, endTime, profit);
                jobs.add(job);

                startTimeField.setText("");
                endTimeField.setText("");
                profitField.setText("");

                outputArea.append("Job added: " + job.toString() + "\n");
            }
        });

        JButton scheduleButton = new JButton("Schedule Jobs");
        scheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Job> schedule = scheduleJobs(jobs);
                outputArea.append("\nOptimal Schedule:\n");
                for (Job job : schedule) {
                    outputArea.append(job.toString() + "\n");
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(scheduleButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private List<Job> scheduleJobs(List<Job> jobs) {
        Collections.sort(jobs, new Comparator<Job>() {
            @Override
            public int compare(Job j1, Job j2) {
                return j1.getEndTime() - j2.getEndTime();
            }
        });

        List<Job> schedule = new ArrayList<>();
        schedule.add(jobs.get(0));
        int lastEndTime = jobs.get(0).getEndTime();

        for (int i = 1; i < jobs.size(); i++) {
            Job currentJob = jobs.get(i);
            if (currentJob.getStartTime() >= lastEndTime) {
                schedule.add(currentJob);
                lastEndTime = currentJob.getEndTime();
            }
        }

        return schedule;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JobView().setVisible(true);
            }
        });
    }
}

class Job {
    private int startTime;
    private int endTime;
    private int profit;

    public Job(int startTime, int endTime, int profit) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.profit = profit;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getProfit() {
        return profit;
    }

    @Override
    public String toString() {
        return "Job {start time: " + startTime + ", end time: " + endTime + ", profit: " + profit + "}";
    }
}


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MatrixGUI extends JFrame {
    private JTextField inputFileField;
    private JButton loadButton, calculateButton;
    private JLabel resultLabel;
    private JTable matrixTableA, matrixTableB;
    private int[][] matrixA, matrixB;
    private int n;

    // Власне виключення
    public static class CustomArithmeticException extends ArithmeticException {
        public CustomArithmeticException(String message) {
            super(message);
        }
    }

    public MatrixGUI() {
        setTitle("Matrix Comparison");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel inputFileLabel = new JLabel("Input file:");
        inputFileField = new JTextField(20);
        loadButton = new JButton("Load");

        inputPanel.add(inputFileLabel);
        inputPanel.add(inputFileField);
        inputPanel.add(loadButton);

        add(inputPanel, BorderLayout.NORTH);

        JPanel matrixPanel = new JPanel();
        matrixPanel.setLayout(new GridLayout(2, 1));

        matrixTableA = new JTable(15, 15);  // Максимум 15x15
        matrixTableB = new JTable(15, 15);

        matrixPanel.add(new JScrollPane(matrixTableA));
        matrixPanel.add(new JScrollPane(matrixTableB));

        add(matrixPanel, BorderLayout.CENTER);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new FlowLayout());

        calculateButton = new JButton("Calculate");
        resultLabel = new JLabel("Result: ");

        resultPanel.add(calculateButton);
        resultPanel.add(resultLabel);

        add(resultPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(new LoadButtonListener());
        calculateButton.addActionListener(new CalculateButtonListener());
    }

    private class LoadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String filePath = inputFileField.getText();
            try {
                loadMatricesFromFile(filePath);
                fillTable(matrixTableA, matrixA);
                fillTable(matrixTableB, matrixB);
                resultLabel.setText("Matrices loaded successfully.");
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "File not found: " + filePath);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid number format in file.");
            } catch (CustomArithmeticException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (matrixA != null && matrixB != null) {
                int[] X = createVectorX(matrixA, matrixB);
                StringBuilder result = new StringBuilder("Vector X: ");
                for (int value : X) {
                    result.append(value).append(" ");
                }
                resultLabel.setText(result.toString());
            } else {
                JOptionPane.showMessageDialog(null, "Please load matrices first.");
            }
        }
    }

   private void loadMatricesFromFile(String filePath) throws FileNotFoundException, CustomArithmeticException {
    try (Scanner scanner = new Scanner(new File(filePath))) {  
        n = scanner.nextInt();

        if (n > 15) {
            throw new CustomArithmeticException("Matrix dimension exceeds 15!");
        }

        matrixA = new int[n][n];
        matrixB = new int[n][n];

        // A
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!scanner.hasNextInt()) {
                    throw new NumberFormatException("Invalid number format at matrix A[" + i + "][" + j + "]");
                }
                matrixA[i][j] = scanner.nextInt();
            }
        }

        // B
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!scanner.hasNextInt()) {
                    throw new NumberFormatException("Invalid number format at matrix B[" + i + "][" + j + "]");
                }
                matrixB[i][j] = scanner.nextInt();
            }
        }
    }
}


    private void fillTable(JTable table, int[][] matrix) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                table.setValueAt(matrix[i][j], i, j);
            }
        }
    }

    private boolean isRowEqualToColumn(int[][] A, int[][] B, int rowIndex, int colIndex) {
        for (int i = 0; i < A.length; i++) {
            if (A[rowIndex][i] != B[i][colIndex]) {
                return false;
            }
        }
        return true;
    }

    private int[] createVectorX(int[][] A, int[][] B) {
        int[] X = new int[n];
        for (int i = 0; i < n; i++) {
            if (isRowEqualToColumn(A, B, i, i)) {
                X[i] = 1;
            } else {
                X[i] = 0;
            }
        }
        return X;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MatrixGUI gui = new MatrixGUI();
            gui.setVisible(true);
        });
    }
}

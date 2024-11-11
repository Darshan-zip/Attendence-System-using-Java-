package packages.datastructures;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private List<String[]> rows = new ArrayList<>();
    private List<String> columnNames = new ArrayList<>();
    private int[] columnWidths;

    public void clear() {
        rows.clear();        
        columnNames.clear();
    }

    public void addColumn(String columnName) {
        columnNames.add(columnName);
        updateColumnWidths();
    }

    public void addRow(String... values) {
        if (values.length != columnNames.size()) {
            throw new IllegalArgumentException("Row length does not match column count.");
        }
        rows.add(values);
        updateColumnWidths();
    }

    public void updateRow(int index, String... values) {
        if (index < 0 || index >= rows.size()) {
            throw new IndexOutOfBoundsException("Invalid row index.");
        }
        if (values.length != columnNames.size()) {
            throw new IllegalArgumentException("New row length does not match column count.");
        }
        rows.set(index, values);
        updateColumnWidths();
    }

    private void updateColumnWidths() {
        columnWidths = new int[columnNames.size()];
        for (int i = 0; i < columnNames.size(); i++) {
            columnWidths[i] = columnNames.get(i).length();
        }
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }
    }

    public void print() {
        printSeparator();
        printRow(columnNames.toArray(new String[0]));
        printSeparator();
        for (String[] row : rows) {
            printRow(row);
        }
        printSeparator();
    }

    private void printRow(String[] row) {
        StringBuilder sb = new StringBuilder();
        sb.append("|");
        for (int i = 0; i < row.length; i++) {
            sb.append(" ").append(padRight(row[i], columnWidths[i])).append(" |");
        }
        System.out.println(sb.toString());
    }

    private void printSeparator() {
        StringBuilder sb = new StringBuilder("+");
        for (int width : columnWidths) {
            sb.append("-".repeat(width + 2)).append("+");
        }
        System.out.println(sb.toString());
    }

    private String padRight(String str, int length) {
        return String.format("%-" + length + "s", str);
    }

    public static void main(String[] args) {
        Table table = new Table();
        table.addColumn("S.no");
        table.addColumn("Experiment");
        table.addColumn("Date");
        
        table.addRow("1","A1:If Else","10/01");
        table.addRow("2","A2:While For","10/02");
        table.addRow("3","A3:Functions","10/03");
        table.addRow("4","A4:Classes","10/04");

        table.print();
    }
}

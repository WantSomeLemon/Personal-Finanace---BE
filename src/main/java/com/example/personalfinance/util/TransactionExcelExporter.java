package com.example.personalfinance.util;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import com.example.personalfinance.entity.Transaction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class generates an Excel file containing transaction data.
 * It uses Apache POI to create an XLSX file from a list of Transaction objects.
 */
public class TransactionExcelExporter {
    private XSSFWorkbook workbook; // The workbook that holds the Excel file
    private XSSFSheet sheet;       // The sheet where the transaction data will be written
    private final List<Transaction> transactionList; // List of transactions to be exported

    /**
     * Constructor to initialize the Excel exporter with a list of transactions.
     *
     * @param transactionList list of transactions to be included in the Excel file
     */
    public TransactionExcelExporter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
        this.workbook = new XSSFWorkbook(); // Create a new workbook
    }

    /**
     * Writes the header row of the Excel sheet (the column names).
     */
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Transactions"); // Create a new sheet named "Transactions"

        Row row = sheet.createRow(0); // Create the first row (header row)

        // Define a style for the header cells (bold font, 16 pt size)
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16); // Set font size to 16
        style.setFont(font);

        // Create header cells for each column
        createCell(row, 0, "Transaction ID", style);
        createCell(row, 1, "Date", style);
        createCell(row, 2, "Time", style);
        createCell(row, 3, "Amount", style);
        createCell(row, 4, "Category", style);
        createCell(row, 5, "Type", style);
        createCell(row, 6, "Description", style);
        createCell(row, 7, "Account", style);
        createCell(row, 8, "Payment Type", style);
    }

    /**
     * Creates a single cell in the specified row and column, with the given value and style.
     *
     * @param row the row to create the cell in
     * @param column the column to create the cell in
     * @param value the value to be inserted in the cell
     * @param style the style to be applied to the cell
     */
    private void createCell(Row row, int column, Object value, CellStyle style) {
        sheet.autoSizeColumn(column); // Auto-size the column based on the content
        Cell cell = row.createCell(column); // Create the cell in the specified row and column

        // Set the cell value based on the type of the value object
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : ""); // Default to empty string if value is null
        }

        cell.setCellStyle(style); // Apply the style to the cell
    }

    /**
     * Writes the data rows to the Excel sheet, one row per transaction.
     */
    private void writeDataLines() {
        int rowCount = 1; // Start writing data from the second row (row index 1)

        // Define a style for the data rows (font size 14)
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14); // Set font size to 14
        style.setFont(font);

        // Loop through the list of transactions and create a row for each
        for (Transaction transaction : transactionList) {
            Row row = sheet.createRow(rowCount++); // Create a new row for each transaction
            int columnCount = 0; // Start with the first column

            // Convert the transaction timestamp to LocalDateTime
            Instant instant = Instant.ofEpochMilli(transaction.getDateTime());
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            // Format the date and time using DateTimeFormatter
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            String formattedDate = dateTime.format(dateFormatter);
            String formattedTime = dateTime.format(timeFormatter);

            // Write the transaction data to the row
            createCell(row, columnCount++, transaction.getId(), style);
            createCell(row, columnCount++, formattedDate, style);
            createCell(row, columnCount++, formattedTime, style);
            createCell(row, columnCount++, transaction.getAmount(), style);
            createCell(row, columnCount++, transaction.getCategory().getName(), style);
            createCell(row, columnCount++, transaction.getCategory().getType(), style);
            createCell(row, columnCount++, transaction.getDescription(), style);
            createCell(row, columnCount++, transaction.getAccount().getName(), style);
            createCell(row, columnCount++, transaction.getPaymentType(), style);
        }
    }

    /**
     * Exports the transaction data to an Excel file and writes it to the HTTP response output stream.
     *
     * @param response the HttpServletResponse to write the Excel file to
     * @throws IOException if an I/O error occurs while writing the file
     */
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine(); // Write the header row
        writeDataLines(); // Write the data rows

        // Get the output stream of the HTTP response and write the workbook to it
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream); // Write the workbook to the output stream
        } finally {
            workbook.close(); // Close the workbook to release resources
        }
    }
}

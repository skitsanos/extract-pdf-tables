package com.skitsanos;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import technology.tabula.*;
import technology.tabula.extractors.ExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TableExtractor {
    public static void main(String @NotNull [] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Please provide the path to a PDF file location as an argument.");
            System.exit(1);
        }

        String pdfFilePath = args[0];
        File pdfFile = new File(pdfFilePath);

        if (!pdfFile.exists() || !pdfFile.isFile()) {
            System.err.println("Invalid file path provided.");
            System.exit(1);
        }

        PDDocument pdfDocument = PDDocument.load(pdfFile);

        ObjectExtractor oe = new ObjectExtractor(pdfDocument);
        PageIterator pages = oe.extract();
        ExtractionAlgorithm algorithm = new SpreadsheetExtractionAlgorithm();

        JSONArray tablesJsonArray = new JSONArray();

        while (pages.hasNext()) {
            Page page = pages.next();
            var tables = algorithm.extract(page);
            for (Table table : tables) {
                JSONArray tableArray = new JSONArray();

                for (List<RectangularTextContainer> row : table.getRows()) {
                    JSONArray rowArray = new JSONArray();
                    for (RectangularTextContainer cell : row) {
                        rowArray.put(cell.getText().trim());
                    }
                    tableArray.put(rowArray);
                }

                tablesJsonArray.put(tableArray);
            }
        }

        pdfDocument.close();

        System.out.println(tablesJsonArray.toString(2));
    }
}
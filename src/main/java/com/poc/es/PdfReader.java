package com.poc.es;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfReader {

	public static void main(String[] args) {
		try {
			// Creating file instance
			File file = new File("C:\\Users\\Faizanahmed.khan\\Downloads\\git-cheat-sheet-education.pdf");
			// Loading pdf file
			PDDocument document = PDDocument.load(file);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			// Fetching PDF document
			String text = pdfStripper.getText(document);
			System.out.println(text);
			// Closing the document
			document.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}

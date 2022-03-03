package com.poc.es.utils;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PdfUtils {

	private Logger logger = LoggerFactory.getLogger(PdfUtils.class);

	public String extractDataFromInputFile(MultipartFile doc) throws AppServiceException {
		logger.debug("Inside PdfUtils -- extractDataFromInputFile");
		try {
			// Creating file instance
			File file = new File("C:/Users/Faizanahmed.khan/Downloads/targetFile.tmp");
			doc.transferTo(file);
			// Loading PDF file
			System.out.println("Loading the PDF file");
			PDDocument document = PDDocument.load(file);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			// Fetching PDF document
			String extractedText = pdfStripper.getText(document);
			// Closing the document
			document.close();
			System.out.println("PDF Loaded successfully");
			return extractedText;
		} catch (Exception e) {
			logger.error("Exception Occured while extractDataFromInputFile {}", e);
			throw new AppServiceException("Exception Occured while extractDataFromInputFile", e);
		}
	}
}

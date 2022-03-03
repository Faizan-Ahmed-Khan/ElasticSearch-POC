package com.poc.es.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poc.es.model.StoredDocument;
import com.poc.es.service.IAppService;
import com.poc.es.utils.AppServiceException;
import com.poc.es.utils.ElasticUtils;
import com.poc.es.utils.PdfUtils;

@Service
public class AppServiceImpl implements IAppService {

	@Autowired
	private ElasticUtils elasticUtils;

	@Autowired
	private PdfUtils pdfUtils;

	private Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);

	@Override
	public String index(String indexName, MultipartFile doc, String idName) throws AppServiceException {
		logger.debug("Inside AppService -- index()");
		String extractedText = pdfUtils.extractDataFromInputFile(doc);
		return elasticUtils.indexAFileInElasticSearch(indexName, extractedText, idName);
	}

	@Override
	public String search(String key) throws AppServiceException {
		logger.debug("Inside AppService -- search()");
		List<StoredDocument> l = elasticUtils.searchInElasticSearch(key);
		logger.info("search result:: {}", l);
		return l.toString();
	}
}

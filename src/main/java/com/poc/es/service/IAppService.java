package com.poc.es.service;

import org.springframework.web.multipart.MultipartFile;

import com.poc.es.utils.AppServiceException;

public interface IAppService {

	String index(String indexName, MultipartFile doc, String idName) throws AppServiceException;

	String search(String key) throws AppServiceException;

}

package com.poc.es.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.poc.es.model.Product;
import com.poc.es.model.StoredDocument;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Component
public class ElasticUtils {

	RestClient restClient;
	ElasticsearchClient client;

	private Logger logger = LoggerFactory.getLogger(ElasticUtils.class);

	@PostConstruct
	private void init() {
		logger.info("Establishing connection with the ES");
		// Create the low-level client
		restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();

		// Create the transport with a Jackson mapper
		ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

		// And create the API client
		client = new ElasticsearchClient(transport);
		logger.info("Connection Established");
	}

	@PreDestroy
	private void closeConnection() throws IOException {
		restClient.close();
	}

	public String indexAFileInElasticSearch(String indexName, String extractedText, String idName)
			throws AppServiceException {
		logger.debug("Inside indexAFileInElasticSearch");
		try {
			checkIfIndexExistsElseCreate(indexName);

			IndexResponse ir = client.index(builder -> builder.index(indexName)
					.document(Product.builder().name(extractedText).build()).id(idName));
			return ("Document Stored with id:: " + ir.id());

		} catch (ElasticsearchException elEx) {
			logger.error("ElasticsearchException Occured while indexing a document:: {}", elEx.getMessage());
			throw new AppServiceException("ElasticsearchException Occured while indexing a document", elEx);
		} catch (IOException ioEx) {
			logger.error("IOException Occured while indexing a document:: {}", ioEx.getMessage());
			throw new AppServiceException("IOException Occured while indexing a document", ioEx);
		}
	}

	public void checkIfIndexExistsElseCreate(String indexName) throws AppServiceException {
		logger.debug("Inside checkIfIndexExistsElseCreate");
		try {
			BooleanResponse resp = client.indices().exists(builder -> builder.index(indexName, ""));
			if (!resp.value()) {
				CreateIndexResponse index = client.indices().create(indexBuilder -> indexBuilder.index(indexName)
						.aliases("alias", aliasBuilder -> aliasBuilder.isWriteIndex(true)));
				logger.info("Index Created, name:: " + index.index());
			} else {
				logger.info("Index already Exists");
			}
		} catch (ElasticsearchException elEx) {
			logger.error("ElasticsearchException Occured while creating an index:: {}", elEx.getMessage());
			throw new AppServiceException("ElasticsearchException Occured while creating an index", elEx);
		} catch (IOException ioEx) {
			logger.error("IOException Occured while creating an index:: {}", ioEx.getMessage());
			throw new AppServiceException("IOException Occured while creating an index", ioEx);
		}
	}

	public List<StoredDocument> searchInElasticSearch(String key) throws AppServiceException {
		logger.debug("Inside searchInElasticSearch");
		SearchResponse<Product> response;
		try {
			response = client.search(s -> s.query(q -> q.term(t -> t.field("name").value(v -> v.stringValue(key)))),
					Product.class);
			List<StoredDocument> docList = new ArrayList<>();
			for (Hit<Product> p : response.hits().hits()) {
				docList.add(StoredDocument.builder().id(p.id()).index(p.index()).doc(p.source().getName()).build());
			}
			return docList;
		} catch (ElasticsearchException elEx) {
			logger.error("ElasticsearchException Occured while searching a document:: {}", elEx.getMessage());
			throw new AppServiceException("ElasticsearchException Occured while searching a document", elEx);
		} catch (IOException ioEx) {
			logger.error("IOException Occured while searching a document:: {}", ioEx.getMessage());
			throw new AppServiceException("IOException Occured while searching a document", ioEx);
		}

	}

}

package org.njit.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TrendsStore {
    private static final String TRENDS_BUCKET_NAME = System.getenv("TRENDS_BUCKET");
    private final AmazonS3 s3Client;
    private final ObjectMapper objectMapper;

    public TrendsStore() {
        this.s3Client = AmazonS3ClientBuilder.standard().build();
        this.objectMapper = new ObjectMapper();
    }

    public Trend get(final String id) throws Exception {
        final S3Object object = s3Client.getObject(new GetObjectRequest(TRENDS_BUCKET_NAME, id));
        final S3ObjectInputStream input = object.getObjectContent();

        return objectMapper.readValue(input, Trend.class);
    }

    public void save(final Trend trend) throws JsonProcessingException {
        s3Client.putObject(TRENDS_BUCKET_NAME, trend.getId(), objectMapper.writeValueAsString(trend));
    }

    public void delete(final String id) {
        s3Client.deleteObject(new DeleteObjectRequest(TRENDS_BUCKET_NAME, id));
    }
}

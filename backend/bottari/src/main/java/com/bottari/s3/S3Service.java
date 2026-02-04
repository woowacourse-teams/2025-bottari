package com.bottari.s3;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import io.awspring.cloud.s3.S3Operations;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Operations s3Operations;

    @Value("${app.s3.bucket}")
    private String bucket;

    public String upload(
            final MultipartFile file,
            final String keyPrefix,
            final String keyName
    ) {
        final String key = getS3Key(keyPrefix, keyName);
        try (InputStream inputStream = file.getInputStream()) {
            s3Operations.upload(bucket, key, inputStream);
        } catch (final IOException e) {
            log.warn("S3 upload failed. bucket={}, key={}", bucket, key, e);
            throw new BusinessException(ErrorCode.S3_UPLOAD_FAILED, "bucket=" + bucket + ", key=" + key);
        }
        log.info("S3 upload complete. bucket={}, key={}", bucket, key);

        return key;
    }

    public Resource download(
            final String keyPrefix,
            final String keyName
    ) {
        final String key = getS3Key(keyPrefix, keyName);
        try {
            final Resource resource = s3Operations.download(bucket, key);
            log.info("S3 download complete. bucket={}, key={}", bucket, key);

            return resource;
        } catch (final Exception e) {
            log.warn("S3 download failed. bucket={}, key={}", bucket, key, e);
            throw new BusinessException(ErrorCode.S3_DOWNLOAD_FAILED, "bucket=" + bucket + ", key=" + key);
        }
    }

    private String getS3Key(
            final String keyPrefix,
            final String keyName
    ) {
        return keyPrefix + "/" + keyName;
    }
}

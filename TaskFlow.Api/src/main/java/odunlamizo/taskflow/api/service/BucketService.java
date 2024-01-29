package odunlamizo.taskflow.api.service;

import io.github.bucket4j.Bucket;

public interface BucketService {

    default Bucket getRoot() {
        throw new UnsupportedOperationException("Unimplemented method 'getRoot'");
    }

    Bucket resolveBucket(String ipAddress);
    
}

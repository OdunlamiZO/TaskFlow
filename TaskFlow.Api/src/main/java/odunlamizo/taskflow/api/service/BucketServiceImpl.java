package odunlamizo.taskflow.api.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Service
public class BucketServiceImpl implements BucketService {

    private final Bucket root;

    private final Map<String, Bucket> cache = new HashMap<>();

    public BucketServiceImpl() {
        Bandwidth limit = Bandwidth.builder().capacity(250).refillIntervally(250, Duration.ofMinutes(1)).build(); // is 250 practical??
        root = Bucket.builder().addLimit(limit).build();
    }

    @Override
    public Bucket getRoot() {
        return root;
    }

    @Override
    public Bucket resolveBucket(String ipAddress) {
        if (cache.containsKey(ipAddress)) {
            return cache.get(ipAddress);
        }
        Bucket bucket = newBucket();
        cache.put(ipAddress, bucket);
        return bucket;
    }

    private Bucket newBucket() {
        return Bucket.builder()
                .addLimit(
                        Bandwidth.builder()
                                .capacity(20)
                                .refillIntervally(20, Duration.ofMinutes(1))
                                .build())
                .build();
    }

}

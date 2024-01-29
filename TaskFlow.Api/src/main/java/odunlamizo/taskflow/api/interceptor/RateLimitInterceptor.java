package odunlamizo.taskflow.api.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import odunlamizo.taskflow.api.dto.Error;
import odunlamizo.taskflow.api.payload.response.AbstractResponsePayload;
import odunlamizo.taskflow.api.service.BucketService;
import odunlamizo.taskflow.api.util.Json;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private BucketService bucketService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Bucket root = bucketService.getRoot();
        if (root.tryConsume(1)) {
            String ipAddress = request.getRemoteAddr();
            Bucket ipBucket = bucketService.resolveBucket(ipAddress);
            if (ipBucket.tryConsume(1)) {
                return true;
            }
        }
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        Json.writeValue(
                response.getOutputStream(),
                AbstractResponsePayload.failure(
                        Error.builder()
                                .useMessage("Too many requests")
                                .build()));
        return false;
    }

}

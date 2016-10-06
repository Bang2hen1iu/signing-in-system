package com.xuemiao.exception;

import org.apache.commons.lang.StringUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dzj on 10/1/2016.
 */
@Provider
public class APIException extends Exception implements ExceptionMapper<Exception> {
    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIException(Throwable cause) {
        super(cause);
    }

    public APIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public Response toResponse(Exception ex) {
        Map<String, String> mp = new HashMap<String, String>();
        mp.put("exception", ex.getClass().getSimpleName());
        if (StringUtils.isNotBlank(ex.getMessage())) {
            mp.put("message", ex.getMessage());
        }
        return Response.status(400).entity(mp).type(MediaType.APPLICATION_JSON).build();
    }
}

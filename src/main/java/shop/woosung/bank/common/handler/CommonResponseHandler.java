package shop.woosung.bank.common.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import shop.woosung.bank.common.ApiResponse;
import shop.woosung.bank.common.exception.CommonIoException;
import shop.woosung.bank.common.exception.ObjectConvertJsonException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CommonResponseHandler {

    public void handleSuccess(HttpServletResponse response, String message, HttpStatus status, Object data) {
        settingJsonResponse(response, status);
        ApiResponse<Object> apiResponse = ApiResponse.success(message, data);
        sendResponse(response, objectConvertJson(apiResponse));
    }

    public void handleException(HttpServletResponse response, String message, HttpStatus status) {
        settingJsonResponse(response, status);
        ApiResponse<Object> apiResponse = ApiResponse.error(message);
        sendResponse(response, objectConvertJson(apiResponse));
    }

    private void settingJsonResponse(HttpServletResponse response, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json; charset=utf-8");
    }

    private <T> String objectConvertJson(ApiResponse<T> apiResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException exception) {
            log.error("JsonProcessingException = {}", exception.getMessage());
            throw new ObjectConvertJsonException();
        }
    }

    private void sendResponse(HttpServletResponse response, String responseBody) {
        try {
            response.getWriter().write(responseBody);
        } catch (IOException ioException) {
            log.error("IOException = {}", ioException.getMessage());
            throw new CommonIoException();
        }
    }
}

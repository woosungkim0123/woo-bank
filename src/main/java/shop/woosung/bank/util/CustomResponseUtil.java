package shop.woosung.bank.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shop.woosung.bank.dto.ResponseDto;

import javax.servlet.http.HttpServletResponse;


public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    /**
     * 401은 인증이 안된 것
     * 403은 권한이 없는 것
     */
    public static void unAuthentication(HttpServletResponse response, String msg) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(401);
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("서버 파싱 에러");
        }

    }
}

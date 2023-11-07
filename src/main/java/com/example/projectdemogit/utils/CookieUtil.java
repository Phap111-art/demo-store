package com.example.projectdemogit.utils;


import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Component
public class CookieUtil {

    public void setTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setMaxAge(0); // Đặt thời gian sống của cookie thành 0 để xóa nó ngay lập tức
        cookie.setPath("/"); // Đặt đường dẫn cookie cho toàn bộ ứng dụng
        response.addCookie(cookie);
    }

    // Các phương thức khác để tùy chỉnh xóa cookie nếu cần thiết
    // ...

}
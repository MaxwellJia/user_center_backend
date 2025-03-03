import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8000","http://localhost:8000/user/login", "https://icy-tree-0067c3610.6.azurestaticapps.net/user/login") // 允许的前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方式
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true); // 是否允许携带身份认证信息（如 cookie）
    }
}


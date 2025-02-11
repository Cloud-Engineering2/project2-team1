package project2.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		// Jackson의 MappingJackson2HttpMessageConverter를 찾아 추가 미디어 타입 설정
		for (HttpMessageConverter<?> converter: converters) {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
				List<MediaType> supportedMediaTypes = new ArrayList<>(jsonConverter.getSupportedMediaTypes());
				// 기존 지원 미디어 타입에 application/octet-stream 추가
				supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
				jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
			}
		}
	}
}

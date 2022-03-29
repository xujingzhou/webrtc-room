package com.dten.healthcare.room;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Creator: Johnny Xu
 * @Date: 2021/6/30
 * @Description:
 */
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
@ComponentScan(basePackages = {"com.dten.healthcare.room.*"})
@EnableCaching
@EnableAsync
public class HealthcareApplication {

	// 启用的http请求的端口(如80)
	@Value("${http.port}")
	Integer httpPort;

	// 启用的https端口(如443)
	@Value("${server.port}")
	Integer httpsPort;

	public static void main(String[] args) {

		SpringApplication.run(HealthcareApplication.class, args);
	}

	// --------------------------------------
	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
			}
		};

		tomcat.addAdditionalTomcatConnectors(createStandardConnector()); // 添加http
		return tomcat;
	}

	// 配置http
	private Connector createStandardConnector() {
		// 默认协议为org.apache.coyote.http11.Http11NioProtocol
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setSecure(false);
		connector.setScheme("http");
		connector.setPort(httpPort);
		connector.setRedirectPort(httpsPort);
		return connector;
	}
	// --------------------------------------
}

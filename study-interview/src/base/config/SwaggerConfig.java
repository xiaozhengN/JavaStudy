package base.config;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SwaggerConfig
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-12-18 1:54:46
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        RequestParameterBuilder parameterBuilder = new RequestParameterBuilder();
        parameterBuilder.name("user_info").description("登录信息").in(ParameterType.HEADER).required(false).query(param -> param.model(model -> model.scalarModel(ScalarType.STRING)));
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(parameterBuilder.build());
        return new Docket(DocumentationType.OAS_30).apiInfo(getApiInfo()).pathMapping("/").select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.regex("/error.*|/actuator.*").negate()).paths(PathSelectors.regex("/.*")).build().globalRequestParameters(parameters);
    }

    @Bean
    public WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsEndpointProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        ArrayList<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());

        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);

        boolean shouldRegisterLinksMapping = this.shouldRegisterLinkMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsEndpointProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping);
    }

    private boolean shouldRegisterLinkMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        // TODO 这里报错, 可能导包不对
        return false;
        // return webEndpointProperties.getDiscovery().isEnable() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder().title("Custom Service API").description("Custom Service API").version("1.0.0").build();
    }

}

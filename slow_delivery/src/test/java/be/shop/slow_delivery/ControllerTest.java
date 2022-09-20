package be.shop.slow_delivery;

import be.shop.slow_delivery.menu.application.MenuDisplayService;
import be.shop.slow_delivery.menu.application.MenuService;
import be.shop.slow_delivery.menu.presentation.MenuController;
import be.shop.slow_delivery.menu.presentation.dto.MenuDtoMapper;
import be.shop.slow_delivery.product.application.ProductCommandService;
import be.shop.slow_delivery.product.application.ProductQueryService;
import be.shop.slow_delivery.product.presentation.ProductController;
import be.shop.slow_delivery.product.presentation.dto.ProductDtoMapper;
import be.shop.slow_delivery.shop.application.ShopCommandService;
import be.shop.slow_delivery.shop.application.ShopQueryService;
import be.shop.slow_delivery.shop.presentation.ShopController;
import be.shop.slow_delivery.shop.presentation.dto.ShopDtoMapper;
import be.shop.slow_delivery.stock.application.StockCommandService;
import be.shop.slow_delivery.stock.presentation.StockController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest({
        ShopController.class,
        ProductController.class,
        MenuController.class,
        StockController.class
})
public abstract class ControllerTest {
    protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;

    @MockBean protected ShopQueryService shopQueryService;
    @MockBean protected ShopCommandService shopCommandService;
    @MockBean protected ShopDtoMapper shopDtoMapper;

    @MockBean protected ProductQueryService productQueryService;
    @MockBean protected ProductCommandService productCommandService;
    @MockBean protected ProductDtoMapper productDtoMapper;

    @MockBean protected MenuService menuService;
    @MockBean protected MenuDtoMapper menuDtoMapper;
    @MockBean protected MenuDisplayService menuDisplayService;

    @MockBean protected StockCommandService stockCommandService;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider contextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(contextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print()).build();
    }
}

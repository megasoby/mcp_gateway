package com.example.mcpgateway.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class InventoryTool {

    private final RestClient inventoryRestClient;

    public InventoryTool(RestClient inventoryRestClient) {
        this.inventoryRestClient = inventoryRestClient;
    }

    @Tool(description = "상품의 재고 수량과 판매 상태를 조회합니다. 상품ID, 판매처번호, 단위상품ID를 입력하면 해당 상품의 가용재고수량, 판매상태, 예약배송 가능여부 등을 반환합니다.")
    public String queryInventory(
            @ToolParam(description = "상품 ID (예: 1000036532433)") String itemId,
            @ToolParam(description = "판매처 번호 (예: 2493)") String salestrNo,
            @ToolParam(description = "단위 상품 ID (예: 00000)") String uitemId) {

        String response = inventoryRestClient.get()
                .uri("/inventory/front-query/v2/display/inventories?itemId={itemId}&salestrNo={salestrNo}&uitemId={uitemId}",
                        itemId, salestrNo, uitemId)
                .retrieve()
                .body(String.class);

        return response;
    }

    @Tool(description = "상품의 판매가격을 조회합니다. 상품ID, 판매처번호, 단위상품ID를 입력하면 공급가(splprc), 판매가(sellprc), 마진율(mrgrt), 최대판매가(maxSellprc) 등을 반환합니다.")
    public String queryPrice(
            @ToolParam(description = "상품 ID (예: 1000036532433)") String itemId,
            @ToolParam(description = "판매처 번호 (예: 2493)") String salestrNo,
            @ToolParam(description = "단위 상품 ID (예: 00000)") String uitemId) {

        String response = inventoryRestClient.get()
                .uri("/item/query/item/standard/1/price/{itemId}?salestrNos={salestrNo}&uitemIds={uitemId}",
                        itemId, salestrNo, uitemId)
                .retrieve()
                .body(String.class);

        return response;
    }
}

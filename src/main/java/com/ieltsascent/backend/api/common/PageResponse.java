package com.ieltsascent.backend.api.common;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

@Schema(description = "Paginated response wrapper.")
public class PageResponse<T> {
    @ArraySchema(schema = @Schema(description = "Page items."))
    private final List<T> items;

    @Schema(description = "Pagination metadata.")
    private final PageInfo page;

    public PageResponse(List<T> items, PageInfo page) {
        this.items = items;
        this.page = page;
    }

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), PageInfo.from(page));
    }

    public List<T> getItems() {
        return items;
    }

    public PageInfo getPage() {
        return page;
    }
}

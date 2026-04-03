package com.ieltsascent.backend.api.common;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Schema(description = "Paginated response wrapper.")
public record PageResponse<T>(
    @ArraySchema(schema = @Schema(description = "Page items."))
    List<T> items,
    @Schema(description = "Pagination metadata.")
    PageInfo page
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), PageInfo.from(page));
    }

    public static <T> PageResponse<T> from(List<T> items, Pageable pageable) {
        return from(toPage(items, pageable));
    }

    private static <T> Page<T> toPage(List<T> items, Pageable pageable) {
        if (items == null) {
            return new PageImpl<>(List.of(), pageable == null ? Pageable.unpaged() : pageable, 0);
        }
        if (pageable == null || pageable.isUnpaged()) {
            return new PageImpl<>(items);
        }
        int total = items.size();
        long offset = pageable.getOffset();
        if (offset > Integer.MAX_VALUE || offset >= total) {
            return new PageImpl<>(List.of(), pageable, total);
        }
        int start = (int) offset;
        int end = Math.min(start + pageable.getPageSize(), total);
        return new PageImpl<>(items.subList(start, end), pageable, total);
    }
}

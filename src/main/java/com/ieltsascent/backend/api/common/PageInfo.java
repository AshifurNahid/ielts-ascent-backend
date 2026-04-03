package com.ieltsascent.backend.api.common;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(description = "Pagination metadata.")
public record PageInfo(
    @Schema(description = "Zero-based page index.") int page,
    @Schema(description = "Page size.") int size,
    @Schema(description = "Total number of pages.") int totalPages,
    @Schema(description = "Total number of elements.") long totalElements
) {
    public static PageInfo from(Page<?> page) {
        return new PageInfo(
            page.getNumber(),
            page.getSize(),
            page.getTotalPages(),
            page.getTotalElements()
        );
    }
}

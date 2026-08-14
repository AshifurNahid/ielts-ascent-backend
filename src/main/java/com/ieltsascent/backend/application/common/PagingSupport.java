package com.ieltsascent.backend.application.common;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public final class PagingSupport {
    private PagingSupport() {
    }

    public static <T> Page<T> fromList(List<T> items, Pageable pageable) {
        List<T> safeItems = items == null ? List.of() : items;
        Pageable safePageable = pageable == null ? Pageable.unpaged() : pageable;

        if (safePageable.isUnpaged()) {
            return new PageImpl<>(safeItems);
        }

        int total = safeItems.size();
        long offset = safePageable.getOffset();
        if (offset >= total) {
            return new PageImpl<>(List.of(), safePageable, total);
        }

        int start = (int) offset;
        int end = Math.min(start + safePageable.getPageSize(), total);
        return new PageImpl<>(safeItems.subList(start, end), safePageable, total);
    }
}


package com.ieltsascent.backend.api.common;

import java.util.Map;

public record ErrorResponse(String error, Map<String, String> errors, String timestamp) {
}

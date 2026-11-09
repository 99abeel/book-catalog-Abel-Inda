package com.example.catalog.web;

import java.time.Instant;

public record ApiError(String error, String message, Instant timestamp) {
}

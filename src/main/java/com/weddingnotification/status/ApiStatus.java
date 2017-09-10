package com.weddingnotification.status;


/**
 * @author Rohan Jain
 *
 * Enumeration of different weddingapi constants
 * that may occur when processing service request/response.
 * These constants should be identified on client and
 * appropriately handled
 */
public enum ApiStatus {

    DUPLICATE_RECORD(404),
    RECORD_NOT_FOUND(404),
    INVALID_REQUEST(400),
    UNKNOWN(400),
    SUCCESS(200),
    CREATED(201);

    private final int status;

    ApiStatus(int status) {
        this.status=status;
    }

    public int getStatus() {
        return this.status;
    }
}

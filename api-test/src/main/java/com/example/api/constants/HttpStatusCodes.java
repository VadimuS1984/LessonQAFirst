package com.example.api.constants;

/**
 * Константы для HTTP статус-кодов
 */
public class HttpStatusCodes {

    // Успешные ответы
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int NO_CONTENT = 204;

    // Ошибки клиента
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;
    public static final int UNPROCESSABLE_ENTITY = 422;

    // Ошибки сервера
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;

    // Дополнительные коды для специфичных случаев
    public static final int TOO_MANY_REQUESTS = 429;
    public static final int GATEWAY_TIMEOUT = 504;
}

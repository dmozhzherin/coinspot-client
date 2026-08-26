# CoinSpot Client for Kotlin / Java

An asynchronous, type-safe Kotlin client library for the [CoinSpot Australia](https://www.coinspot.com.au/) cryptocurrency exchange [API (v2)](https://www.coinspot.com.au/v2/api).

Built on top of [Ktor Client](https://ktor.io/) and [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html), this library provides a clean interface for accessing CoinSpot's Public, Read-Only, and Full-Access API endpoints with built-in HMAC-SHA512 request signing, nonce management, and Jackson JSON serialization.

---

## Features

- **Asynchronous & Coroutine-First**: All API operations are non-blocking `suspend` functions using Ktor CIO engine.
- **Three API Client Tiers**:
  - `CoinspotPubApiClient`: Access public market rates and prices without authentication.
  - `CoinspotROApiClient`: Query account balances, order history, and deposit/withdrawal transfers using read-only API keys.
  - `CoinspotFAApiClient`: Request swap/sell quotes and execute instant coin swaps using full-access API keys.
- **Automated HMAC-SHA512 Authentication**: Thread-safe request signing and nonce sequencing across concurrent coroutines.
- **Robust Domain Models**:
  - `AssetType` registry with automatic alias resolution (e.g., `STR` → `XLM`, `ENG` → `SCRT`, `BCH` → `BCC`).
  - High-precision `BigDecimal` calculations with automatic serialization formatting.
  - Strongly-typed DTOs for orders, transfers, balances, and swap results.
- **Resource Management**: Implements `AutoCloseable` for easy lifecycle and connection pool management.


## Domain Models

### `AssetType`

`AssetType` provides a type-safe wrapper around coin symbols, automatically handling alias differences between various CoinSpot API endpoints.

```kotlin
// Built-in predefined constants:
val aud = AssetType.AUD
val scrt = AssetType.SCRT // Automatically handles alias "ENG"
val xlm = AssetType.XLM   // Automatically handles alias "STR"

// Dynamically create or resolve any asset:
val sol = AssetType.of("SOL")
```

### `OperationType`

Represents trade and transaction types:
`MARKET`, `INSTANT`, `TAKE_PROFIT`, `STOP_LOSS`, `LIMIT`, `BUY_STOP`, `DEPOSIT`, `WITHDRAWAL`, `TRANSFER`, `UNKNOWN`.

---

## Error Handling

The client maps API response statuses and network issues into specific exceptions:

- `CoinspotApiException`: Thrown when CoinSpot returns an error response (e.g., invalid key, insufficient balance, bad request). Contains `status` and `message`.
- `CoinspotException`: Thrown on communication failures, HTTP errors, or JSON deserialization issues.

```kotlin
try {
    val balance = client.loadBalance(AssetType.of("BTC"))
} catch (e: CoinspotApiException) {
    println("CoinSpot API returned error [${e.status}]: ${e.message}")
} catch (e: CoinspotException) {
    println("Failed to communicate with CoinSpot: ${e.message}")
}
```


## License

This project is available under the Apache License 2.0 terms.

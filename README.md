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


## Quick Start & Usage Examples

### 1. Public API (`CoinspotPubApiClient`)

No API keys are required for the public API.

```kotlin
import dym.coins.coinspot.client.CoinspotPubApiClient
import dym.coins.coinspot.domain.AssetType
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    CoinspotPubApiClient().use { client ->
        // Get latest rates for all coins
        val rates = client.latestRates()
        rates.forEach { (asset, rate) ->
            println("${asset.code}: Bid = ${rate.bid}, Ask = ${rate.ask}, Last = ${rate.last}")
        }

        // Get specific buy and sell prices
        val btcBuyPrice = client.buyPrice(AssetType.of("BTC"))
        val btcSellPrice = client.sellPrice(AssetType.of("BTC"))
        println("BTC Buy: $btcBuyPrice | Sell: $btcSellPrice")
    }
}
```

---

### 2. Read-Only API (`CoinspotROApiClient`)

Requires a Read-Only API Key and Secret generated from your CoinSpot account settings.

```kotlin
import dym.coins.coinspot.client.CoinspotROApiClient
import dym.coins.coinspot.domain.AssetType
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

fun main() = runBlocking {
    val apiKey = System.getenv("COINSPOT_RO_KEY")
    val apiSecret = System.getenv("COINSPOT_RO_SECRET")

    CoinspotROApiClient(apiKey, apiSecret).use { client ->
        // Fetch all account balances
        val balances = client.loadBalances()
        balances.forEach { (asset, balance) ->
            println("${asset.code}: Total = ${balance.balance}, AUD Value = ${balance.audbalance}")
        }

        // Fetch detailed balance for a single coin (includes available balance)
        val ethBalance = client.loadBalance(AssetType.of("ETH"))
        println("ETH Balance: ${ethBalance.balance}, Available: ${ethBalance.available}")

        // Load completed order history within date range
        val startDate = LocalDate.now().minusMonths(1)
        val endDate = LocalDate.now()
        val orders = client.loadOperations(startDate, endDate, limit = 50)
        println("Recent Buy Orders: ${orders.buyorders.size}")
        println("Recent Sell Orders: ${orders.sellorders.size}")

        // Load transfer / deposit & withdrawal history
        val transfers = client.loadTransfers(startDate, endDate)
        println("Send Transactions: ${transfers.sendtransactions.size}")
        println("Receive Transactions: ${transfers.receivetransactions.size}")
    }
}
```

---

### 3. Full-Access API (`CoinspotFAApiClient`)

Requires a Full-Access API Key and Secret with trading permissions.

```kotlin
import dym.coins.coinspot.client.CoinspotFAApiClient
import dym.coins.coinspot.domain.AssetType
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal

fun main() = runBlocking {
    val apiKey = System.getenv("COINSPOT_FA_KEY")
    val apiSecret = System.getenv("COINSPOT_FA_SECRET")

    CoinspotFAApiClient(apiKey, apiSecret).use { client ->
        val fromCoin = AssetType.of("USDT")
        val toCoin = AssetType.of("BTC")
        val amount = BigDecimal("100.00")

        // Get a swap quote
        val swapRate = client.swapQuote(fromCoin, toCoin, amount)
        println("Estimated rate to swap $amount ${fromCoin.code} to ${toCoin.code}: $swapRate")

        // Get a sell quote (to AUD)
        val sellRate = client.sellQuote(fromCoin, amount)
        println("Estimated rate to sell $amount ${fromCoin.code}: $sellRate")

        // Execute an instant swap
        val result = client.swapNow(
            from = fromCoin,
            to = toCoin,
            amount = amount,
            rate = swapRate,
            treshold = BigDecimal("0.01") // 1% slippage threshold
        )
        println("Swapped ${result.amount} ${result.from.code} for ${result.total} ${result.to.code} at rate ${result.rate}")
    }
}
```

---

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

---

## Building from Source

To compile and build the project:

```bash
mvn clean package
```

To run unit tests:

```bash
mvn test
```

> **Note on Integration Tests**: Integration tests require valid API credentials. Set environment variables `RO_KEY`, `RO_SECRET`, `FA_KEY`, and `FA_SECRET` to run integration tests against the live CoinSpot API.

---

## License

This project is available under the Apache License 2.0 terms.

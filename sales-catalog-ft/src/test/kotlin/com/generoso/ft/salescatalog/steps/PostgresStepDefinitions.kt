package com.generoso.ft.salescatalog.steps

import com.generoso.ft.salescatalog.utils.PostgresDao
import com.generoso.salescatalog.entity.Product
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class PostgresStepDefinitions @Autowired constructor(
    private val postgresDao: PostgresDao
) {

    @And("product table has records:")
    fun productIsPresentOnDatabase(table: DataTable) {
        table.asMaps().forEach { row ->
            val product = Product(
                productId = UUID.fromString(row["id"]),
                name = row["name"],
                description = row["description"],
                price = row["price"]?.toBigDecimal(),
                quantity = row["quantity"]?.toLong()!!,
                salesUserId = UUID.fromString(row["salesUserId"]),
                isReserved = false,
                isSold = false,
                isDeleted = false,
                created = LocalDateTime.now()
            )

            if (row.containsKey("isDeleted")) {
                product.isDeleted = row["isDeleted"].toBoolean()
            }

            postgresDao.insert(product)
        }
    }

    @And("assert product table has record:")
    fun assertProductTableHasRecord(table: DataTable) {
        val row: Map<String, String> = table.asMaps()[0]
        val products = postgresDao.findAllProducts()

        assertThat(products.size).isEqualTo(1)

        val product = products[0]

        assertThat(product.productId).isNotNull()
        assertThat(product.name).isEqualTo(row["name"])
        assertThat(product.description).isEqualTo(row["description"])
        assertThat(product.price).isEqualTo(BigDecimal(row["price"]))
        assertThat(product.quantity).isEqualTo(row["quantity"]?.toLong())
        assertThat(product.isReserved).isEqualTo(row["isReserved"].toBoolean())
        assertThat(product.isSold).isEqualTo(row["isSold"].toBoolean())

        val salesUserId = row["salesUserId"]
        if (salesUserId.equals("NON_NULL")) {
            assertThat(product.salesUserId).isNotNull()
        }

        val lastUpdate = row["salesUserId"]
        if (lastUpdate.equals("NON_NULL")) {
            assertThat(product.salesUserId).isNotNull()
        } else if (lastUpdate.equals("NULL")) {
            assertThat(product.salesUserId).isNull()
        }
        assertThat(product.isDeleted).isEqualTo(row["isDeleted"].toBoolean())
    }

    @And("product table has no records")
    fun productTableHasNoRecords() = assertThat(postgresDao.findAllProducts()).isEmpty()
}
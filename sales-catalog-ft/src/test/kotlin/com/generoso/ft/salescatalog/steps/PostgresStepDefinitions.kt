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

    @And("^assert product table has (?:record|records):$")
    fun assertProductTableHasRecord(table: DataTable) {
        for ((index, row) in table.asMaps().withIndex()) {
            val products = postgresDao.findAllProducts()
            assertThat(products[index].productId).isNotNull()
            assertThat(products[index].name).isEqualTo(row["name"])
            assertThat(products[index].description).isEqualTo(row["description"])
            assertThat(products[index].price).isEqualTo(BigDecimal(row["price"]))
            assertThat(products[index].quantity).isEqualTo(row["quantity"]?.toLong())
            assertThat(products[index].isReserved).isEqualTo(row["isReserved"].toBoolean())
            assertThat(products[index].isSold).isEqualTo(row["isSold"].toBoolean())

            val salesUserId = row["salesUserId"]
            if (salesUserId.equals("NON_NULL")) {
                assertThat(products[index].salesUserId).isNotNull()
            } else {
                assertThat(products[index].salesUserId).isEqualTo(UUID.fromString(row["salesUserId"]))
            }

            val lastUpdate = row["lastUpdate"]
            if (lastUpdate.equals("NON_NULL")) {
                assertThat(products[index].lastUpdate).isNotNull()
            } else if (lastUpdate.equals("NULL")) {
                assertThat(products[index].lastUpdate).isNull()
            }

            assertThat(products[index].isDeleted).isEqualTo(row["isDeleted"].toBoolean())
            if (row.containsKey("deletedAt") && row["deletedAt"].equals("NON_NULL")) {
                assertThat(row["deletedAt"]).isNotNull()
            }

        }
    }

    @And("database is down")
    fun databaseIsDown() {
        postgresDao.stop()
    }

    @And("product table has no records")
    fun productTableHasNoRecords() = assertThat(postgresDao.findAllProducts()).isEmpty()
}
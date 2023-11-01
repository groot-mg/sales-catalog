package com.generoso.ft.salescatalog.steps

import com.generoso.salescatalog.entity.Product
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.sql.DriverManager
import java.util.*

class DatabaseStepDefinitions @Autowired constructor(
    private val embeddedPostgres: EmbeddedPostgres
) {

    @And("product table has record:")
    fun productTableHasRecord(table: DataTable) {
        val row: Map<String, String> = table.asMaps()[0]
        val products = findAllProducts()

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
    fun productTableHasNoRecords() = assertThat(findAllProducts()).isEmpty()


    private fun findAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        createConnection().use { connection ->
            val selectQuery = "SELECT * FROM products"
            connection.prepareStatement(selectQuery).use { preparedStatement ->
                preparedStatement.executeQuery().use { resultSet ->
                    while (resultSet.next()) {
                        val productId = resultSet.getObject("product_id") as UUID
                        val name = resultSet.getString("name")
                        val description = resultSet.getString("description")
                        val price = resultSet.getBigDecimal("price")
                        val quantity = resultSet.getLong("quantity")
                        val isReserved = resultSet.getBoolean("is_reserved")
                        val isSold = resultSet.getBoolean("is_sold")
                        val salesUserId = resultSet.getObject("sales_user_id") as UUID
                        val createdAt = resultSet.getTimestamp("created_at")?.toLocalDateTime()
                        val lastUpdateAt = resultSet.getTimestamp("last_update_at")?.toLocalDateTime()
                        val isDeleted = resultSet.getBoolean("is_deleted")

                        products.add(
                            Product(
                                productId = productId,
                                name = name,
                                description = description,
                                price = price,
                                quantity = quantity,
                                isReserved = isReserved,
                                isSold = isSold,
                                salesUserId = salesUserId,
                                created = createdAt,
                                lastUpdate = lastUpdateAt,
                                isDeleted = isDeleted
                            )
                        )
                    }
                }
            }
        }
        return products
    }

    private fun createConnection() = DriverManager.getConnection(embeddedPostgres.getJdbcUrl("postgres", "postgres"))
}
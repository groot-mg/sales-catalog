package com.generoso.ft.salescatalog.utils

import com.generoso.salescatalog.entity.Product
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.sql.SQLException
import java.sql.Timestamp
import java.util.*

@Component
class PostgresDao(
    private var embeddedPostgres: EmbeddedPostgres
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun setupDatabase() {
        try {
            embeddedPostgres.postgresDatabase.connection
        } catch (ex: PSQLException) {
            embeddedPostgres = EmbeddedPostgres.builder().setPort(5432).start()
        }
        createTables()
    }

    fun stop() {
        embeddedPostgres.close()
    }

    fun findAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        embeddedPostgres.postgresDatabase.connection.use { connection ->
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

    fun insert(product: Product) {
        embeddedPostgres.postgresDatabase.connection.use { connection ->
            val insert = """
                INSERT INTO products (
                    product_id, 
                    name, 
                    description, 
                    price, 
                    quantity, 
                    is_reserved, 
                    is_sold, 
                    is_deleted,
                    created_at,
                    sales_user_id
                ) 
                VALUES (?,?,?,?,?,?,?,?,?,?);
            """
            connection.prepareStatement(insert).use { preparedStatement ->
                preparedStatement.setObject(1, product.productId)
                preparedStatement.setString(2, product.name)
                preparedStatement.setString(3, product.description)
                preparedStatement.setBigDecimal(4, product.price)
                preparedStatement.setLong(5, product.quantity)
                preparedStatement.setBoolean(6, product.isReserved)
                preparedStatement.setBoolean(7, product.isSold)
                preparedStatement.setBoolean(8, product.isDeleted!!)
                preparedStatement.setTimestamp(9, Timestamp.valueOf(product.created))
                preparedStatement.setObject(10, product.salesUserId)

                preparedStatement.executeUpdate()
            }
        }
    }

    fun cleanUpTables() {
        try {
            embeddedPostgres.postgresDatabase.connection.use { connection ->
                val deleteProducts = "DELETE FROM products;"
                connection.prepareStatement(deleteProducts).use { preparedStatement ->
                    preparedStatement.executeUpdate()
                }
            }
        } catch (ex: PSQLException) {
            log.error("Error on table clean up: ${ex.message}")
        }
    }

    private fun createTables() {
        val sql = """
        CREATE TABLE IF NOT EXISTS products (
            is_deleted BOOLEAN NOT NULL,
            is_reserved BOOLEAN NOT NULL,
            is_sold BOOLEAN NOT NULL,
            price NUMERIC(10,2) NOT NULL,
            created_at TIMESTAMP(6) NOT NULL,
            last_update_at TIMESTAMP(6),
            quantity BIGINT NOT NULL,
            product_id UUID NOT NULL,
            sales_user_id UUID NOT NULL,
            name VARCHAR(100) NOT NULL,
            description VARCHAR(256),
            PRIMARY KEY (product_id)
        )
    """.trimIndent()

        try {
            embeddedPostgres.postgresDatabase.connection.use { connection ->
                connection.prepareStatement(sql).use { statement ->
                    statement.execute()
                    log.info("Table 'products' created successfully")
                }
            }
        } catch (e: SQLException) {
            log.error("Error creating table 'products': ${e.message}")
        }
    }
}
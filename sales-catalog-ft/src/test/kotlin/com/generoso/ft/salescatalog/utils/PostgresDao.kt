package com.generoso.ft.salescatalog.utils

import com.generoso.salescatalog.entity.Product
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.springframework.stereotype.Component
import java.sql.DriverManager
import java.sql.Timestamp
import java.util.*

@Component
class PostgresDao(
    private val embeddedPostgres: EmbeddedPostgres
) {

    fun findAllProducts(): List<Product> {
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

    fun insert(product: Product) {
        createConnection().use { connection ->
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

                preparedStatement.executeUpdate();
            }
        }
    }

    fun cleanUpTables() {
        createConnection().use { connection ->
            val deleteProducts = "DELETE FROM products;"
            connection.prepareStatement(deleteProducts).use { preparedStatement ->
                preparedStatement.executeUpdate()
            }
        }
    }

    private fun createConnection() = DriverManager.getConnection(embeddedPostgres.getJdbcUrl("postgres", "postgres"))

}
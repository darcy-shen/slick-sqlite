package com.example
import slick.driver.SQLiteDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

object HelloSlick {
  def main(args: Array[String]) = {

    import com.example.Suppliers._
    import com.example.Coffees._

    val url = "jdbc:sqlite:/Users/mfirry/my-db-file.sqlite"
    val driver = "org.sqlite.JDBC"

    val db = Database.forURL(url, driver)

    val setup = DBIO.seq(
      // Create the tables, including primary and foreign keys
      (suppliers.schema ++ coffees.schema).create,

      // Insert some suppliers
      suppliers += (101, "Acme, Inc.",      "99 Market Street", "Groundsville", "CA", "95199"),
      suppliers += ( 49, "Superior Coffee", "1 Party Place",    "Mendocino",    "CA", "95460"),
      suppliers += (150, "The High Ground", "100 Coffee Lane",  "Meadows",      "CA", "93966"),
      // Equivalent SQL code:
      // insert into SUPPLIERS(SUP_ID, SUP_NAME, STREET, CITY, STATE, ZIP) values (?,?,?,?,?,?)

      // Insert some coffees (using JDBC's batch insert feature, if supported by the DB)
      coffees ++= Seq(
        ("Colombian",         101, 7.99, 0, 0),
        ("French_Roast",       49, 8.99, 0, 0),
        ("Espresso",          150, 9.99, 0, 0),
        ("Colombian_Decaf",   101, 8.99, 0, 0),
        ("French_Roast_Decaf", 49, 9.99, 0, 0)
      )
      // Equivalent SQL code:
      // insert into COFFEES(COF_NAME, SUP_ID, PRICE, SALES, TOTAL) values (?,?,?,?,?)
    )

    val setupFuture = db.run(setup)

  }
}

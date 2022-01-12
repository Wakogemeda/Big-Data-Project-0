import java.sql.DriverManager
import java.sql.Connection
import java.util.Scanner
import scala.util.matching.Regex
import java.io.File
import java.io.PrintWriter
import java.util.Calendar

/** A Scala JDBC connection example by Alvin Alexander,
  * https://alvinalexander.com
  */
/** A Scala JDBC connection example by Alvin Alexander,
  * https://alvinalexander.com
  */
object JDBCEx {

  var current_balance: Int = 0
  var account_number = 0
  var phone_number = ""
  var last_name = ""
  var first_name = ""
  var user_input = ""
  var user_name = ""
  var user_pass = ""
  var acct_type = ""

  def main(args: Array[String]): Unit = {
    login()
    //var o = open()
    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url =
      "jdbc:mysql://localhost:3306/freemans_capitol" // Modify for whatever port you are running your DB on
    val username = "root"
    val password = "70Sevenkawkroot!" // Update to include your password
    val log = new PrintWriter(new File("query.log"))

    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      //statement.executeUpdate(o)
      //var d = deposit()
      details()
      //statement.executeUpdate(d)
      val resultSet = statement.executeQuery("SELECT * FROM checking_accounts")

      // while (resultSet.next()) {
      //   print(
      //     resultSet.getString(1) + " " + resultSet.getString(
      //       2
      //     ) + " " + resultSet.getString(3)
      //   );
      //   println()
      // }
    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close()
  }

// Login
  def login(): Unit = {
    var s = new Scanner(System.in)
    print("\n\nLogin --\nEnter your username: ")
    user_name = s.nextLine()
    print("Enter your password: ")
    user_pass = s.nextLine()

    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url =
      "jdbc:mysql://localhost:3306/freemans_capitol" // Modify for whatever port you are running your DB on
    val username = "root"
    val password = "70Sevenkawkroot!" // Update to include your password
    val log = new PrintWriter(new File("query.log"))
    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()

      val is_user = statement.executeQuery(
        "SELECT * FROM checking_accounts WHERE username=" + "'" + user_name + "' " + "AND password=" + "'" + user_pass + "'" + ";"
      )

      if (is_user.next()) {

        var s = new Scanner(System.in)

        print(
          "\n\nOkay " + is_user.getString(
            1
          ) + " what's next?\n\nMake a deposit  [D]\n" +
            "Withdraw funds  [W]\n__ "
        )
        var user_input = s.nextLine()

        if (user_input == "d" || user_input == "D") {
          member_deposit()
        } else if (user_input == "w" || user_input == "W") {
          member_withdrawl()
        }
      } else {
        print(
          "\nYou don't appear to have an account, would you like to open one? [Y/N] "
        )
        var open_acct = s.nextLine()
        if (open_acct == "y" || user_input == "Y") {
          open()
        } else {
          print("Thank you, goodbye\n\n")

        }
      }

    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close()

  }

  // Open a new account
  def open(): Unit = {
    var s = new Scanner(System.in)

    print("\n\nNew Account -- ")

    print("\nEnter your first name: ")
    first_name = s.nextLine()

    print("Enter your last name: ")
    last_name = s.nextLine()

    print("Enter your Phone number: ")
    phone_number = s.nextLine()

    print("Choose your username: ")
    user_name = s.nextLine()

    print(
      "\nAccount Type --\nWould you like to open a checking or savings accout? [C/S] "
    )
    acct_type = s.nextLine()

    print("Enter your initial deposit: ")
    current_balance = s.nextInt()

    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url =
      "jdbc:mysql://localhost:3306/freemans_capitol" // Modify for whatever port you are running your DB on
    val username = "root"
    val password = "70Sevenkawkroot!" // Update to include your password
    val log = new PrintWriter(new File("query.log"))
    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()

      if (acct_type == "b" || acct_type == "B") {

        // INSERT INTO BOTH
        val resultSet = statement.executeUpdate(
          "INSERT INTO checking_accounts(first_name, last_name, current_balance, phone_number) OUTPUT inserted.first_name, inserted.last_name, inserted.current_balance, inserted.phone_number INTO savings_accounts VALUES(" + "'" + first_name + "', " + "'" + last_name + "', " + "'" + current_balance + "', " + "'" + phone_number + "', " + "), (" + "'" + first_name + "', " + "'" + last_name + "', " + "'" + current_balance + "', " + "'" + phone_number + "', " + ");"
        )
        details()

      } else if (acct_type == "c" || acct_type == "C") {

        // INSERT INTO CHECKING
        val resultSet = statement.executeUpdate(
          "INSERT INTO checking_accounts(first_name, last_name, current_balance, phone_number, username) VALUES(" + "'" + first_name + "', " + "'" + last_name + "', " + "'" + current_balance + "', " + "'" + phone_number + "', " + "'" + user_name + "'" + ");"
        )
        details()
        // print(
        //   "INSERT INTO checking_accounts(first_name, last_name, current_balance, phone_number, username) VALUES(" + "'" + first_name + "', " + "'" + last_name + "', " + "'" + current_balance + "', " + "'" + phone_number + "', " + "'" + user_name + "'" + ");"
        // )

      } else if (acct_type == "s" || acct_type == "S") {

        // INSERT INTO SAVINGS
        val resultSet = statement.executeUpdate(
          "INSERT INTO savings_accounts(first_name, last_name, current_balance, phone_number, username) VALUES(" + "'" + first_name + "', " + "'" + last_name + "', " + "'" + current_balance + "', " + "'" + phone_number + "', " + "'" + user_name + "'" + ");"
        )
        details()

      } else {
        print(acct_type + "Thank you!!!")
        details()
      }

    } catch {
      case e: Exception => e.printStackTrace
    }
    //connection.close()

  }

  // Account details here
  def details(): Unit = {

    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url =
      "jdbc:mysql://localhost:3306/freemans_capitol" // Modify for whatever port you are running your DB on
    val username = "root"
    val password = "70Sevenkawkroot!" // Update to include your password
    val log = new PrintWriter(new File("query.log"))
    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()

      val is_user = statement.executeQuery(
        "SELECT * FROM checking_accounts WHERE username=" + "'" + user_name + "' " + "AND password=" + "'" + user_pass + "'" + ";"
      )

      if (is_user.next()) {

        var s = new Scanner(System.in)

        print(
          "\n\nOkay " + is_user.getString(
            1
          ) + " what's next?\n\nMake a deposit  [D]\n" +
            "Withdraw funds  [W]\nClose account   [C]\n__ "
        )
        var user_input = s.nextLine()

        if (user_input == "d" || user_input == "D") {
          member_deposit()
        } else if (user_input == "c" || user_input == "C") {
          close()
        } else {
          member_withdrawl()
        }
      }

    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close()

  }

  // Used to add money to a checking account
  def member_deposit(): Unit = {
    var s = new Scanner(System.in)

    print(
      "\n\nMember Deposit -- \nDeposit in checking [C]\nDeposit in savings  [S]\n__ "
    )
    var which_acct = s.nextLine()

    print("\n\nHow much would you like to deposit? ")
    var deposit_amt = s.nextInt()

    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url =
      "jdbc:mysql://localhost:3306/freemans_capitol" // Modify for whatever port you are running your DB on
    val username = "root"
    val password = "70Sevenkawkroot!" // Update to include your password
    val log = new PrintWriter(new File("query.log"))
    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      if (which_acct == "c" || which_acct == "C") {
        // create the statement, and run the select query
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(
          "SELECT * FROM checking_accounts WHERE username=" + "'" + user_name + "'"
        )
        log.write(
          Calendar
            .getInstance()
            .getTimeInMillis + " - Excecuting 'SELECT * FROM checking_accounts WHERE username = " + "'" + user_name + "'" + ";'\n"
        )
        resultSet.next();
        var curr_bal = resultSet.getString(3).toInt
        var total_deposit = (deposit_amt + curr_bal)

        statement.executeUpdate(
          "UPDATE checking_accounts SET current_balance = " + "'" + total_deposit + "'" + " WHERE username =" + "'" + user_name + "'" + ";"
        )
        println(
          "\n------\n\nAwesome - Your new checking balance is $" + total_deposit
        )
        details()
        // Get user input after deposit

        var s = new Scanner(System.in)
        print(
          "\n\nNext steps --\nMake a deposit    [D]\n" +
            "Make a withdrawl  [W]\n__ "
        )
        var user_input = s.nextLine()
      } else {
        // create the statement, and run the select query
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(
          "SELECT * FROM savings_accounts WHERE username=" + "'" + user_name + "'"
        )
        log.write(
          Calendar
            .getInstance()
            .getTimeInMillis + " - Excecuting 'SELECT * FROM checking_accounts WHERE username = " + "'" + user_name + "'" + ";'\n"
        )
        resultSet.next();
        var curr_savings_bal = resultSet.getString(4).toInt
        var total_savings_deposit = (deposit_amt + curr_savings_bal)

        statement.executeUpdate(
          "UPDATE savings_accounts SET current_balance = " + "'" + total_savings_deposit + "'" + " WHERE username =" + "'" + user_name + "'" + ";"
        )
        println(
          "\n------\n\nAwesome - Your new savings balance is $" + total_savings_deposit
        )
        details()
        // Get user input after deposit
        var s = new Scanner(System.in)
        print(
          "\n\nNext Steps --\nMake a deposit    [D]\n" +
            "Make a withdrawl  [W]\n__ "
        )
        var user_input = s.nextLine()
      }

    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close()

  }

  // Used to add money to a checking account
  def member_withdrawl(): Unit = {
    var s = new Scanner(System.in)

    print(
      "\n\nMember Withdrawl -- \nWithdraw from checking [C]\nWithdraw from savings  [S]\n__ "
    )
    var which_acct = s.nextLine()

    print("\n\nHow much would you like to withdraw? ")
    var withdrawl_amt = s.nextInt()

    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url =
      "jdbc:mysql://localhost:3306/freemans_capitol" // Modify for whatever port you are running your DB on
    val username = "root"
    val password = "70Sevenkawkroot!" // Update to include your password
    val log = new PrintWriter(new File("query.log"))

    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      if (which_acct == "c" || which_acct == "C") {
        // create the statement, and run the select query
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(
          "SELECT * FROM checking_accounts WHERE username=" + "'" + user_name + "'"
        )
        log.write(
          Calendar
            .getInstance()
            .getTimeInMillis + " - Excecuting 'SELECT * FROM checking_accounts WHERE username = " + "'" + user_name + "'" + ";'\n"
        )
        resultSet.next();
        var curr_bal = resultSet.getString(3).toInt
        var total_withdrawl = (curr_bal - withdrawl_amt)

        statement.executeUpdate(
          "UPDATE checking_accounts SET current_balance = " + "'" + total_withdrawl + "'" + " WHERE username =" + "'" + user_name + "'" + ";"
        )
        println(
          "\n------\n\nAwesome - Your new checking balance is $" + total_withdrawl
        )
        details()
        // Get user input after deposit

        var s = new Scanner(System.in)
        print(
          "\n\nNext steps --\nMake a deposit    [D]\n" +
            "Make a withdrawl  [W]\n__ "
        )
        var user_input = s.nextLine()
      } else {
        // create the statement, and run the select query
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(
          "SELECT * FROM savings_accounts WHERE username=" + "'" + user_name + "'"
        )
        log.write(
          Calendar
            .getInstance()
            .getTimeInMillis + " - Excecuting 'SELECT * FROM checking_accounts WHERE username = " + "'" + user_name + "'" + ";'\n"
        )
        resultSet.next();
        var curr_savings_bal = resultSet.getString(4).toInt
        var total_savings_withdrawl = (curr_savings_bal - withdrawl_amt)

        statement.executeUpdate(
          "UPDATE savings_accounts SET current_balance = " + "'" + total_savings_withdrawl + "'" + " WHERE username =" + "'" + user_name + "'" + ";"
        )
        println(
          "\n------\n\nAwesome - Your new savings balance is $" + total_savings_withdrawl
        )
        details()
        // Get user input after deposit
        var s = new Scanner(System.in)
        print(
          "\n\nNext Steps --\nMake a deposit    [D]\n" +
            "Make a withdrawl  [W]\n__ "
        )
        var user_input = s.nextLine()
      }

    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close()

  }

  // Used to add money to a checking account
  def deposit(): Unit = {
    var s = new Scanner(System.in)

    print(
      "\n\nGreat! Which account is this going into? \n'C' for checking 'S' for savings (C/S): "
    )
    var which_acct = s.nextLine()

    print("\n\nHow much would you like to deposit? ")
    var deposit_amt = s.nextInt()

    println("\nDepositing $" + deposit_amt + " into your account now.\n\n")

    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url =
      "jdbc:mysql://localhost:3306/freemans_capitol" // Modify for whatever port you are running your DB on
    val username = "root"
    val password = "70Sevenkawkroot!" // Update to include your password
    val log = new PrintWriter(new File("query.log"))

    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      var total_deposit = (current_balance + deposit_amt)

      print(
        "UPDATE checking_accounts SET current_balance = " + "'" + total_deposit + "'" + " WHERE phone_number =" + "'" + phone_number + "'" + ";"
      )

      if (which_acct == "c" || which_acct == "C") {
        statement.executeUpdate(
          "UPDATE checking_accounts SET current_balance = " + "'" + total_deposit + "'" + "WHERE phone_number =" + "'" + phone_number + "'" + ";"
        )
      } else {
        statement.executeUpdate(
          // "UPDATE savings_accounts SET current_balance = " + "'" + total_deposit + "'" + "WHERE phone_number =" + "'" + phone_number + "'" + ";"

          "INSERT INTO savings_accounts(first_name, last_name, phone_number, current_balance) VALUES(" + "'" + first_name + "', " + "'" + last_name + "', " + "'" + phone_number + "', " + "'" + current_balance + "'" + ");"
        )
      }
      // statement.executeUpdate(
      //   "UPDATE checking_accounts SET current_balance = " + "'" + total_deposit + "'" + "WHERE phone_number =" + "'" + phone_number + "'" + ";"
      // )

    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close()

  }

  // Close account
  def close(): Unit = {
    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url =
      "jdbc:mysql://localhost:3306/freemans_capitol" // Modify for whatever port you are running your DB on
    val username = "root"
    val password = "70Sevenkawkroot!" // Update to include your password
    val log = new PrintWriter(new File("query.log"))

    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      print(user_name)

      statement.executeUpdate(
        "DELETE FROM checking_accounts WHERE username =" + "'" + user_name + "'" + ";"
      )

    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close()
    System.exit(0)

  }

  // Used to add money to a checking account
  def deposit_savings(): Unit = {
    var s = new Scanner(System.in)

    print("\n\nHow much would you like to deposit? ")
    var savings_deposit_amt = s.nextInt()

    println(
      "\nDepositing $" + savings_deposit_amt + " into your account now.\n\n"
    )

    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url =
      "jdbc:mysql://localhost:3306/freemans_capitol" // Modify for whatever port you are running your DB on
    val username = "root"
    val password = "70Sevenkawkroot!" // Update to include your password
    val log = new PrintWriter(new File("query.log"))

    var connection: Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      var total_savings_deposit = (savings_deposit_amt + current_balance)

      print(
        "UPDATE checking_accounts SET current_balance = " + "'" + total_savings_deposit + "'" + " WHERE phone_number =" + "'" + phone_number + "'" + ";"
      )

      statement.executeUpdate(
        "UPDATE checking_accounts SET current_balance = " + "'" + total_savings_deposit + "'" + "WHERE phone_number =" + "'" + phone_number + "'" + ";"
      )

    } catch {
      case e: Exception => e.printStackTrace
    }
    connection.close()
    log.close()

  }

}

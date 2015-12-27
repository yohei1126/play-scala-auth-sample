package dao

import models.User

object UserDAO {
  private val ValidName1 = "test1@example.com"
  private val ValidName2 = "test2@example.com"
  private val ValidPassword = "test"

  def findOneByUsername(username: String) :Option[User] =
    username match {
      case ValidName1 => Some(User(username,""))
      case ValidName2 => Some(User(username,""))
      case _ => None
    }

  def validate(user: User): Boolean =
    user match {
      case User(ValidName1,ValidPassword) => true
      case User(ValidName2,ValidPassword) => true
      case _ => false
    }
}

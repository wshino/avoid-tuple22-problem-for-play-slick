package models.slick

import play.api.Play.current
import scala.slick.driver.H2Driver.simple._

import play.api.db.DB

/**
 * Created with IntelliJ IDEA.
 * User: shinohara_wataru
 * Date: 13/08/12
 * Time: 18:02
 * To change this template use File | Settings | File Templates.
 */
object BaseModel {

  def withSession[T](f: (Session => T)): T = Database.forDataSource(DB.getDataSource("default")).withSession(f)

}

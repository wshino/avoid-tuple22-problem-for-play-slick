import models.slick.Reports
import play.api.db.DB
import play.api.{Application, GlobalSettings}
import scala.slick.driver.H2Driver.simple._
import play.api.Play.current

/**
 * Created with IntelliJ IDEA.
 * User: shinohara_wataru
 * Date: 13/08/21
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Database.forDataSource(DB.getDataSource()).withSession {
      implicit ses: Session =>
        Reports.ddl.create
    }
  }

}

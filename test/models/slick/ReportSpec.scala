package models.slick

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import java.sql.{Date, Timestamp}
import scala.slick.driver.H2Driver.simple._
import scala.slick.ast._

import scala.slick.ast.Util._
import org.joda.time.DateTime

/**
 * Created with IntelliJ IDEA.
 * User: shinohara_wataru
 * Date: 13/08/21
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
class ReportSpec extends Specification {

  "Report" should {

    val rc = ReportClick.apply(0,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L)
    val articleId = 1L
    val r = Report.apply(id = 1L, articleId = articleId, referer = "none", clickAm = rc, clickPm = rc, date = new Date(2013 - 1900, 2,6))

    "insert" in new WithApplication {
      BaseModel.withSession {implicit session =>
        Reports.insert(r)
        Reports.insert(r)
        Reports.findAll().size mustEqual 2
      }
    }

    "count" in new WithApplication() {
      BaseModel.withSession {implicit session =>
        Reports.insert(r)
        Reports.count() mustEqual 1
      }
    }

    "countByArticleId" in new WithApplication() {
      BaseModel.withSession {implicit session =>
        Reports.insert(r)
        val res = Reports.countByArticleId(articleId)
        res mustEqual 154

        Reports.insert(r)
        val res2 = Reports.countByArticleId(articleId)
        res2 mustEqual 308
      }
    }

    "findRefererByDate" in new WithApplication() {
      BaseModel.withSession {implicit session =>
        Reports.insert(r)
        Reports.insert(r.copy(referer = "community"))
        Reports.insert(r.copy(referer = "community"))
        Reports.insert(r.copy(referer = "game"))

        val date = new DateTime(2013, 3, 6, 0, 0, 0)

        val res = Reports.findRefererByDate(date)
        // res は List[(String, List[Report])]
        res.size mustEqual 3
        res.head._1 mustEqual "community"
        res.head._2.size mustEqual 2
        res.last._1 mustEqual "none"
      }
    }


  }
}

package models.slick

import scala.slick.ast._
import scala.slick.ast.Util._
import scala.slick.driver.H2Driver.simple._
import scala.language.postfixOps
import org.joda.time.DateTime
import java.sql.Date
import annotation.tailrec

/**
 * Created with IntelliJ IDEA.
 * User: shinohara_wataru
 * Date: 13/08/21
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
case class ReportClick(click00: Long,
                       click01: Long,
                       click02: Long,
                       click03: Long,
                       click04: Long,
                       click05: Long,
                       click06: Long,
                       click07: Long,
                       click08: Long,
                       click09: Long,
                       click10: Long,
                       click11: Long
                        )

case class Report(id: Long, articleId: Long, referer: String, clickAm: ReportClick, clickPm: ReportClick, date: java.sql.Date)



object Reports extends Table[Long]("report") {

  def id = column[Long]("id", O PrimaryKey, O AutoInc, O DBType "bigint(20)")

  def articleId = column[Long]("article_id", O DBType "bigint(20)")

  def referer = column[String]("referer", O DBType "varchar(64)")

  def click00 = column[Long]("click00")

  def click01 = column[Long]("click01")

  def click02 = column[Long]("click02")

  def click03 = column[Long]("click03")

  def click04 = column[Long]("click04")

  def click05 = column[Long]("click05")

  def click06 = column[Long]("click06")

  def click07 = column[Long]("click07")

  def click08 = column[Long]("click08")

  def click09 = column[Long]("click09")

  def click10 = column[Long]("click10")

  def click11 = column[Long]("click11")

  def click12 = column[Long]("click12")

  def click13 = column[Long]("click13")

  def click14 = column[Long]("click14")

  def click15 = column[Long]("click15")

  def click16 = column[Long]("click16")

  def click17 = column[Long]("click17")

  def click18 = column[Long]("click18")

  def click19 = column[Long]("click19")

  def click20 = column[Long]("click20")

  def click21 = column[Long]("click21")

  def click22 = column[Long]("click22")

  def click23 = column[Long]("click23")

  def date = column[java.sql.Date]("date", O DBType "date")

  def * = id

  def all = (
    id,
    articleId,
    referer,
    (click00, click01, click02, click03, click04, click05, click06, click07, click08, click09, click10, click11),
    (click12, click13, click14, click15, click16, click17, click18, click19, click20, click21, click22, click23),
    date
    )

  override def create_* =
    all.shaped.packedNode.collect {
      case Select(Ref(IntrinsicSymbol(in)), f: FieldSymbol) if in == this => f
    }.toSeq.distinct

  def count()(implicit session: Session) = {
    val q1 = Reports.map(_.all)
    q1.list().size
  }

  def insert(report: Report)(implicit session: Session) = {
    // カウント数を取得する
    val inc = this.count() + 1
    val data = (inc.toLong, report.articleId, report.referer,
      (report.clickAm.click00, report.clickAm.click01, report.clickAm.click02, report.clickAm.click03, report.clickAm.click04,
        report.clickAm.click05, report.clickAm.click06, report.clickAm.click07, report.clickAm.click08, report.clickAm.click09,
        report.clickAm.click10, report.clickAm.click11),
      (report.clickPm.click00, report.clickPm.click01, report.clickPm.click02, report.clickPm.click03, report.clickPm.click04,
        report.clickPm.click05, report.clickPm.click06, report.clickPm.click07, report.clickPm.click08, report.clickPm.click09,
        report.clickPm.click10, report.clickPm.click11),
      report.date)
    Reports.all.shaped.insert(data)
    inc.toLong
  }

  def findAll()(implicit session: Session) = {
    val q1 = Reports.map(_.all)
    q1.list()
  }

  def countByArticleId(l: Long)(implicit session: Session) = {
    val res = Reports.map((_.all)).filter(r => r._2 === l).list()
    sum(res, 0L)
  }

  def sortByClickSum(l: List[(Long, Long, String, (Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long)
    , (Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long), java.sql.Date)]) = {
    val res = l.map{ r =>
      val click = r._4.productIterator.toList ::: r._5.productIterator.toList
      val total = click.reduceLeftOption((z, n) => z.asInstanceOf[Long] + n.asInstanceOf[Long]) match {
        case Some(x: Long) => x
        case _ => 0
      }

      (r._2, total)
    }
    res.sortBy(_._2).reverse
  }

  @tailrec
  private def sum(l: List[(Long, Long, String, (Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long)
    , (Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long, Long), java.sql.Date)], res: Long): Long = {
    if (l.isEmpty) res
    else {
      val click = l.head._4.productIterator.toList ::: l.head._5.productIterator.toList
      val r = click.reduceLeftOption((z, n) => z.asInstanceOf[Long] + n.asInstanceOf[Long])
      r match {
        case Some(x: Long) => sum(l.tail, x + res)
        case _ => 0
      }
    }
  }


  /**
   * 日付でフィルタリングした中でrefererをkeyにしたreportのリストを返す, slickで groupby できないのでコレクションでやってる
   * TODO いつかはやりたい
   */
  def findRefererByDate(date: DateTime)(implicit session: Session) = {
    val start = new DateTime(date.getYear, date.getMonthOfYear, date.getDayOfMonth, 0, 0, 0)
    val end = new DateTime(date.getYear, date.getMonthOfYear, date.getDayOfMonth, 23, 59, 59)

    val q = Reports.map((_.all))
      .filter(r => r._6 >= new Date(start.toDate.getTime))
      .filter(r => r._6 <= new Date(end.toDate.getTime))
    val res = q.list().groupBy(x => x._3)
    res.toList.sortBy(_._1)
  }


}

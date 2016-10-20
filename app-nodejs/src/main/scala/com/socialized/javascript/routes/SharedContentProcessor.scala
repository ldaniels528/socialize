package com.socialized.javascript.routes

import org.scalajs.nodejs.console
import org.scalajs.sjs.OptionHelper._

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

/**
  * Shared Content Processor
  * @author lawrence.daniels@gmail.com
  */
object SharedContentProcessor {
  private[this] val expandables = List("&quot;" -> "\"", "&#x27;" -> "'") ::: (27 to 383).map(n => s"&#$n;" -> f"$n%c").toList

  /**
    * Parses Social media metadata
    * @param metadata the given news/article URL
    * @return the aggregated Social media metadata
    */
  def parseMetaData(metadata: Map[String, String]): Option[SharedContent] = {
    // gather as much meta data as possible
    val results = metadata.toSeq // extractTitle(doc) ++ extractMetaData(doc, keyName = "name") ++ extractMetaData(doc, keyName = "property")
    val props = Map(results: _*)
    results.foreach { case (k, v) => console.log(s"meta: $k => $v") }

    // build the list of metadata sources
    val sources = (
      extractArticleMetaData(results, props) :: extractFacebookMetaData(results, props) ::
        extractGenericMetaData(results, props) :: extractLinkedInMetaData(results, props) ::
        extractOpenGraphMetaData(results, props) :: extractSailthruMetaData(results, props) ::
        extractShareaholicMetaData(results, props) :: extractTwitterMetaData(results, props) :: Nil
      ).flatten

    // if there's isn't at least one source that 80% complete or more, also include JSON_LD
    //if (!sources.exists(_.completeness >= 0.80d)) sources = extractJsonLD(url).toList ::: sources

    // create a composite web page summary; using the most complete summary as the template
    sources.sortBy(-_.completeness).headOption map { initialSummary =>
      sources.foldLeft[SharedContent](initialSummary) { (compositeSummary, summary) =>
        compositeSummary.copy(
          author = compositeSummary.author ?? summary.author,
          description = compositeSummary.description ?? summary.description,
          locale = compositeSummary.locale ?? summary.locale,
          publishedTime = compositeSummary.publishedTime ?? summary.publishedTime,
          section = compositeSummary.source ?? summary.section,
          source = compositeSummary.source ?? summary.source,
          tags = (compositeSummary.tags.getOrElse(Nil) ::: summary.tags.getOrElse(Nil)).map(_.trim.toLowerCase).distinct,
          thumbnailUrl = compositeSummary.thumbnailUrl ?? summary.thumbnailUrl,
          title = compositeSummary.title ?? summary.title,
          updatedTime = compositeSummary.updatedTime ?? summary.updatedTime,
          url = compositeSummary.url ?? summary.url
        )
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  //      HTMLx meta-data functions
  ///////////////////////////////////////////////////////////////////////////

  /**
    * Parses Google (Article, etc.) SEO metadata
    * <pre>
    * article:author => http://www.businessinsider.com/author/drake-baer
    * article:modified_time => 2015-10-28T11:12:34+00:00
    * article:publisher => businessinsider
    * article:published_time => 2015-10-28T11:10:20+00:00
    * article:section => Job Hunting
    * article:tag => Job Hunting
    * article:tag => Job Skills
    * article:tag => Unemployment
    * </pre>
    */
  private def extractArticleMetaData(results: Seq[(String, String)], props: Map[String, String]) = {
    val author = props.get("article:author")
    val tags = results.filter(_._1 == "article:tag").map(_._2).toList
    val modified_time = props.get("article:modified_time").flatMap(parseDate(_, format = "yyyy-MM-dd'T'HH:mm:ssZ"))
    val published_time = props.get("article:published_time").flatMap(parseDate(_, format = "yyyy-MM-dd'T'HH:mm:ssZ"))
    val section = props.get("article:section")
    Option(SharedContent(author = author, section = section, thumbnailUrl = None, publishedTime = published_time, tags = tags, updatedTime = modified_time))
  }

  /**
    * Parses Facebook SEO metadata
    * <pre>
    * fb:app_id => 155043519637
    * </pre>
    */
  private def extractFacebookMetaData(results: Seq[(String, String)], props: Map[String, String]): Option[SharedContent] = None

  /**
    * Parses Generic (legacy) SEO metadata
    * <pre>
    * author => Drake Baer
    * date => 2015-10-22
    * description => Learning strategies that really work.
    * news_keywords => Learning, Cognitive Function, Education, Productivity, Psychology, Features, Drake Baer,
    * title => 4 strategies for remembering everything you learn
    * </pre>
    */
  private def extractGenericMetaData(results: Seq[(String, String)], props: Map[String, String]) = {
    val author = props.get("author")
    val date = props.get("date").flatMap(parseDate(_, format = "yyyy-MM-dd"))
    val description = props.get("description")
    val keywords = props.get("news_keywords").map(parseTags)
    val title = props.get("title")
    Option(SharedContent(author = author, description = description, thumbnailUrl = None, publishedTime = date, tags = keywords, title = title))
  }

  /**
    * Parses LinkedIn SEO metadata
    * <pre>
    * linkedin:owner => mid:1d5f7b
    * </pre>
    */
  private def extractLinkedInMetaData(results: Seq[(String, String)], props: Map[String, String]) = None

  /**
    * Parses Open Graph SEO metadata
    * <pre>
    * og:locale => en_US
    * og:type => article
    * og:title => Tech Unemployment Rises In Some Categories - Dice Insights
    * og:description => The technology industry’s unemployment rate crept up to 3.0 percent in the third quarter of 2015, according to the U.S. Bureau of Labor Statistics (BLS).
    * og:url => http://insights.dice.com/2015/10/28/tech-unemployment-rises-in-some-categories/
    * og:site_name => Dice Insights
    * og:updated_time => 2015-10-28T11:12:34+00:00
    * og:image => http://insights.dice.com/wp-content/uploads/2015/10/2015-Q3_Full_Report_Image1.png
    * </pre>
    */
  private def extractOpenGraphMetaData(results: Seq[(String, String)], props: Map[String, String]) = {
    for {
      image <- props.get("og:image")
      description = props.get("og:description")
      locale = props.get("og:locale")
      site_name = props.get("og:site_name")
      title = props.get("og:title")
      url = props.get("og:url")
      updated_time = props.get("og:updated_time").flatMap(parseDate(_, format = "yyyy-MM-dd'T'HH:mm:ssZ"))
    } yield SharedContent(description = description, locale = locale, thumbnailUrl = image, title = title, source = site_name, url = url, updatedTime = updated_time)
  }

  /**
    * Parses Sailthru SEO metadata
    * <pre>
    * sailthru.author => Drake Baer
    * sailthru.date => 2015-10-22
    * sailthru.description => Learning strategies that really work.
    * sailthru.image.full => http://static5.businessinsider.com/image/56292a629dd7cc1b008c4139/4-strategies-for-remembering-everything-you-learn.jpg
    * sailthru.image.thumb => http://static2.businessinsider.com/image/56292a629dd7cc1b008c4139-50-50/4-strategies-for-remembering-everything-you-learn.jpg
    * sailthru.tags => Learning, Cognitive Function, Education, Productivity, Psychology, Features, Drake Baer,
    * sailthru.title => 4 strategies for remembering everything you learn
    * sailthru.verticals => warroom,education,science
    * </pre>
    * @param results the given collection of key-value pairs
    */
  private def extractSailthruMetaData(results: Seq[(String, String)], props: Map[String, String]) = {
    for {
      imageThumb <- props.get("sailthru.image.thumb")
      imageFull = props.get("sailthru.image.full")
      author = props.get("sailthru.author")
      date = props.get("sailthru.date").flatMap(parseDate(_, "yyyy-MM-dd"))
      description = props.get("sailthru.description")
      tags = props.get("sailthru.tags").map(parseTags)
      title = props.get("sailthru.title")
      url = props.get("sailthru.url")
    } yield SharedContent(author = author, description = description, tags = tags, thumbnailUrl = imageFull, title = title, url = url, publishedTime = date)
  }

  /**
    * Parses Shareaholic SEO metadata
    * <pre>
    * shareaholic:site_name => Dice Insights
    * shareaholic:language => en-US
    * shareaholic:url => http://insights.dice.com/2015/10/28/tech-unemployment-rises-in-some-categories/
    * shareaholic:keywords => job hunting, job skills, unemployment, technology, industry
    * shareaholic:article_published_time => 2015-10-28T15:10:20+00:00
    * shareaholic:article_modified_time => 2015-11-23T16:16:26+00:00
    * shareaholic:shareable_page => true
    * shareaholic:article_author_name => Dice Staff
    * shareaholic:site_id => 95e918a36eb505f905f5ad1d1c1400ee
    * shareaholic:wp_version => 7.6.1.8
    * shareaholic:image => http://insights.dice.com/wp-content/uploads/2015/10/2015-Q3_Full_Report_Image1-1024x665.png
    * </pre>
    */
  private def extractShareaholicMetaData(results: Seq[(String, String)], props: Map[String, String]) = {
    for {
      image <- props.get("shareaholic:image")
      keywords = props.get("shareaholic:keywords").map(parseTags)
      language = props.get("shareaholic:language")
      modified_time = props.get("shareaholic:article_modified_time").flatMap(parseDate(_, format = "yyyy-MM-dd'T'HH:mm:ssZ"))
      published_time = props.get("shareaholic:article_published_time").flatMap(parseDate(_, format = "yyyy-MM-dd'T'HH:mm:ssZ"))
      site_name = props.get("shareaholic:site_name")
      url = props.get("shareaholic:url")
    } yield SharedContent(locale = language, thumbnailUrl = image, source = site_name, url = url, tags = keywords, publishedTime = published_time, updatedTime = modified_time)
  }

  /**
    * Parses Twitter SEO metadata
    * <pre>
    * twitter:card => summary_large_image
    * twitter:creator => drake_baer
    * twitter:description => The technology industry’s unemployment rate crept up to 3.0 percent in the third quarter of 2015, according to the U.S. Bureau of Labor Statistics (BLS).
    * twitter:title => Tech Unemployment Rises In Some Categories - Dice Insights
    * twitter:domain => Dice Insights
    * twitter:image => http://insights.dice.com/wp-content/uploads/2015/10/2015-Q3_Full_Report_Image1.png
    * twitter:site => bi_strategy
    * </pre>
    */
  private def extractTwitterMetaData(results: Seq[(String, String)], props: Map[String, String]) = {
    for {
      image <- props.get("twitter:image")
      creator = props.get("twitter:creator")
      description = props.get("twitter:description")
      domain = props.get("twitter:domain")
      site = props.get("twitter:site")
      title = props.get("twitter:title")
    } yield SharedContent(author = creator, description = description, thumbnailUrl = image, title = title, source = site)
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Utility functions
  ///////////////////////////////////////////////////////////////////////////

  /**
    * Parses the given text and returns its date equivalent
    * @param text the given text string
    * @return the [[java.util.Date date]]
    */
  private def parseDate(text: String, format: String) = Option(new js.Date(js.Date.parse(text)))

  /**
    * Parses the given tag string into a collection of tags
    * @param tagList the given tag string
    * @return a collection of tags
    */
  private def parseTags(tagList: String) = tagList.trim.split("[,]").toList

  implicit def value2Option[T](value: T): Option[T] = Option(value)

  /**
    * Represents Shared Content; usually posted from a news site, etc.
    * @author lawrence.daniels@gmail.com
    */
  case class SharedContent(author: Option[String] = None,
                           description: Option[String] = None,
                           locale: Option[String] = None,
                           publishedTime: Option[js.Date] = None,
                           section: Option[String] = None,
                           source: Option[String] = None,
                           tags: Option[List[String]] = None,
                           thumbnailUrl: Option[String],
                           title: Option[String] = None,
                           updatedTime: Option[js.Date] = None,
                           url: Option[String] = None) {

    def completeness = {
      val values = Seq(author, description, locale, publishedTime, section, source, tags, thumbnailUrl, title, updatedTime, url)
      val count = values.foldLeft[Int](0) { (total, value) => total + value.map(_ => 1).getOrElse(0) }
      count / values.length.toDouble
    }

    def toJson = {
      js.Dictionary(
        "author" -> author.map(_.expandMarkup).orUndefined,
        "description" -> description.map(_.expandMarkup).orUndefined,
        "locale" -> locale.map(_.expandMarkup).orUndefined,
        "publishedTime" -> publishedTime.orUndefined,
        "section" -> section.orUndefined,
        "source" -> source.orUndefined,
        "tags" -> tags.map(js.Array(_: _*)).orUndefined,
        "thumbnailUrl" -> thumbnailUrl.orUndefined,
        "title" -> title.map(_.expandMarkup).orUndefined,
        "updatedTime" -> updatedTime.orUndefined,
        "url" -> url.orUndefined
      )
    }

  }

  /**
    * String Extensions
    * @author lawrence.daniels@gmail.com
    */
  implicit class StringExtensions(val text: String) extends AnyVal {

    def expandMarkup: String = {
      expandables.foldLeft(text) { case (s, (expandable, replacement)) =>
        s.replaceAll(expandable, replacement)
      }
    }

  }

}
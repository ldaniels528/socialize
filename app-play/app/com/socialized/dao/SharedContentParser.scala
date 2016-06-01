package com.socialized.dao

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import com.github.jsonldjava.core.{JsonLdOptions, JsonLdProcessor}
import com.github.jsonldjava.utils.JsonUtils
import com.socialized.models.SharedContent
import com.socialized.util.OptionHelper._
import play.api.Logger
import play.api.libs.json.Json

import scala.util.{Failure, Success, Try}
import scala.xml.XML

/**
  * Shared Content Parser
  * @author lawrence.daniels@gmail.com
  */
trait SharedContentParser {

  /**
    * Parses Social media metadata
    * @param url the given news/article URL
    * @return the aggregated Social media metadata
    */
  def parseMetaData(url: String): Option[SharedContent] = {
    // retrieve the document
    val doc = XML
      .withSAXParser(new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser())
      .load(url)

    // gather as much meta data as possible
    val results = extractTitle(doc) ++ extractMetaData(doc, keyName = "name") ++ extractMetaData(doc, keyName = "property")
    val props = Map(results: _*)
    results.foreach { case (k, v) => System.out.println(s"meta: $k => $v") }

    // build the list of metadata sources
    var sources = (
      extractArticleMetaData(results, props) :: extractFacebookMetaData(results, props) ::
        extractGenericMetaData(results, props) :: extractLinkedInMetaData(results, props) ::
        extractOpenGraphMetaData(results, props) :: extractSailthruMetaData(results, props) ::
        extractShareaholicMetaData(results, props) :: extractTwitterMetaData(results, props) :: Nil
      ).flatten

    // if there's isn't at least one source that 80% complete or more, also include JSON_LD
    if (!sources.exists(_.completeness >= 0.80d)) sources = extractJsonLD(url).toList ::: sources

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
    * Retrieve a collection of metadata key-value pairs
    * @param doc the given [[scala.xml.Elem document]]
    * @return the collection of key-value pairs
    */
  private def extractTitle(doc: scala.xml.Elem) = {
    (doc \\ "title").map(_.text).headOption map (title => Seq("title" -> title)) getOrElse Seq.empty
  }

  /**
    * Retrieve a collection of metadata key-value pairs
    * @param doc the given [[scala.xml.Elem document]]
    * @param keyName the name of the key (e.g. "name" or "property")
    * @return the collection of key-value pairs
    */
  private def extractMetaData(doc: scala.xml.Elem, keyName: String) = (doc \\ "meta") flatMap { node =>
    for {
      name <- (node \ s"@$keyName").map(_.text).headOption
      content <- (node \ "@content").map(_.text).headOption
    } yield (name, content)
  }

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
  //      JSON-LD functions
  ///////////////////////////////////////////////////////////////////////////

  /**
    * Parses Schema.org JSON-LD SEO metadata
    * <pre>
    * {
    * "@type": "http://schema.org/NewsArticle",
    * "http://schema.org/articleSection": "warroom",
    * "http://schema.org/creator": "Drake Baer",
    * "http://schema.org/dateCreated": {
    * "@type": "http://schema.org/Date",
    * "@value": "2015-10-22T19:03:00Z"
    * },
    * "http://schema.org/headline": "4 strategies for remembering everything you learn",
    * "http://schema.org/keywords": [ "Learning","Cognitive Function","Education","Productivity","Psychology","Features"],
    * "http://schema.org/thumbnailUrl": {
    * "@id": "http://static5.businessinsider.com/image/56292a629dd7cc1b008c4139/4-strategies-for-remembering-everything-you-learn.jpg"
    * },
    * "http://schema.org/url": {
    * "@id": "http://www.businessinsider.com/how-to-remember-everything-you-learn-2015-10"
    * }
    * }
    * <pre>
    */
  private def extractJsonLD(url: String): Option[SharedContent] = {
    // retrieve the document
    val doc = XML
      .withSAXParser(new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl().newSAXParser())
      .load(url)

    // find the script of type "application/ld+json"
    val results = (doc \\ "script") flatMap { node =>
      if ((node \ "@type").exists(_.text == "application/ld+json")) Some(readJsonLD(extractContentsData(node.text))) else None
    }

    for {
      js <- results.headOption map Json.parse
      headline = (js \ "http://schema.org/headline").asOpt[String]
      thumbnailUrl <- (js \ "http://schema.org/thumbnailUrl" \ "@id").asOpt[String]
      keywords = (js \ "http://schema.org/keywords").asOpt[List[String]]
      articleSection = (js \ "http://schema.org/articleSection").asOpt[String]
      url = (js \ "http://schema.org/url" \ "@id").asOpt[String]
      dateCreated = (js \ "http://schema.org/dateCreated" \ "@id").asOpt[String].flatMap(parseDate(_, format = "yyyy-MM-dd'T'HH:mm:ss'Z'"))
    } yield SharedContent(title = headline, thumbnailUrl = thumbnailUrl, section = articleSection, tags = keywords, url = url, publishedTime = dateCreated)
  }

  private def extractContentsData(text: String) = {
    val cdata = "//<![CDATA["
    val start = text.indexOf(cdata)
    val end = text.indexOf("//]]>")
    if (start != -1 && end != -1) {
      text.substring(start + cdata.length, end)
    }
    else text
  }

  private def readJsonLD(text: String) = {
    val jsonObject = JsonUtils.fromString(text)
    val context = new util.HashMap()
    val options = new JsonLdOptions()
    val compact = JsonLdProcessor.compact(jsonObject, context, options)
    JsonUtils.toPrettyString(compact)
  }

  ///////////////////////////////////////////////////////////////////////////
  //      Utility functions
  ///////////////////////////////////////////////////////////////////////////

  /**
    * Parses the given text and returns its date equivalent
    * @param text the given text string
    * @return the [[java.util.Date date]]
    */
  private def parseDate(text: String, format: String): Option[Date] = {
    val s = if (format.toLowerCase.endsWith("z") && text.endsWith(":00")) text.dropRight(3) + "00" else text
    Try(new SimpleDateFormat(format).parse(s)) match {
      case Success(v) => Some(v)
      case Failure(e) =>
        Logger.info(s"date error: $text [$s] -> ${e.getMessage}")
        None
    }
  }

  /**
    * Parses the given tag string into a collection of tags
    * @param tagList the given tag string
    * @return a collection of tags
    */
  private def parseTags(tagList: String) = tagList.trim.split("[,]").toList

}

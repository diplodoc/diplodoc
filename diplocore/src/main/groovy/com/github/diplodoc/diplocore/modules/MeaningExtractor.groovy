package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplocore.services.WwwService
import org.apache.commons.lang3.StringUtils
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.mllib.classification.LogisticRegressionModel
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.DoubleRDDFunctions
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/meaning-extractor')
class MeaningExtractor {

    private static final double THRESHOLD = 0.5

    @Autowired
    WwwService wwwService

    @Autowired
    PostRepository postRepository

    @RequestMapping(value = '/post/{id}/extract-text', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void extractText(@PathVariable('id') String postId) {
        assert false : 'not implemented yet'
    }

    @RequestMapping(value = '/train-model', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody String trainModel() {
        boolean first = true
        Collection<LabeledPoint> data = postRepository.findByTrainMeaningHtmlIsNotNull().collectMany { Post post ->
            Document document = wwwService.parse(post.html)

            if (first) {
                println "POST: ${post.trainMeaningHtml.replaceAll('\\s+','')}"
            }

            def result = document.select('div').collect { Element element ->
                double label

                if (sameHtml(post.trainMeaningHtml, element.outerHtml())) {
                    label = 1.0
                } else {
                    label = 0.0
                }

                if (first) {
                    String str1 = post.trainMeaningHtml.replaceAll('\\s+','')
                    String str2 = element.outerHtml().replaceAll('\\s+','')
                    int distance = StringUtils.getLevenshteinDistance(str1, str2)
                    println "ELEMENT ${label} (distandce ${distance}): ${element.outerHtml().replaceAll('\\s+','')}"
                }

                double linksCount = element.select('a').size()
                double childrenCount = element.children().size()
                double textSize = element.text().length()
                double textSize2 = textSize * textSize
                double pointsCount = element.text().toCharArray().findAll({ '.,:;?!'.contains(Character.toString(it)) }).size()

                Vector features = Vectors.dense(linksCount, childrenCount, textSize, textSize2, pointsCount)

                new LabeledPoint(label, features)
            }

            if (result.size() > 0) {
                first = false
            }

            return result
        }

        SparkConf sparkConf = new SparkConf().setAppName('/diplocore/meaning-extractor/train-model').setMaster('local')
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf)

        JavaRDD<LabeledPoint> rdd = sparkContext.parallelize(data)

        JavaRDD<LabeledPoint>[] splits = rdd.randomSplit([ 0.7, 0.3 ] as double[])
        JavaRDD<LabeledPoint> trainSet = splits[0]
        JavaRDD<LabeledPoint> testSet = splits[1]

        LogisticRegressionModel model = new LogisticRegressionWithLBFGS().run(trainSet.rdd())

        def scores = testSet.toArray().collect{ LabeledPoint point ->
            double prediction = model.predict(point.features())
            println "TEST: ${point} predicted as ${prediction}"
            if (point.label() > THRESHOLD) {
                return 1 - prediction
            } else {
                return prediction
            }
        }

        double score = scores.sum() / scores.size()

        String result = "${model.toString()}\nSCORE: ${score}"
        println result
        return result
    }

    boolean sameHtml(String html1, String html2) {
        String cleaned1 = html1.replaceAll('\\s+','')
        String cleaned2 = html2.replaceAll('\\s+','')
        int threshold = cleaned1.length() / 50

        int dist = StringUtils.getLevenshteinDistance(cleaned1, cleaned2, threshold)
        return dist > 0
    }
}

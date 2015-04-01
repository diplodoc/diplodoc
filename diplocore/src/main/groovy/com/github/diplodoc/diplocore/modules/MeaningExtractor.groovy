package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.Post
import com.github.diplodoc.diplobase.repository.mongodb.PostRepository
import com.github.diplodoc.diplocore.services.WwwService
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.mllib.classification.LogisticRegressionModel
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
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
    void trainModel() {
        JavaRDD<LabeledPoint> data = postRepository.findByTrainMeaningHtmlIsNotNull().collectMany { Post post ->
            Document document = wwwService.parse(post.html)

            allSuccessors(document).collect { Element element ->
                double label = post.trainMeaningHtml.contains(element.html()) ? 1.0 : 0.0

                double linksCount = element.select('a').size()
                double childrenCount = element.children().size()
                double textSize = element.text().length()
                double textSize2 = textSize * textSize
                double pointsCount = element.text().toCharArray().findAll({ '.,:;?!'.contains(it) }).size()

                Vector features = Vectors.dense(linksCount, childrenCount, textSize, textSize2, pointsCount)

                println "DATA: ${new LabeledPoint(label, features)}"

                new LabeledPoint(label, features)
            }
        }

        JavaRDD<LabeledPoint>[] splits = data.randomSplit([ 0.7, 0.3 ])
        JavaRDD<LabeledPoint> trainSet = splits[0]
        JavaRDD<LabeledPoint> testSet = splits[1]

        LogisticRegressionModel model = new LogisticRegressionWithLBFGS().run(trainSet.rdd())

        def scores = testSet.collect({ LabeledPoint point ->
            double prediction = model.predict(point.features())
            println "TEST: ${point} predicted as ${prediction}"
            if (point.features() > THRESHOLD) {
                return 1 - prediction
            } else {
                return prediction
            }
        })

        double score = scores.sum() / scores.size()

        println "SCORE: ${score}"
    }

    Collection<Element> allSuccessors(Element parent) {
        Collection<Element> result = []

        result.addAll(parent.children())
        parent.children().each { Element child ->
            result.addAll(allSuccessors(child))
        }

        return result
    }
}

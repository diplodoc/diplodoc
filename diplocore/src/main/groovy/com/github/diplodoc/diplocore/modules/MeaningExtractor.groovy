package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Post
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.PostRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleRepository
import com.github.diplodoc.diplocore.services.SerializationService
import com.github.diplodoc.diplocore.services.WwwService
import groovy.json.JsonOutput
import org.apache.commons.lang3.StringUtils
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext
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
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/meaning-extractor')
class MeaningExtractor {

    @Autowired
    PostRepository postRepository

    @Autowired
    ModuleRepository moduleRepository

    @Autowired
    WwwService wwwService

    @Autowired
    SerializationService serializationService

    @RequestMapping(value = '/post/{id}/extract-text', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void extractText(@PathVariable('id') String postId) {
        assert false : 'not implemented yet'
    }

    @RequestMapping(value = '/train-model', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody String trainModel() {
        def dataSplits = dataSplits()
        JavaRDD<LabeledPoint> trainSet = dataSplits['trainSet']
        JavaRDD<LabeledPoint> testSet = dataSplits['testSet']

        LogisticRegressionModel model = model(trainSet)
        Map metrics = metrics(model, testSet)

        Module module = moduleRepository.findOneByName(this.class.name)
        module.data['model'] = serializationService.serialize(model)
        moduleRepository.save module

        return JsonOutput.prettyPrint(JsonOutput.toJson(metrics))
    }

    JavaRDD<LabeledPoint>[] dataSplits() {
        Collection<LabeledPoint> data = postRepository.findByTrainMeaningHtmlIsNotNull().collectMany { Post post ->
            Document document = wwwService.parse(post.html)
            Collection<Element> positives = allSubelements(wwwService.parseFragment(post.trainMeaningHtml))

            allSubelements(document.body()).collect { Element element ->
                double label = ( (positives.find({ sameHtml(it, element) })) && (!element.text().isEmpty()) ) ? 1.0 : 0.0

                double size = element.outerHtml().length()
                double linksCount = element.select('a').size()
                double childrenCount = element.children().size()
                double ownTextLength = element.ownText().length()
                double pointsCount = element.text().toCharArray().findAll({ '.,:;?!'.contains(Character.toString(it)) }).size()

                Vector features = Vectors.dense(size, linksCount, childrenCount, ownTextLength, pointsCount)

                new LabeledPoint(label, features)
            }
        }

        SparkConf sparkConf = new SparkConf().setAppName('/diplocore/meaning-extractor/train-model').setMaster('local')
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf)

        JavaRDD<LabeledPoint> rdd = sparkContext.parallelize(data)

        JavaRDD<LabeledPoint>[] splits = rdd.randomSplit([ 0.7, 0.3 ] as double[])

        [ 'trainSet': splits[0], 'testSet': splits[1] ]
    }

    LogisticRegressionModel model(JavaRDD<LabeledPoint> trainSet) {
        new LogisticRegressionWithLBFGS().run(trainSet.rdd())
    }

    Map metrics(LogisticRegressionModel model, JavaRDD<LabeledPoint> testSet) {
        int testSetSize = testSet.toArray().size()
        double accuracySum = 0
        int truePositives = 0
        int trueNegatives = 0
        int falsePositives = 0
        int falseNegatives = 0

        testSet.toArray().each{ LabeledPoint point ->
            double prediction = model.predict(point.features())

            accuracySum += point.label() * (1 - prediction) + (1 - point.label()) * prediction
            if (point.label() == 1.0 && prediction == 1.0) truePositives++
            if (point.label() == 0.0 && prediction == 0.0) trueNegatives++
            if (point.label() == 0.0 && prediction == 1.0) falsePositives++
            if (point.label() == 1.0 && prediction == 0.0) falseNegatives++
        }

        Map metrics = [:]
        metrics.model = model.toString()
        metrics.accuracy = 1 - accuracySum / testSetSize
        metrics.truePositives = truePositives
        metrics.trueNegatives = trueNegatives
        metrics.falsePositives = falsePositives
        metrics.falseNegatives = falseNegatives
        metrics.precision = 1.0 * truePositives / (truePositives + falsePositives)
        metrics.recall = 1.0 * truePositives / (truePositives + falseNegatives)

        metrics
    }

    Collection<Element> allSubelements(Element element) {
        Collection result = []
        result.addAll(element.children())
        result.addAll(element.children().collectMany({ allSubelements(it) }))

        return result
    }

    boolean sameHtml(Element element1, Element element2) {
        String cleanedHtml1 = element1.outerHtml().replaceAll('\\s+','')
        String cleanedHtml2 = element2.outerHtml().replaceAll('\\s+','')
        int threshold = cleanedHtml1.length() / 50

        StringUtils.getLevenshteinDistance(cleanedHtml1, cleanedHtml2, threshold) != -1
    }
}

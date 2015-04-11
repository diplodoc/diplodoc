package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethodRun
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRunRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleRepository
import com.github.diplodoc.diplocore.services.HtmlService
import com.github.diplodoc.diplocore.services.SerializationService
import org.apache.commons.lang3.SerializationUtils
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
import org.springframework.web.bind.annotation.ResponseStatus

import java.time.LocalDateTime

/**
 * @author yaroslav.yermilov
 */
@Controller
@RequestMapping('/html-meaning-extractor')
class HtmlMeaningExtractor {

    @Autowired
    DocRepository docRepository

    @Autowired
    ModuleRepository moduleRepository

    @Autowired
    ModuleMethodRepository moduleMethodRepository

    @Autowired
    ModuleMethodRunRepository moduleMethodRunRepository

    @Autowired
    HtmlService htmlService

    @Autowired
    SerializationService serializationService

    @RequestMapping(value = '/doc/{id}/extract-meaning', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void extractMeaning(@PathVariable('id') String docId) {
        Doc doc = docRepository.findOne docId

        Module module = moduleRepository.findOneByName('com.github.diplodoc.diplocore.modules.MeaningExtractor')
        LogisticRegressionModel model = serializationService.deserialize(module.data['model'])

        String html = new String(doc.binary)
        Document document = htmlService.parse(html)

        List<Element> meaningElements = predictMeaningElements(model, document.body())

        doc.meaningHtml = meaningElements.collect({ it.outerHtml() }).join()
        doc.meaningText = meaningElements.collect({ it.text() }).join(' ')

        docRepository.save doc
    }

    @RequestMapping(value = '/train-model', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void trainModel() {
        ModuleMethodRun moduleMethodRun = new ModuleMethodRun(startTime: LocalDateTime.now().toString())

        def dataSplits = dataSplits()
        JavaRDD<LabeledPoint> trainSet = dataSplits['trainSet']
        JavaRDD<LabeledPoint> testSet = dataSplits['testSet']

        LogisticRegressionModel model = model(trainSet)
        Map metrics = metrics(model, trainSet, testSet)

        moduleMethodRun.endTime = LocalDateTime.now().toString()
        moduleMethodRun.metrics = metrics

        Module module = moduleRepository.findOneByName('com.github.diplodoc.diplocore.modules.MeaningExtractor')
        moduleMethodRun.moduleMethod = moduleMethodRepository.findByName('trainModel').find { it.moduleId.toString() == module.id }
        moduleMethodRunRepository.save moduleMethodRun

        if (!module.data) module.data = [:]
        module.data['model'] = serializationService.serialize(model)
        moduleRepository.save module
    }

    List<Element> predictMeaningElements(LogisticRegressionModel model, Element element) {
        double selfLabel = model.predict(elementFeatures(element))

        if (selfLabel == 1.0) {
            [ element ]
        } else {
            element.children().collectMany { predictMeaningElements(model, it) }
        }
    }

    Vector elementFeatures(Element element) {
        double size = element.outerHtml().length()
        double linksCount = element.select('a').size()
        double childrenCount = element.children().size()
        double ownTextLength = element.ownText().length()
        double pointsCount = element.text().toCharArray().findAll({ '.,:;?!'.contains(Character.toString(it)) }).size()

        Vectors.dense(size, linksCount, childrenCount, ownTextLength, pointsCount)
    }

    def dataSplits() {
        SparkConf sparkConf = new SparkConf().setAppName('/diplocore/meaning-extractor/train-model').setMaster('local')
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf)

        Collection<LabeledPoint> data = docRepository.findByTrainMeaningHtmlIsNotNull().collectMany(this.&docToLabeledPoints)

        JavaRDD<LabeledPoint>[] splits = sparkContext.parallelize(data).randomSplit([ 0.7, 0.3 ] as double[])

        [ 'trainSet': splits[0], 'testSet': splits[1] ]
    }

    Collection<LabeledPoint> docToLabeledPoints(Doc doc) {
        String html = new String(doc.binary)
        Document document = htmlService.parse(html)
        Collection<Element> positives = allSubelements(htmlService.parseFragment(doc.trainMeaningHtml))

        allSubelements(document.body()).collect { Element element ->
            double label = positives.find({ sameHtml(it, element) }) ? 1.0 : 0.0
            new LabeledPoint(label, elementFeatures(element))
        }
    }

    LogisticRegressionModel model(JavaRDD<LabeledPoint> trainSet) {
        new LogisticRegressionWithLBFGS().run(trainSet.rdd())
    }

    Map metrics(LogisticRegressionModel model, JavaRDD<LabeledPoint> trainSet, JavaRDD<LabeledPoint> testSet) {
        int testSetSize = testSet.toArray().size()
        int truePositives = 0
        int trueNegatives = 0
        int falsePositives = 0
        int falseNegatives = 0

        testSet.toArray().each{ LabeledPoint point ->
            double prediction = model.predict(point.features())

            if (point.label() == 1.0 && prediction == 1.0) truePositives++
            if (point.label() == 0.0 && prediction == 0.0) trueNegatives++
            if (point.label() == 0.0 && prediction == 1.0) falsePositives++
            if (point.label() == 1.0 && prediction == 0.0) falseNegatives++
        }

        Map metrics = [:]
        metrics.model = model.weights().toArray()
        metrics.trainSetSize = trainSet.toArray().size()
        metrics.testSetSize = testSetSize
        metrics.truePositives = truePositives
        metrics.trueNegatives = trueNegatives
        metrics.falsePositives = falsePositives
        metrics.falseNegatives = falseNegatives
        metrics.accuracy = 1.0 * (truePositives + trueNegatives) / testSetSize
        metrics.precision = 1.0 * truePositives / (truePositives + falsePositives)
        metrics.recall = 1.0 * truePositives / (truePositives + falseNegatives)

        metrics
    }

    Collection<Element> allSubelements(Element element) {
        Collection result = [ element ]
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

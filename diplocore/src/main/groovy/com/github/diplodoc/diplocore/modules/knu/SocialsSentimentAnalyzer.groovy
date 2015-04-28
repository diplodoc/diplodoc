package com.github.diplodoc.diplocore.modules.knu

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.AuditService
import com.github.diplodoc.diplocore.services.SerializationService
import groovy.util.logging.Slf4j
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.mllib.classification.SVMModel
import org.apache.spark.mllib.classification.SVMWithSGD
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.regression.LabeledPoint
import org.bson.types.ObjectId
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
@RequestMapping('/knu/socials-sentiment-analyzer')
@Slf4j
class SocialsSentimentAnalyzer {

    @Autowired
    DocRepository docRepository

    @Autowired
    AuditService auditService

    @Autowired
    SerializationService serializationService

    @RequestMapping(value = '/train-model', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def trainModel() {
        auditService.runMethodUnderAudit('knu.SocialsSentimentAnalyzer', 'trainModel') { module, moduleMethod, moduleMethodRun ->
            def dataSplits = dataSplits()
            JavaRDD<LabeledPoint> trainSet = dataSplits['trainSet']
            JavaRDD<LabeledPoint> testSet = dataSplits['testSet']

            SVMModel model = model(trainSet)

            if (!module.data) module.data = [:]
            module.data['model'] = serializationService.serialize(model)

            [ 'metrics': metrics(model, trainSet, testSet), 'module': module ]
        }
    }

    @RequestMapping(value = '/doc/{id}/analyze-sentiment', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def analyzeSentiment(@PathVariable('id') String docId) {
        auditService.runMethodUnderAudit('knu.SocialsSentimentAnalyzer', 'analyzeSentiment') { module, moduleMethod, moduleMethodRun ->
            moduleMethodRun.parameters = [ 'docId': docId ]

            Doc doc = docRepository.findOne new ObjectId(docId)
            SVMModel model = serializationService.deserialize(module.data['model'])
            doc.knuSocialPredictedSentimentScore = model.predict(elementFeatures(doc.meaningText))

            docRepository.save doc

            [ 'moduleMethodRun': moduleMethodRun ]
        }
    }

    def dataSplits() {
        SparkConf sparkConf = new SparkConf().setAppName('/diplocore/knu/socials-sentiment-analyzer/train-model').setMaster('local')
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf)

        assert null: 'not implemented yet'
//        Collection<LabeledPoint> data = docRepository.findByTrainMeaningHtmlIsNotNull().collectMany(this.&docToLabeledPoints)

        JavaRDD<LabeledPoint>[] splits = sparkContext.parallelize(data).randomSplit([ 0.7, 0.3 ] as double[])

        [ 'trainSet': splits[0], 'testSet': splits[1] ]
    }

    Vector elementFeatures(String element) {
//        List<String> words = doc.meaningText.split('\\s+').collect { String word -> word.toLowerCase().replaceAll('\\s+','') }
//        words.eachWithIndex { String word, int index ->
//            if (index > 0) {
//                String bigram = "${words[index - 1]} ${word}"
//            }
//        }
        assert null: 'not implemented yet'
    }

    SVMModel model(JavaRDD<LabeledPoint> trainSet) {
        SVMWithSGD.train(trainSet.rdd(), 100)
    }

    Map metrics(SVMModel model, JavaRDD<LabeledPoint> trainSet, JavaRDD<LabeledPoint> testSet) {
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
}

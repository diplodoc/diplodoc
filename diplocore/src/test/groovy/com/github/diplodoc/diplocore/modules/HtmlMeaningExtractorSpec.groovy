package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethod
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethodRun
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRunRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleRepository
import com.github.diplodoc.diplocore.services.HtmlService
import com.github.diplodoc.diplocore.services.SerializationService
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.mllib.classification.LogisticRegressionModel
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.bson.types.ObjectId
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class HtmlMeaningExtractorSpec extends Specification {

    DocRepository docRepository = Mock(DocRepository)
    ModuleRepository moduleRepository = Mock(ModuleRepository)
    ModuleMethodRepository moduleMethodRepository = Mock(ModuleMethodRepository)
    ModuleMethodRunRepository moduleMethodRunRepository = Mock(ModuleMethodRunRepository)
    HtmlService htmlService = Mock(HtmlService)
    SerializationService serializationService = Mock(SerializationService)

    HtmlMeaningExtractor meaningExtractor = Spy(HtmlMeaningExtractor)

    def 'void extractMeaning(String docId)'() {
        setup:
            meaningExtractor.docRepository = docRepository
            meaningExtractor.moduleRepository = moduleRepository
            meaningExtractor.htmlService = htmlService
            meaningExtractor.serializationService = serializationService

            LogisticRegressionModel model = Mock(LogisticRegressionModel)

            Doc doc = new Doc(id: 'doc-id', binary: 'doc-html'.bytes)
            Module module = new Module(name: 'com.github.diplodoc.diplocore.modules.MeaningExtractor', data: [ 'model': ([ 1, 2 ,3 ] as byte[]) ])

            Document document = Mock(Document)
            Element body = Mock(Element)
            document.body() >> body

            1 * docRepository.findOne('doc-id') >> doc
            1 * moduleRepository.findOneByName('com.github.diplodoc.diplocore.modules.MeaningExtractor') >> module
            1 * htmlService.parse('doc-html') >> document
            1 * serializationService.deserialize([ 1, 2 ,3 ] as byte[]) >> model

            1 * meaningExtractor.predictMeaningElements(model, body) >> [
                Jsoup.parseBodyFragment('<div>text 1</div>').body().child(0),
                Jsoup.parseBodyFragment('<div>text 2</div>').body().child(0),
                Jsoup.parseBodyFragment('<div/>').body().child(0),
                Jsoup.parseBodyFragment('<div>text 4</div>').body().child(0),
            ]

        when:
            meaningExtractor.extractMeaning('doc-id')

        then:
            1 * docRepository.save({ Doc docToSave ->
                docToSave.id == 'doc-id' &&
                docToSave.binary == 'doc-html'.bytes &&
                docToSave.meaningHtml.replaceAll('\\s+','') == '<div>text1</div><div>text2</div><div></div><div>text4</div>' &&
                docToSave.meaningText.replaceAll('\\s+','') == 'text1text2text4'
            })
    }

    def 'void trainModel()'() {
        setup:
            meaningExtractor.moduleRepository = moduleRepository
            meaningExtractor.moduleMethodRepository = moduleMethodRepository
            meaningExtractor.moduleMethodRunRepository = moduleMethodRunRepository
            meaningExtractor.serializationService = serializationService

            Module module = new Module(name: 'com.github.diplodoc.diplocore.modules.MeaningExtractor', id: '111111111111111111111111')

            1 * moduleRepository.findOneByName('com.github.diplodoc.diplocore.modules.MeaningExtractor') >> module

            JavaRDD<LabeledPoint> trainSet = Mock(JavaRDD)
            JavaRDD<LabeledPoint> testSet = Mock(JavaRDD)
            1 * meaningExtractor.dataSplits() >> [ 'trainSet': trainSet, 'testSet': testSet ]

            LogisticRegressionModel model = Mock(LogisticRegressionModel)
            1 * meaningExtractor.model(trainSet) >> model

            1 * meaningExtractor.metrics(model, trainSet, testSet) >> [ 'metric': 'value' ]

            serializationService.serialize(model) >> ([ 1, 2 ,3 ] as byte[])

            moduleMethodRepository.findByName('trainModel') >> [
                new ModuleMethod(id: 'method-1', name: 'trainModel', moduleId: new ObjectId('111111111111111111111111')),
                new ModuleMethod(id: 'method-2', name: 'trainModel', moduleId: new ObjectId('222222222222222222222222'))
            ]

        when:
            meaningExtractor.trainModel()

        then:
            1 * moduleRepository.save({ Module moduleToSave ->
                moduleToSave.id == '111111111111111111111111' &&
                moduleToSave.name == 'com.github.diplodoc.diplocore.modules.MeaningExtractor' &&
                moduleToSave.data == [ 'model': ([ 1, 2, 3 ] as byte[]) ]
            })

            1 * moduleMethodRunRepository.save({ ModuleMethodRun moduleMethodRunToSave ->
                moduleMethodRunToSave.startTime != null &&
                moduleMethodRunToSave.endTime != null &&
                moduleMethodRunToSave.metrics == [ 'metric': 'value' ] &&
                moduleMethodRunToSave.moduleMethod == new ModuleMethod(id: 'method-1', name: 'trainModel', moduleId: new ObjectId('111111111111111111111111'))
            })
    }

    def 'List<Element> predictMeaningElements(LogisticRegressionModel model, Element element) - predict 1.0 for element'() {
        setup:
            LogisticRegressionModel model = Mock(LogisticRegressionModel)
            Element element = Jsoup.parseBodyFragment('<div>text</div>').body().child(0)
            model.predict(_) >> 1.0

        when:
            List<Element> actual = meaningExtractor.predictMeaningElements(model, element)

        then:
            actual == [ element ]
    }

    def 'List<Element> predictMeaningElements(LogisticRegressionModel model, Element element) - predict 0.0 for element'() {
        setup:
            LogisticRegressionModel model = Mock(LogisticRegressionModel)
            Element rootElement = Jsoup.parseBodyFragment('<div><div>text 1</div><div>text 2</div></div>').body().child(0)
            Element element1 = Jsoup.parseBodyFragment('<div>text 1</div>').body().child(0)
            Element element2 = Jsoup.parseBodyFragment('<div>text 2</div>').body().child(0)
            model.predict(meaningExtractor.elementFeatures(rootElement)) >> 0.0
            model.predict(meaningExtractor.elementFeatures(element1)) >> 1.0
            model.predict(meaningExtractor.elementFeatures(element2)) >> 1.0

        when:
            List<Element> actual = meaningExtractor.predictMeaningElements(model, rootElement)

        then:
            actual.size() == 2
            actual[0].outerHtml() == element1.outerHtml()
            actual[1].outerHtml() == element2.outerHtml()
    }

    def 'Vector elementFeatures(Element element)'() {
        setup:
            Element element = Jsoup.parseBodyFragment('<div>own text<div>text:1</div><a></a><div>text 2.</div></div>').body().child(0)

        when:
            Vector actual = meaningExtractor.elementFeatures(element)

        then:
            actual == Vectors.dense(80.0, 1.0, 3.0, 8.0, 2.0)
    }

    def 'Collection<LabeledPoint> docToLabeledPoints(Doc doc)'() {
        setup:
            meaningExtractor.htmlService = htmlService

            Doc doc = new Doc(binary: 'doc-html'.bytes, trainMeaningHtml: 'doc-trainMeaningHtml')

            Document document = Mock(Document)
            document.body() >> Jsoup.parseBodyFragment('<div><div>text 1</div><div>text</div></div>').body().child(0)
            htmlService.parse('doc-html') >> document

            htmlService.parseFragment('doc-trainMeaningHtml') >> Jsoup.parseBodyFragment('<div>text 1</div>').body().child(0)

        when:
            Collection<LabeledPoint> actual = meaningExtractor.docToLabeledPoints(doc)

        then:
            actual == [
                new LabeledPoint(0.0, Vectors.dense(58.0, 0.0, 2.0, 0.0, 0.0)),
                new LabeledPoint(1.0, Vectors.dense(20.0, 0.0, 0.0, 6.0, 0.0)),
                new LabeledPoint(0.0, Vectors.dense(18.0, 0.0, 0.0, 4.0, 0.0))
            ]
    }

    def 'Map metrics(LogisticRegressionModel model, JavaRDD<LabeledPoint> trainSet, JavaRDD<LabeledPoint> testSet)'() {
        setup:
            LogisticRegressionModel model = Mock(LogisticRegressionModel)
            model.weights() >> Vectors.dense(1.0, 2.0, 3.0)
            model.predict(Vectors.dense(4.0)) >> 1.0
            model.predict(Vectors.dense(5.0)) >> 0.0

            JavaRDD<LabeledPoint> trainSet = Mock(JavaRDD)
            trainSet.toArray() >> [ new LabeledPoint(0.0, Vectors.dense(1.0)), new LabeledPoint(0.0, Vectors.dense(2.0)), new LabeledPoint(0.0, Vectors.dense(3.0)) ]

            JavaRDD<LabeledPoint> testSet = Mock(JavaRDD)
            testSet.toArray() >> [ new LabeledPoint(1.0, Vectors.dense(4.0)), new LabeledPoint(1.0, Vectors.dense(5.0)) ]

        when:
            Map actual = meaningExtractor.metrics(model, trainSet, testSet)

        then:
            actual.keySet().size() == 10
            actual.model == [ 1.0, 2.0, 3.0 ]
            actual.trainSetSize == 3
            actual.testSetSize == 2
            actual.truePositives == 1
            actual.trueNegatives == 0
            actual.falsePositives == 0
            actual.falseNegatives == 1
            actual.accuracy == 0.5
            actual.precision == 1
            actual.recall == 0.5
    }

    def 'Collection allSubelements(Element element) - no children'() {
        setup:
            Element element = Jsoup.parseBodyFragment('<div>text</div>').body().child(0)

        when:
            Collection<Element> actual = meaningExtractor.allSubelements(element)

        then:
            actual.size() == 1
            actual[0].outerHtml().replaceAll('\\s+','') == '<div>text</div>'
    }

    def 'Collection allSubelements(Element element) - with children'() {
        setup:
            Element element = Jsoup.parseBodyFragment('<div><div>text1</div><div>text2</div></div>').body().child(0)

        when:
            Collection<Element> actual = meaningExtractor.allSubelements(element)

        then:
            actual.size() == 3
            actual[0].outerHtml().replaceAll('\\s+','') == '<div><div>text1</div><div>text2</div></div>'
            actual[1].outerHtml().replaceAll('\\s+','') == '<div>text1</div>'
            actual[2].outerHtml().replaceAll('\\s+','') == '<div>text2</div>'
    }

    def 'boolean sameHtml(Element element1, Element element2) - same elements'() {
        setup:
            Element element1 = Jsoup.parseBodyFragment('<div>text</div>').body().child(0)
            Element element2 = Jsoup.parseBodyFragment('<div> text </div>').body().child(0)

        when:
            boolean actual = meaningExtractor.sameHtml(element1, element2)

        then:
            actual == true
    }

    def 'boolean sameHtml(Element element1, Element element2) - similar elements'() {
        setup:
            Element element1 = Jsoup.parseBodyFragment('<div>012345678901234567890123456789012345678901234567890123456789</div>').body().child(0)
            Element element2 = Jsoup.parseBodyFragment('<div>12345678901234567890123456789012345678901234567890123456789</div>').body().child(0)

        when:
            boolean actual = meaningExtractor.sameHtml(element1, element2)

        then:
            actual == true
    }

    def 'boolean sameHtml(Element element1, Element element2) - different elements'() {
        setup:
            Element element1 = Jsoup.parseBodyFragment('<div>text</div>').body().child(0)
            Element element2 = Jsoup.parseBodyFragment('<div> other text </div>').body().child(0)

        when:
            boolean actual = meaningExtractor.sameHtml(element1, element2)

        then:
            actual == false
    }
}

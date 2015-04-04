package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Post
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethod
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.ModuleMethodRun
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.PostRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleMethodRunRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleRepository
import com.github.diplodoc.diplocore.services.HtmlService
import com.github.diplodoc.diplocore.services.SerializationService
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.mllib.classification.LogisticRegressionModel
import org.apache.spark.mllib.regression.LabeledPoint
import org.bson.types.ObjectId
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class MeaningExtractorSpec extends Specification {

    PostRepository postRepository = Mock(PostRepository)
    ModuleRepository moduleRepository = Mock(ModuleRepository)
    ModuleMethodRepository moduleMethodRepository = Mock(ModuleMethodRepository)
    ModuleMethodRunRepository moduleMethodRunRepository = Mock(ModuleMethodRunRepository)
    HtmlService htmlService = Mock(HtmlService)
    SerializationService serializationService = Mock(SerializationService)

    MeaningExtractor meaningExtractor = Spy(MeaningExtractor)

    def 'void extractMeaning(String postId)'() {
        setup:
            meaningExtractor.postRepository = postRepository
            meaningExtractor.moduleRepository = moduleRepository
            meaningExtractor.htmlService = htmlService
            meaningExtractor.serializationService = serializationService

            LogisticRegressionModel model = Mock(LogisticRegressionModel)

            Post post = new Post(id: 'post-id', html: 'post-html')
            Module module = new Module(name: 'com.github.diplodoc.diplocore.modules.MeaningExtractor', data: [ 'model': ([ 1, 2 ,3 ] as byte[]) ])

            Document document = Mock(Document)
            Element body = Mock(Element)
            document.body() >> body

            1 * postRepository.findOne('post-id') >> post
            1 * moduleRepository.findOneByName('com.github.diplodoc.diplocore.modules.MeaningExtractor') >> module
            1 * htmlService.parse('post-html') >> document
            1 * serializationService.deserialize([ 1, 2 ,3 ] as byte[]) >> model

            1 * meaningExtractor.predictMeaningElements(model, body) >> [
                    Jsoup.parseBodyFragment('<div>text 1</div>').body().child(0),
                    Jsoup.parseBodyFragment('<div>text 2</div>').body().child(0),
                    Jsoup.parseBodyFragment('<div/>').body().child(0),
                    Jsoup.parseBodyFragment('<div>text 4</div>').body().child(0),
            ]

        when:
            meaningExtractor.extractMeaning('post-id')

        then:
            1 * postRepository.save({ Post postToSave ->
                postToSave.id == 'post-id' &&
                postToSave.html == 'post-html' &&
                postToSave.meaningHtml.replaceAll('\\s+','') == '<div>text1</div><div>text2</div><div></div><div>text4</div>' &&
                postToSave.meaningText.replaceAll('\\s+','') == 'text1text2text4'
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
                moduleMethodRunToSave.moduleMethod == new ModuleMethod(id: 'method-1')
            })
    }
}

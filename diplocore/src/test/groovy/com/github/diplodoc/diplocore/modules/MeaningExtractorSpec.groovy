package com.github.diplodoc.diplocore.modules

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Post
import com.github.diplodoc.diplobase.domain.mongodb.diploexec.Module
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.PostRepository
import com.github.diplodoc.diplobase.repository.mongodb.diploexec.ModuleRepository
import com.github.diplodoc.diplocore.services.HtmlService
import com.github.diplodoc.diplocore.services.SerializationService
import org.apache.commons.lang3.SerializationUtils
import org.apache.spark.mllib.classification.LogisticRegressionModel
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

            postRepository.findOne('post-id') >> post
            moduleRepository.findOneByName('com.github.diplodoc.diplocore.modules.MeaningExtractor') >> module
            htmlService.parse('post-html') >> document
            serializationService.deserialize([ 1, 2 ,3 ] as byte[]) >> model

            meaningExtractor.predictMeaningElements(model, body) >> [
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
}

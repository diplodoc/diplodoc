package com.github.diplodoc.diplocore.modules.knu

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.AuditService
import com.github.diplodoc.diplocore.services.SerializationService
import groovy.util.logging.Slf4j
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.mllib.classification.SVMModel
import org.apache.spark.mllib.classification.SVMWithSGD
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF
import org.apache.spark.mllib.regression.LabeledPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
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

    @RequestMapping(value = '/analyze-all-sentiments', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def analyzeAllSentiments() {
        auditService.runMethodUnderAudit('knu.SocialsSentimentAnalyzer', 'analyzeAllSentiments') { module, moduleMethod, moduleMethodRun ->
            List<Doc> socials = docRepository.findByKnu('social')
            List words = socials.findAll({ Doc doc -> doc.meaningText != null }).collect { Doc doc ->
                doc.meaningText.split('\\s+').collect { String word -> word.toLowerCase().replaceAll('\\s+','') }
            }
            log.info "words=${words}"

            Map<String, Double> trainingBigrams = serializationService.deserialize(module.data['training-bigrams'])
            log.info "trainingBigrams=${trainingBigrams}"

            List bigrams = words.collect { List socialWords ->
                List socialBigrams = []
                socialWords.eachWithIndex { String word, int index ->
                    if (index > 0) {
                        socialBigrams << "${socialWords[index - 1]} ${word}"
                    }
                }
                return socialBigrams
            }
            bigrams.addAll(trainingBigrams.keySet().collect([ it ]))
            log.info "bigrams=${bigrams}"

            SparkConf sparkConf = new SparkConf().setAppName('/diplocore/knu/socials-sentiment-analyzer/analyze-all-sentiments').setMaster('local')
            JavaSparkContext sparkContext = new JavaSparkContext(sparkConf)

            def tf = new HashingTF().transform(sparkContext.parallelize(bigrams))
            def tfidf = new IDF().fit(tf).transform(tf).toArray()

            List trainSet = []
            bigrams.eachWithIndex { List bigram, int index ->
                if (index >= bigrams.size() - trainingBigrams.keySet().size()) {
                    trainSet << new LabeledPoint(trainingBigrams.get(bigram.first()), tfidf[index])
                }
            }
            log.info "trainSet=${trainSet}"

            SVMModel model = SVMWithSGD.train(sparkContext.parallelize(trainSet).rdd(), 100)

            socials.eachWithIndex { Doc social, int index ->
                social.knuSocialPredictedSentimentScore = model.predict(tfidf[index])
                log.info "social.[${social.id}].sentiment=${social.knuSocialPredictedSentimentScore}"
            }

            docRepository.save socials
        }
    }
}

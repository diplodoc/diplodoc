package com.github.diplodoc.modules.knu

import com.github.diplodoc.domain.mongodb.data.Doc
import com.github.diplodoc.domain.repository.mongodb.data.DocRepository
import com.github.diplodoc.modules.services.AuditService
import groovy.util.logging.Slf4j
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
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
@RequestMapping('/knu/posts-social-detector')
@Slf4j
class PostsSocialDetector {

    @Autowired
    DocRepository docRepository

    @Autowired
    AuditService auditService

    @RequestMapping(value = '/detect-related-socials', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def detectRelatedSocials() {
        auditService.runMethodUnderAudit('knu.PostsSocialDetector', 'detectRelatedSocials') { module, moduleMethod, moduleMethodRun ->
            List<Doc> posts = docRepository.findByKnu('post')
            List<Doc> socials = docRepository.findByKnu('social')

            List words = (posts + socials).findAll({ Doc doc -> doc.meaningText != null }).collect { Doc doc ->
                doc.meaningText.split('\\s+').collect { String word -> word.toLowerCase().replaceAll('\\s+','') }
            }

            SparkConf sparkConf = new SparkConf().setAppName('/modules-java/knu/posts-social-detector/detect-related-socials').setMaster('local')
            JavaSparkContext sparkContext = new JavaSparkContext(sparkConf)

            def tf = new HashingTF().transform(sparkContext.parallelize(words))
            def tfidf = new IDF().fit(tf).transform(tf).toArray()

            tfidf.eachWithIndex { Vector vector1, int i ->
                def knu_similarities = [:]
                tfidf.eachWithIndex { Vector vector2, int j ->
                    if (j >= posts.size() && i != j) {
                        double dist = Vectors.sqdist(vector1, vector2)
                        knu_similarities.put((posts + socials)[j].id, dist)
                    }
                }
                (posts + socials)[i].knuSimilarities = knu_similarities
            }

            docRepository.save(posts + socials)

            [ : ]
        }
    }
}

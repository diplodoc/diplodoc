package com.github.diplodoc.modules.knu

import com.github.diplodoc.domain.mongodb.data.Doc
import com.github.diplodoc.domain.repository.mongodb.data.DocRepository
import com.github.diplodoc.modules.services.AuditService
import groovy.util.logging.Slf4j
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF
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
@RequestMapping('/knu/related-documents-detector')
@Slf4j
class RelatedDocumentsDetector {

    @Autowired
    DocRepository docRepository

    @Autowired
    AuditService auditService

    @RequestMapping(value = '/detect-related', method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    def detectRelated() {
        auditService.runMethodUnderAudit('knu.RelatedDocumentsDetector', 'detectRelated') { module, moduleMethod, moduleMethodRun ->
            List<Doc> documents = docRepository.findByKnu('document')
            List words = documents.findAll({ Doc doc -> doc.meaningText != null }).collect { Doc doc ->
                doc.meaningText.split('\\s+').collect { String word -> word.toLowerCase().replaceAll('\\s+','') }
            }

            SparkConf sparkConf = new SparkConf().setAppName('/modules-java/knu/related-documents-detector/detect-related').setMaster('local')
            JavaSparkContext sparkContext = new JavaSparkContext(sparkConf)
            def wordsRdd = sparkContext.parallelize(words)

            def tf = new HashingTF().transform(wordsRdd)
            def tfidf = new IDF().fit(tf).transform(tf).toArray()

            tfidf.eachWithIndex { Vector vector1, int i ->
                def knu_similarities = [:]
                tfidf.eachWithIndex { Vector vector2, int j ->
                    if (i != j) {
                        double dist = Vectors.sqdist(vector1, vector2)
                        knu_similarities.put(documents[j].id, dist)
                    }
                }
                documents[i].knuSimilarities = knu_similarities
            }

            docRepository.save(documents)

            [ : ]
        }
    }
}

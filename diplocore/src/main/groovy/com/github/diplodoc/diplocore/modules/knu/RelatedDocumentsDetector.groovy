package com.github.diplodoc.diplocore.modules.knu

import com.github.diplodoc.diplobase.domain.mongodb.diplodata.Doc
import com.github.diplodoc.diplobase.repository.mongodb.diplodata.DocRepository
import com.github.diplodoc.diplocore.services.AuditService
import groovy.util.logging.Slf4j
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF
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
            Collection<Doc> documents = docRepository.findByKnu('document')
            Collection<String> words = documents
                                        .collectMany { Doc doc ->
                                            doc.meaningText.split('\\s+')
                                        }
                                        .collect { String word -> word.replaceAll('\\s+','') }

            SparkConf sparkConf = new SparkConf().setAppName('/diplocore/knu/related-documents-detector/detect-related').setMaster('local')
            JavaSparkContext sparkContext = new JavaSparkContext(sparkConf)
            def wordsRdd = sparkContext.parallelize(words)

            def tf = new HashingTF().transform(wordsRdd)

            def idf = new IDF().fit(tf)
            def tfidf = idf.transform(tf)
        }
    }
}

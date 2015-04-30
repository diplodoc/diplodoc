package com.github.dipodoc.diploweb.controller.knu

import com.github.dipodoc.diploweb.domain.diplodata.Doc
import com.github.dipodoc.diploweb.domain.diploexec.Module
import grails.transaction.Transactional
import org.springframework.security.access.annotation.Secured

@Transactional(readOnly = true)
@Secured([ 'ROLE_ADMIN' ])
class TrainSentimentsController {

    def list() {
        Module module = Module.findByName('knu.SocialsSentimentAnalyzer')
        Map trainingBigrams = module.data?.get('training-bigrams') ?: [:]
        [ trainingBigrams: trainingBigrams ]
    }

    def trainNext() {
        List<Doc> socials = Doc.where({ knu == 'social' }).list()
        List words = socials.findAll({ Doc doc -> doc.meaningText != null }).collect { Doc doc ->
            doc.meaningText.split('\\s+').collect { String word -> word.toLowerCase().replaceAll('\\s+','') }
        }

        List bigrams = words.collectMany { List socialWords ->
            List socialBigrams = []
            socialWords.eachWithIndex { String word, int index ->
                if (index > 0) {
                    socialBigrams << "${socialWords[index - 1]} ${word}"
                }
            }
            return socialBigrams
        }

        Module module = Module.findByName('knu.SocialsSentimentAnalyzer')
        Map trainingBigrams = module.data?.get('training-bigrams') ?: [:]

        String bigramToTrain = null
        while (!bigramToTrain) {
            int index = new Random().nextInt(bigrams.size())
            if (!trainingBigrams.containsKey(bigrams[index])) {
                bigramToTrain = bigrams[index]
            }
        }
        [ bigramToTrain: bigramToTrain ]
    }

    @Transactional
    def saveAndNext() {
        Module module = Module.findByName('knu.SocialsSentimentAnalyzer')
        Map trainingBigrams = module.data?.get('training-bigrams') ?: [:]

        trainingBigrams.put(params.bigram, Double.parseDouble(params.sentimentScore))

        if (!module.data) module.data = [:]
        module.data['training-bigrams'] = trainingBigrams
        module.save flush:true

        redirect action: 'trainNext'
    }
}
